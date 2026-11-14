package com.xxx.myspringboot.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysUser.SysUserPageInput;
import com.xxx.myspringboot.entity.sys.SysUserOMView;
import com.xxx.myspringboot.mapper.sys.SysUserOMViewMapper;
import com.xxx.myspringboot.service.sys.ISysUserOMViewService;
import com.xxx.myspringboot.util.MyStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 系统用户运营视图 实现类
 */
@Service
public class SysUserOMViewServiceImpl extends ServiceImpl<SysUserOMViewMapper, SysUserOMView> implements ISysUserOMViewService {

    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    public PageResult<SysUserOMView> getPageList(SysUserPageInput input) {
        var wrapper = new LambdaQueryWrapper<SysUserOMView>();
        wrapper.eq(input.getOrgId() != null && input.getOrgId() > 0, SysUserOMView::getOrgId, input.getOrgId());
        wrapper.like(StringUtils.isNotBlank(input.getOrgName()), SysUserOMView::getOrgName, input.getOrgName());
        wrapper.like(StringUtils.isNotBlank(input.getCellPhone()), SysUserOMView::getCellPhone, input.getCellPhone());
        wrapper.like(StringUtils.isNotBlank(input.getUserName()), SysUserOMView::getUserName, input.getUserName());
        wrapper.like(StringUtils.isNotBlank(input.getPostNames()), SysUserOMView::getPostNames, input.getPostNames());
        wrapper.like(StringUtils.isNotBlank(input.getRoleNames()), SysUserOMView::getRoleNames, input.getRoleNames());
        var dateArr = MyStringUtil.splitWithSemicolonToSqlDateArray(input.getCreatedTime());
        if (dateArr.length == 2) {
            wrapper.ge(SysUserOMView::getCreatedTime, dateArr[0]);// 大于等于
            wrapper.lt(SysUserOMView::getCreatedTime, dateArr[1]);// 小于
        }

        return baseMapper.myPageQuery(wrapper, input.getPageInfo());
    }
}
