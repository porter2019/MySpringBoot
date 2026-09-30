package com.xxx.myspringboot.service.impl.log;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.log.LogActionPageInput;
import com.xxx.myspringboot.entity.log.LogAction;
import com.xxx.myspringboot.mapper.log.LogActionMapper;
import com.xxx.myspringboot.service.log.ILogActionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 操作日志 服务实现类
 */
@Service
public class LogActionServiceImpl extends ServiceImpl<LogActionMapper, LogAction> implements ILogActionService {
    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    public PageResult<LogAction> getPageList(LogActionPageInput input) {
        var wrapper = new LambdaQueryWrapper<LogAction>();
        wrapper.eq(input.getType() != null && input.getType() > 0, LogAction::getType, input.getType());
        wrapper.eq(input.getClientType() != null && input.getClientType() > 0, LogAction::getClientType, input.getClientType());
        wrapper.like(StringUtils.isNotBlank(input.getOperator()), LogAction::getOperator, input.getOperator());

        return baseMapper.myPageQuery(wrapper, input.getPageInfo());
    }
}
