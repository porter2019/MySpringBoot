package com.xxx.myspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxx.myspringboot.entity.Tree;
import com.xxx.myspringboot.mapper.TreeMapper;
import com.xxx.myspringboot.service.ITreeService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.util.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 树形数据 服务实现类
 */
@Service
@RequiredArgsConstructor
public class TreeServiceImpl extends ServiceImpl<TreeMapper, Tree> implements ITreeService {

    private final TreeMapper treeMapper;


    /**
     * 根据搜索文本获取树形结构列表
     *
     * @param searchText 搜索关键字，用于筛选树节点名称
     * @return 返回构建好的树形结构列表
     */
    public List<Tree> getTreeList(String searchText) {
        var allData = treeMapper.selectList(null);
        if (StringUtils.isBlank(searchText)) {
            //没有搜索条件，构造所有
            return TreeUtil.buildTree(allData,
                    Tree::getId,
                    Tree::getParentId,
                    Tree::setParent,
                    Tree::setChilds,
                    Comparator.comparing(Tree::getFullOrderNo, Comparator.nullsLast(String::compareTo)));
        } else {
            //搜索的节点会缺少父数据，传入所有数据重新构造完整树
            var queryWrapper = new LambdaQueryWrapper<Tree>();
            queryWrapper.like(Tree::getName, searchText);
            var matchedData = treeMapper.selectList(queryWrapper);
            Set<Long> matchedIds = matchedData.stream().map(Tree::getId).collect(Collectors.toSet());
            return TreeUtil.buildTreeWithCondition(
                    allData,
                    matchedIds,
                    Tree::getId,
                    Tree::getParentId,
                    Tree::setParent,
                    Tree::setChilds,
                    Comparator.comparing(Tree::getFullOrderNo, Comparator.nullsLast(String::compareTo)));
        }
    }

    /**
     * 根据父节点ID获取下一个排序数字
     *
     * @param parentId 父节点ID
     * @return 下一个顺序数字
     */
    public String getNextOrderNo(Long parentId) {
        int length = 3;
        var parentModel = treeMapper.selectById(parentId);
        var parentNo = parentModel == null ? "" : parentModel.getOrderNo();

        var queryMaxWrapper = new LambdaQueryWrapper<Tree>();
        queryMaxWrapper.eq(Tree::getParentId, parentId);
        //取一条
        queryMaxWrapper.last("limit 1");
        queryMaxWrapper.orderBy(true, false, Tree::getOrderNo);
        var nowMaxModel = treeMapper.selectOne(queryMaxWrapper);
        String lastOrderNo = "001";
        if (nowMaxModel != null) {
            int num = NumberUtils.toInt(nowMaxModel.getOrderNo(), 0) + 1;
            String str = String.valueOf(num);
            if (str.length() > length) {
                str = str.substring(str.length() - length);
            }
            lastOrderNo = String.format("%" + length + "s", str).replace(' ', '0');
        }
        return parentNo + lastOrderNo;
    }


    /**
     * 添加
     *
     * @param tree 要添加的树节点对象
     * @return 返回插入操作的结果
     */
    public Integer add(Tree tree) {
        var result = treeMapper.insert(tree); // 将树节点插入数据库
        //执行存储过程以更新树节点的层级信息
        treeMapper.callUpdateLayer();
        return result;
    }

    /**
     * 修改
     *
     * @param tree 要添加的树节点对象
     * @return 返回插入操作的结果
     */
    public Integer edit(Tree tree) {
        var result = treeMapper.updateById(tree);
        //执行存储过程以更新树节点的层级信息
        treeMapper.callUpdateLayer();
        return result;
    }

    /**
     * 删除
     *
     * @param id 要删除的树节点ID
     * @return 返回删除操作的结果
     */
    public Integer delete(Long id) {
        return treeMapper.deleteWithChildren(id);
    }

}
