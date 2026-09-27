package com.xxx.myspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxx.myspringboot.entity.Tree;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 树形数据 Mapper 接口
 */
public interface TreeMapper extends BaseMapper<Tree> {

    /**
     * 调用名为sp_update_tree_layer的存储过程
     * 该存储过程用于更新树形结构的层级关系
     * 使用@Select注解来执行SQL语句
     * 花括号{}表示执行存储过程而非SQL查询
     */
    @Select("{call sp_update_tree_layer()}")
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
