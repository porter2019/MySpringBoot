package com.xxx.myspringboot.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysPost.SysPostPageInput;
import com.xxx.myspringboot.entity.sys.SysPost;
import com.xxx.myspringboot.mapper.sys.SysPostMapper;
import com.xxx.myspringboot.service.sys.ISysPostService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 岗位 服务实现类
 */
@Service
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {
    
    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    public PageResult<SysPost> getPageList(SysPostPageInput input) {
        var wrapper = new LambdaQueryWrapper<SysPost>();
        wrapper.like(StringUtils.isNotBlank(input.getName()), SysPost::getName, input.getName());

        return baseMapper.myPageQuery(wrapper, input.getPageInfo());
    }
}
