package com.xxx.myspringboot.mapper.sys;

import com.xxx.myspringboot.entity.sys.SysOrg;
import com.xxx.myspringboot.mapper.common.MyPageQueryMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 部门 Mapper 接口
 */
public interface SysOrgMapper extends MyPageQueryMapper<SysOrg> {

    /**
     * 调用存储过程
     * 该存储过程用于更新树形结构的层级关系
     * 使用@Select注解来执行SQL语句
     * 花括号{}表示执行存储过程而非SQL查询
     */
    @Select("{call sp_update_sys_org_layer()}")
    // 指定要执行的存储过程名称
    void callUpdateLayer();

    /**
     * 根据 id 递归删除该节点及其所有子孙节点
     *
     * @param id 要删除的节点 id
     * @return 删除的行数
     */
    int deleteWithChildren(@Param("id") Long id);

}
