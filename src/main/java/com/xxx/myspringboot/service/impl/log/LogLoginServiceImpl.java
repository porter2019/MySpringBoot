package com.xxx.myspringboot.service.impl.log;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.log.LogLoginPageInput;
import com.xxx.myspringboot.entity.log.LogLogin;
import com.xxx.myspringboot.mapper.log.LogLoginMapper;
import com.xxx.myspringboot.service.log.ILogLoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 登录日志 服务实现类
 */
@Service
public class LogLoginServiceImpl extends ServiceImpl<LogLoginMapper, LogLogin> implements ILogLoginService {
    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    public PageResult<LogLogin> getPageList(LogLoginPageInput input) {
        var wrapper = new LambdaQueryWrapper<LogLogin>();
        wrapper.eq(input.getClientType() != null && input.getClientType() > 0, LogLogin::getClientType, input.getClientType());
        wrapper.like(StringUtils.isNotBlank(input.getUserName()), LogLogin::getUserName, input.getUserName());
        wrapper.like(StringUtils.isNotBlank(input.getCellPhone()), LogLogin::getCellPhone, input.getCellPhone());

        return baseMapper.myPageQuery(wrapper, input.getPageInfo());
    }
}
