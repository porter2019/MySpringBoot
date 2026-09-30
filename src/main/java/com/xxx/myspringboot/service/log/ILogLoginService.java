package com.xxx.myspringboot.service.log;

import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.log.LogLoginPageInput;
import com.xxx.myspringboot.entity.log.LogLogin;
import com.baomidou.mybatisplus.spring.service.IService;

/**
 * 登录日志 服务类
 */
public interface ILogLoginService extends IService<LogLogin> {
    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    PageResult<LogLogin> getPageList(LogLoginPageInput input);
}
