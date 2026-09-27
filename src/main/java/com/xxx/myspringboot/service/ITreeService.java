package com.xxx.myspringboot.service;

import com.xxx.myspringboot.entity.Tree;
import com.baomidou.mybatisplus.spring.service.IService;

import java.util.List;

/**
 * 树形数据 服务类
 */
public interface ITreeService extends IService<Tree> {

    /**
     * 根据搜索文本获取树形结构列表
     *
     * @param searchText 搜索关键字，用于过滤树形结构中的节点
     * @return 返回符合条件的树形结构列表，每个元素都是一个Tree对象
     */
    List<Tree> getTreeList(String searchText);


    /**
     * 根据父节点ID获取下一个排序数字
     *
     * @param parentId 父节点ID
     * @return 下一个顺序数字
     */
    String getNextOrderNo(Long parentId);

    /**
     * 添加
     *
     * @param tree 要添加的树节点对象
     * @return 返回插入操作的结果
     */
    Integer add(Tree tree);

    /**
     * 修改
     *
     * @param tree 要添加的树节点对象
     * @return 返回插入操作的结果
     */
    Integer edit(Tree tree);

    /**
     * 删除
     *
     * @param id 要删除的树节点ID
     * @return 返回删除操作的结果
     */
    Integer delete(Long id);
}
