package com.xxx.myspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xxx.myspringboot.annotation.PermissionAction;
import com.xxx.myspringboot.annotation.PermissionHandler;
import com.xxx.myspringboot.annotation.PermissionScanner;
import com.xxx.myspringboot.entity.SysHandler;
import com.xxx.myspringboot.entity.SysModule;
import com.xxx.myspringboot.entity.SysPermit;
import com.xxx.myspringboot.entity.SysRolePermit;
import com.xxx.myspringboot.mapper.SysHandlerMapper;
import com.xxx.myspringboot.mapper.SysModuleMapper;
import com.xxx.myspringboot.mapper.SysPermitMapper;
import com.xxx.myspringboot.mapper.SysRolePermitMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限同步
 */
@Slf4j
@Service
@RequiredArgsConstructor //Lombok 自动生成构造器
public class PermissionSyncService {

    private final PermissionScanner permissionScanner;
    private final SysHandlerMapper handlerMapper;
    private final SysPermitMapper permitMapper;
    private final SysRolePermitMapper rolePermitMapper;
    private final SysModuleMapper sysModuleMapper;

    /**
     * 同步权限
     */
    @Transactional(rollbackFor = Exception.class)
    public String sync() {
        List<Class<?>> controllerList = permissionScanner.getPermissionControllerList();
        if (controllerList.isEmpty()) {
            throw new RuntimeException("未扫描到任何权限控制器,同步已中止");
        }

        // ========== 1. 处理模块 ==========
        // 取所有的模块名称，先按 orderNo 降序，再按模块名去重（同名模块取 orderNo 最大的那个）
        Map<String, Integer> moduleOrderMap = controllerList.stream()
                .map(c -> AnnotationUtils.findAnnotation(c, PermissionHandler.class))
                .filter(Objects::nonNull)
                .filter(h -> StringUtils.isNotBlank(h.module()))
                .sorted(Comparator.comparingInt(PermissionHandler::orderNo).reversed())
                .collect(Collectors.toMap(
                        PermissionHandler::module,
                        PermissionHandler::orderNo,
                        (existing, replacement) -> existing,   // 已存在保留先到的（即 orderNo 大的）
                        LinkedHashMap::new
                ));


        List<SysModule> dbModuleList = sysModuleMapper.selectList(null);
        List<Long> moduleIdList = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : moduleOrderMap.entrySet()) {
            String moduleName = entry.getKey();
            Integer orderNo = entry.getValue();

            SysModule entityModule = dbModuleList.stream()
                    .filter(m -> moduleName.equals(m.getModuleName()))
                    .findFirst().orElse(null);

            if (entityModule == null) {
                entityModule = new SysModule();
                entityModule.setModuleName(moduleName);
                entityModule.setOrderNo(orderNo);
                sysModuleMapper.insert(entityModule);
                log.info("【权限数据初始化】新增模块:{},排序:{},数据编号:{}",
                        moduleName, orderNo, entityModule.getId());
            } else if (!Objects.equals(entityModule.getOrderNo(), orderNo)) {
                entityModule.setOrderNo(orderNo);
                sysModuleMapper.updateById(entityModule);
                log.info("【权限数据初始化】修改模块排序:{},排序:{}", moduleName, orderNo);
            }
            moduleIdList.add(entityModule.getId());
        }

        // 删除冗余模块
        if (!moduleIdList.isEmpty()) {
            sysModuleMapper.delete(new LambdaQueryWrapper<SysModule>()
                    .notIn(SysModule::getId, moduleIdList));
        }

        // 重新查询，供后续使用
        dbModuleList = sysModuleMapper.selectList(null);
        Map<String, SysModule> moduleMap = dbModuleList.stream()
                .collect(Collectors.toMap(SysModule::getModuleName, m -> m, (a, b) -> a));

        // ========== 2. 处理功能 & 权限 ==========
        List<SysHandler> dbHandlerList = handlerMapper.selectList(null);
        List<SysPermit> dbPermitList = permitMapper.selectList(null);

        List<Long> handlerIdList = new ArrayList<>();
        List<Long> permitIdList = new ArrayList<>();

