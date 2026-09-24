package com.xxx.myspringboot.handler;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.xxx.myspringboot.common.CurrentConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
//        log.info("开始插入填充...");
        Long userId = getCurrentUserId();
        String userName = getCurrentUserName();

        this.strictInsertFill(metaObject, "createdUserId", Long.class, userId);
        this.strictInsertFill(metaObject, "createdUserName", String.class, userName);
        this.strictInsertFill(metaObject, "updatedUserId", Long.class, userId);
        this.strictInsertFill(metaObject, "updatedUserName", String.class, userName);
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "isDeleted", Boolean.class, false);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        log.info("开始更新填充...");
        Long userId = getCurrentUserId();
        String userName = getCurrentUserName();

//        this.strictUpdateFill(metaObject, "updatedUserId", Long.class, userId);
//        this.strictUpdateFill(metaObject, "updatedUserName", String.class, userName);
//        this.strictUpdateFill(metaObject, "updatedTime", LocalDateTime.class, LocalDateTime.now());
        //强制重新设置内容
        this.setFieldValByName("updatedUserId", userId, metaObject);
        this.setFieldValByName("updatedUserName", userName, metaObject);
        this.setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
    }

    private Long getCurrentUserId() {
        Object loginId = StpUtil.getLoginIdDefaultNull();
        return loginId == null ? 0 : Long.parseLong(loginId.toString());
    }

    private String getCurrentUserName() {
        if (!StpUtil.isLogin()) {
            return null;
        }
//        var session = StpUtil.getSession();
//        if (session != null) {
//            User userInfo = session.getModel("user", User.class);
//            return userInfo == null ? null : userInfo.getName();
//        } else {
//            return null;
//        }
        Object name = StpUtil.getSession().get(CurrentConst.UserName);
        return name == null ? null : name.toString();
    }
}