package com.xxx.myspringboot.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.PageOptions;
import com.xxx.myspringboot.util.PageUtil;

/**
 * 通用分页服务接口
 * @param <T>
 */
public interface IPageQueryService<T> {
    BaseMapper<T> getBaseMapper();

    /**
     * 默认实现
     * @param queryWrapper 查询wrapper
     * @param pageOptions 分页条件信息
     * @return 分页对象T
     */
    default PageResult<T> myPageQuery(Wrapper<T> queryWrapper, PageOptions pageOptions) {
        Page<T> page = PageUtil.buildPage(pageOptions);
        Page<T> result= getBaseMapper().selectPage(page, queryWrapper);
        return PageUtil.toResult(result);
    }
}
