package com.xxx.myspringboot.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.myspringboot.entity.sys.SysRole;
import com.xxx.myspringboot.mapper.common.MyPageQueryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户组 Mapper 接口
 */
public interface SysRoleMapper extends MyPageQueryMapper<SysRole> {


    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRoleListByUserId(@Param("userId") Long userId);
}
