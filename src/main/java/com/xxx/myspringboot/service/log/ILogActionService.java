package com.xxx.myspringboot.service.log;

import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.log.LogActionPageInput;
import com.xxx.myspringboot.entity.log.LogAction;
import com.baomidou.mybatisplus.spring.service.IService;

/**
 * 操作日志 服务类
 */
public interface ILogActionService extends IService<LogAction> {
    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    PageResult<LogAction> getPageList(LogActionPageInput input);
}
