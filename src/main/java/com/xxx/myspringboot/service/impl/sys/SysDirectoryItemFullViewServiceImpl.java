package com.xxx.myspringboot.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysDirectory.SysDirectoryItemFullViewPageInput;
import com.xxx.myspringboot.entity.sys.SysDirectoryItemFullView;
import com.xxx.myspringboot.entity.sys.SysPost;
import com.xxx.myspringboot.mapper.sys.SysDirectoryItemFullViewMapper;
import com.xxx.myspringboot.service.sys.SysDirectoryItemFullViewService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 字典项完整信息 服务实现类
 */
@Service
public class SysDirectoryItemFullViewServiceImpl extends ServiceImpl<SysDirectoryItemFullViewMapper, SysDirectoryItemFullView> implements SysDirectoryItemFullViewService {

    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    public PageResult<SysDirectoryItemFullView> getPageList(SysDirectoryItemFullViewPageInput input) {
        var wrapper = new LambdaQueryWrapper<SysDirectoryItemFullView>();
        wrapper.eq(input.getCategoryId() != null, SysDirectoryItemFullView::getCategoryId, input.getCategoryId());
        wrapper.like(StringUtils.isNotBlank(input.getName()), SysDirectoryItemFullView::getName, input.getName());

        return baseMapper.myPageQuery(wrapper, input.getPageInfo());
    }
}