        for (Class<?> controllerInfo : controllerList) {
            PermissionHandler handlerAttr = AnnotationUtils.findAnnotation(controllerInfo, PermissionHandler.class);
            if (handlerAttr == null) continue;

            String controllerFullName = controllerInfo.getName();
            String moduleName = handlerAttr.module();//模块名称
            String handlerName = handlerAttr.handler();//功能名称
            String handlerAliasName = handlerAttr.alias();//别名
            if (!StringUtils.isNotBlank(moduleName)
                    || !StringUtils.isNotBlank(handlerName)
                    || !StringUtils.isNotBlank(handlerAliasName)) {
                continue;
            }

            SysModule moduleEntity = moduleMap.get(moduleName);
            if (moduleEntity == null) continue;

            // ---- 功能 ----
            SysHandler entityHandler = dbHandlerList.stream()
                    .filter(h -> controllerFullName.equals(h.getRefController()))
                    .findFirst().orElse(null);

            Long handlerId;
            if (entityHandler == null) {
                SysHandler newHandler = new SysHandler();
                newHandler.setModuleId(moduleEntity.getId());
                newHandler.setHandlerName(handlerName);
                newHandler.setAliasName(handlerAliasName);
                newHandler.setOrderNo(handlerAttr.orderNo());
                newHandler.setRefController(controllerFullName);
                handlerMapper.insert(newHandler);
                handlerId = newHandler.getId();
                log.info("【权限数据初始化】新增功能：{}，所属模块：{}，命名空间：{}",
                        handlerName, moduleName, controllerFullName);
            } else {
                handlerId = entityHandler.getId();
                entityHandler.setModuleId(moduleEntity.getId());
                entityHandler.setHandlerName(handlerName);
                entityHandler.setAliasName(handlerAliasName);
                entityHandler.setOrderNo(handlerAttr.orderNo());
                handlerMapper.updateById(entityHandler);
                log.info("【权限数据初始化】修改功能：{}，所属模块：{}，命名空间：{}",
                        handlerName, moduleName, controllerFullName);
            }
            handlerIdList.add(handlerId);

            // ---- 权限（扫描方法）----
            // 用 LinkedHashMap 保留插入顺序；value = aliasName
            Map<String, String> operationNameList = new LinkedHashMap<>();
            for (Method method : controllerInfo.getDeclaredMethods()) {
                PermissionAction permissionAttr = AnnotationUtils.findAnnotation(method, PermissionAction.class);
                if (permissionAttr == null) continue;

                String[] operations = permissionAttr.name()
                        .split("[,;]");
                for (String op : operations) {
                    String trimmed = op.trim();
                    if (trimmed.isEmpty()) continue;
                    operationNameList.putIfAbsent(trimmed, permissionAttr.alias());
                }
            }

            for (Map.Entry<String, String> entry : operationNameList.entrySet()) {
                String operationName = entry.getKey();
                String aliasName = entry.getValue();

                SysPermit entityPermit = dbPermitList.stream()
                        .filter(p -> handlerId.equals(p.getHandlerId())
                                && operationName.equals(p.getPermitName()))
                        .findFirst().orElse(null);

                Long permitId;
                if (entityPermit == null) {
                    SysPermit newPermit = new SysPermit();
                    newPermit.setHandlerId(handlerId);
                    newPermit.setPermitName(operationName);
                    newPermit.setAliasName(aliasName);
                    permitMapper.insert(newPermit);
                    permitId = newPermit.getId();
                    log.info("【权限数据初始化】新增权限：{}，所属功能:{}，所属模块:{}",
                            operationName, handlerName, moduleName);
                } else {
                    permitId = entityPermit.getId();
                    if (!Objects.equals(entityPermit.getAliasName(), aliasName)) {
                        permitMapper.update(null,
                                new LambdaUpdateWrapper<SysPermit>()
                                        .eq(SysPermit::getId, permitId)
                                        .set(SysPermit::getAliasName, aliasName));
                        log.info("【权限数据初始化】修改权限别名：{}，新别名：{}，所属模块:{}，命名空间：{}",
                                operationName, aliasName, moduleName, controllerFullName);
                    }
                }
                permitIdList.add(permitId);
            }
        }

        // ========== 3. 删除冗余数据 ==========
        int delHandlerCount = 0;
        if (!handlerIdList.isEmpty()) {
            delHandlerCount = handlerMapper.delete(
                    new LambdaQueryWrapper<SysHandler>().notIn(SysHandler::getId, handlerIdList));
        }
        int delPermitCount = 0;
        if (!permitIdList.isEmpty()) {
            delPermitCount = permitMapper.delete(
                    new LambdaQueryWrapper<SysPermit>().notIn(SysPermit::getId, permitIdList));
            rolePermitMapper.delete(
                    new LambdaQueryWrapper<SysRolePermit>().notIn(SysRolePermit::getPermitId, permitIdList));
        }
        log.info("【权限数据初始化】清除冗余数据：功能清除{}条，权限清除{}条",
                delHandlerCount, delPermitCount);

        return "更新完毕";
    }

//    /**
//     * 使用Hutool工具扫描所有带注解的Controller
//     */
//    private List<Class<?>> scanControllers() {
//        // 推荐用 Spring 的 ClassPathScanningCandidateComponentProvider
//        // 或直接注入 ApplicationContext 获取所有 @RestController
//        return SpringUtil.getBeansOfType(Object.class).values().stream()
//                .map(Object::getClass)
//                .filter(c -> c.isAnnotationPresent(PermissionHandler.class)
//                        || c.getSuperclass().isAnnotationPresent(PermissionHandler.class))
//                .distinct()
//                .collect(Collectors.toList());
//    }
}
