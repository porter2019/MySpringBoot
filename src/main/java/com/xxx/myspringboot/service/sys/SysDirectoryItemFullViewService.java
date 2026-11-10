package com.xxx.myspringboot.service.sys;

import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.input.SysDirectory.SysDirectoryItemFullViewPageInput;
import com.xxx.myspringboot.entity.sys.SysDirectoryItemFullView;

/**
 * 字典完整信息 服务类
 */
public interface SysDirectoryItemFullViewService extends IService<SysDirectoryItemFullView> {

    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    PageResult<SysDirectoryItemFullView> getPageList(SysDirectoryItemFullViewPageInput input);
}
