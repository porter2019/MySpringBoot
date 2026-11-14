package com.xxx.myspringboot.service.impl.sys;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.myspringboot.entity.sys.SysDirectoryCategory;
import com.xxx.myspringboot.entity.sys.SysDirectoryItemFullView;
import com.xxx.myspringboot.mapper.sys.SysDirectoryCategoryMapper;
import com.xxx.myspringboot.service.sys.ISysDirectoryCategoryService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.service.sys.ISysDirectoryItemFullViewService;
import com.xxx.myspringboot.util.EnumOptionUtil;
import com.xxx.myspringboot.util.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典类别 服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysDirectoryCategoryServiceImpl extends ServiceImpl<SysDirectoryCategoryMapper, SysDirectoryCategory> implements ISysDirectoryCategoryService {

    private final ISysDirectoryItemFullViewService sysDirectoryItemFullViewService;

    /**
     * 根据搜索文本获取树形结构列表
     *
     * @param searchText 搜索关键字，用于筛选树节点名称
     * @return 返回构建好的树形结构列表
     */
    public List<SysDirectoryCategory> getTreeList(String searchText) {
        var allData = baseMapper.selectList(null);
        if (StringUtils.isBlank(searchText)) {
            //没有搜索条件，构造所有
            return TreeUtil.buildTree(allData,
                    SysDirectoryCategory::getId,
                    SysDirectoryCategory::getParentId,
                    SysDirectoryCategory::setParent,
                    SysDirectoryCategory::setChilds,
                    Comparator.comparing(SysDirectoryCategory::getFullOrderNo, Comparator.nullsLast(String::compareTo)));
        } else {
            //搜索的节点会缺少父数据，传入所有数据重新构造完整树
            var queryWrapper = new LambdaQueryWrapper<SysDirectoryCategory>();
            queryWrapper.like(SysDirectoryCategory::getName, searchText);
            var matchedData = baseMapper.selectList(queryWrapper);
            Set<Long> matchedIds = matchedData.stream().map(SysDirectoryCategory::getId).collect(Collectors.toSet());
            return TreeUtil.buildTreeWithCondition(
                    allData,
                    matchedIds,
                    SysDirectoryCategory::getId,
                    SysDirectoryCategory::getParentId,
                    SysDirectoryCategory::setParent,
                    SysDirectoryCategory::setChilds,
                    Comparator.comparing(SysDirectoryCategory::getFullOrderNo, Comparator.nullsLast(String::compareTo)));
        }
    }

    /**
     * 根据父节点ID获取下一个排序数字
     *
     * @param parentId 父节点ID
     * @return 下一个顺序数字
     */
    public String getNextOrderNo(Long parentId) {
        int length = 3;
        var parentModel = baseMapper.selectById(parentId);
        var parentNo = parentModel == null ? "" : parentModel.getOrderNo();

        var queryMaxWrapper = new LambdaQueryWrapper<SysDirectoryCategory>();
        queryMaxWrapper.eq(SysDirectoryCategory::getParentId, parentId);
        //取一条
        queryMaxWrapper.last("limit 1");
        queryMaxWrapper.orderByDesc(SysDirectoryCategory::getOrderNo);
        var nowMaxModel = baseMapper.selectOne(queryMaxWrapper);
        String lastOrderNo = "001";
        if (nowMaxModel != null) {
            int num = NumberUtils.toInt(nowMaxModel.getOrderNo(), 0) + 1;
            String str = String.valueOf(num);
            if (str.length() > length) {
                str = str.substring(str.length() - length);
            }
            lastOrderNo = String.format("%" + length + "s", str).replace(' ', '0');
        }
        return parentNo + lastOrderNo;
    }


    /**
     * 添加
     *
     * @param tree 要添加的树节点对象
     * @return 返回插入操作的结果
     */
    public Integer add(SysDirectoryCategory tree) {
        var result = baseMapper.insert(tree); // 将树节点插入数据库
        //执行存储过程以更新树节点的层级信息
        baseMapper.callUpdateLayer();
        return result;
    }

    /**
     * 修改
     *
     * @param tree 要添加的树节点对象
     * @return 返回插入操作的结果
     */
    public Integer edit(SysDirectoryCategory tree) {
        var result = baseMapper.updateById(tree);
        //执行存储过程以更新树节点的层级信息
        baseMapper.callUpdateLayer();
        return result;
    }

    /**
     * 删除
     *
     * @param id 要删除的树节点ID
     * @return 返回删除操作的结果
     */
    public Integer delete(Long id) {
        return baseMapper.deleteWithChildren(id);
    }

    /**
     * 构建枚举列表
     *
     * @param codes 枚举代码数组
     * @return 枚举列表
     */
    @Override
    public List<EnumOptionUtil.EnumGroupVO> buildEnumList(String... codes) {
        if (ArrayUtil.isEmpty(codes)) return List.of();
        var wrapper = new LambdaQueryWrapper<SysDirectoryItemFullView>();
        wrapper.eq(SysDirectoryItemFullView::getStatus, true);
        wrapper.in(SysDirectoryItemFullView::getCategoryCode, Arrays.asList(codes));
        wrapper.orderByAsc(SysDirectoryItemFullView::getSort);
        var allData = sysDirectoryItemFullViewService.list(wrapper);
        var resultList = new ArrayList<EnumOptionUtil.EnumGroupVO>();
        for (String code : codes) {
            var currList = allData.stream().filter(x -> x.getCategoryCode().equals(code)).toList();
            var optionList = new ArrayList<EnumOptionUtil.OptionVO>();
            for (SysDirectoryItemFullView item : currList) {
                optionList.add(new EnumOptionUtil.OptionVO(item.getName(), item.getValue()));
            }
            resultList.add(new EnumOptionUtil.EnumGroupVO(code, optionList));
        }
        return resultList;
    }
}
