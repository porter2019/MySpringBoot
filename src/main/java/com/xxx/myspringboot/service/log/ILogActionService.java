package com.xxx.myspringboot.service.log;

import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.event.DataChangeEvent;
import com.xxx.myspringboot.dto.input.log.LogActionPageInput;
import com.xxx.myspringboot.entity.log.LogAction;

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

    /**
     * 保存变更日志
     *
     * @param dto dto
     */
    void saveChangeLog(DataChangeEvent dto);
}
