package com.xxx.myspringboot.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysPost.SysPostPageInput;
import com.xxx.myspringboot.entity.sys.SysPost;
import com.xxx.myspringboot.entity.sys.SysPostUser;
import com.xxx.myspringboot.entity.sys.SysRolePermit;
import com.xxx.myspringboot.entity.sys.SysRoleUser;
import com.xxx.myspringboot.mapper.sys.SysPostMapper;
import com.xxx.myspringboot.mapper.sys.SysPostUserMapper;
import com.xxx.myspringboot.service.sys.ISysPostService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 岗位 服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysPostServiceImpl extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostService {

    private final SysPostUserMapper sysPostUserMapper;

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


    /**
     * 批量删除
     *
     * @param idList id列表
     * @return 是否成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Set<Long> idList) {
        removeByIds(idList);

        //删除用户
        var deleteWrapper = new LambdaQueryWrapper<SysPostUser>();
        deleteWrapper.in(SysPostUser::getPostId, idList);
        sysPostUserMapper.delete(deleteWrapper);

        return true;
    }
}
