package com.xxx.myspringboot.service.sys;

import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.entity.sys.SysDirectoryCategory;
import com.xxx.myspringboot.util.EnumOptionUtil;

import java.util.List;

/**
 * 字典类别 服务类
 */
public interface ISysDirectoryCategoryService extends IService<SysDirectoryCategory> {
    /**
     * 根据搜索文本获取树形结构列表
     *
     * @param searchText 搜索关键字，用于过滤树形结构中的节点
     * @return 返回符合条件的树形结构列表，每个元素都是一个Tree对象
     */
    List<SysDirectoryCategory> getTreeList(String searchText);


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
    Integer add(SysDirectoryCategory tree);

    /**
     * 修改
     *
     * @param tree 要添加的树节点对象
     * @return 返回插入操作的结果
     */
    Integer edit(SysDirectoryCategory tree);

    /**
     * 删除
     *
     * @param id 要删除的树节点ID
     * @return 返回删除操作的结果
     */
    Integer delete(Long id);

    /**
     * 构建枚举列表
     *
     * @param codes 枚举代码数组
     * @return 枚举列表
     */
    List<EnumOptionUtil.EnumGroupVO> buildEnumList(String... codes);
}
