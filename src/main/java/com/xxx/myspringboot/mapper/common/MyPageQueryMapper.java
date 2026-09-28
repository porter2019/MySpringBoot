package com.xxx.myspringboot.mapper.common;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.PageOptions;
import com.xxx.myspringboot.util.PageUtil;

/**
 * 通用分页查询 Mapper接口
 *
 * @param <T>
 */
public interface MyPageQueryMapper<T> extends BaseMapper<T> {

    /**
     * 自定义分页查询方法
     *
     * @param queryWrapper 查询条件封装对象，用于构建查询条件
     * @param pageOptions  分页参数选项，包含分页大小、当前页码等信息
     * @return PageResult<T> 分页查询结果，包含数据列表、总记录数等分页信息
     */
    default PageResult<T> myPageQuery(Wrapper<T> queryWrapper, PageOptions pageOptions) {
        // 根据分页参数构建分页对象
        Page<T> page = PageUtil.buildPage(pageOptions);
        // 执行分页查询，获取分页结果
        Page<T> result = this.selectPage(page, queryWrapper);
        // 将分页结果转换为统一的分页结果格式并返回
        return PageUtil.toResult(result);
    }
}
