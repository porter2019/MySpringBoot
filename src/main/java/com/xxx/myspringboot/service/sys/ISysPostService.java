package com.xxx.myspringboot.service.sys;

import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysPost.SysPostPageInput;
import com.xxx.myspringboot.entity.sys.SysPost;
import com.baomidou.mybatisplus.spring.service.IService;

import java.util.Set;

/**
 * 岗位 服务类
 */
public interface ISysPostService extends IService<SysPost> {

    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    PageResult<SysPost> getPageList(SysPostPageInput input);

    /**
     * 批量删除
     *
     * @param idList id列表
     * @return 是否成功
     */
    Boolean delete(Set<Long> idList);
}
