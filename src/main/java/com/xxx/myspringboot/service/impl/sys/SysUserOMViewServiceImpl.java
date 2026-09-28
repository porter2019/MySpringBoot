package com.xxx.myspringboot.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysUser.SysUserPageInput;
import com.xxx.myspringboot.entity.sys.SysUserOMView;
import com.xxx.myspringboot.mapper.sys.SysUserOMViewMapper;
import com.xxx.myspringboot.service.sys.ISysUserOMViewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 系统用户运营视图 实现类
 */
@Service
public class SysUserOMViewServiceImpl extends ServiceImpl<SysUserOMViewMapper, SysUserOMView> implements ISysUserOMViewService {

    /**
     * 获取分页列表
     * @param input input
     * @return 分页对象
     */
    public PageResult<SysUserOMView> getPageList(SysUserPageInput input) {
        var wrapper = new LambdaQueryWrapper<SysUserOMView>();
        wrapper.like(StringUtils.isNotBlank(input.getCellPhone()),SysUserOMView::getCellPhone, input.getCellPhone());

        return baseMapper.myPageQuery(wrapper,input.getPageInfo());
    }
}
