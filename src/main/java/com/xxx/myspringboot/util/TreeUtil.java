package com.xxx.myspringboot.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import java.util.*;

/**
 * 树形结构工具类，用于构建树形数据结构
 */
public class TreeUtil {

    /**
     * 构建树形结构
     *
     * @param dataList    原始数据列表
     * @param getId       获取节点ID的函数
     * @param getParentId 获取父节点ID的函数
     * @param setParent   设置父节点的方法
     * @param setChildren 设置子节点列表的方法
     * @param comparator  排序 升序：Comparator.comparing(Tree::getFullOrderNo, Comparator.nullsLast(String::compareTo))
     * @param <T>         节点类型
     * @param <ID>        ID类型
     * @return 树形结构的根节点列表
     */
    public static <T, ID> List<T> buildTree(
            List<T> dataList,
            Function<T, ID> getId,
            Function<T, ID> getParentId,
            BiConsumer<T, T> setParent,
            BiConsumer<T, List<T>> setChildren,
            Comparator<T> comparator) {

        // 如果数据列表为空，返回空列表
        if (CollUtil.isEmpty(dataList)) {
            return Collections.emptyList();
        }

        // 将数据列表转换为以ID为键的Map，便于查找
        Map<ID, T> nodeMap = dataList.stream()
                .collect(Collectors.toMap(getId, Function.identity(), (a, b) -> a));

        // 用于存储根节点的列表
        List<T> roots = new ArrayList<>();

        // 遍历所有节点，构建树形结构
        for (T node : dataList) {
            // 获取当前节点的父ID
            ID parentId = getParentId.apply(node);

            // 如果父ID为空或不存在于nodeMap中，则该节点为根节点
            if (parentId == null || !nodeMap.containsKey(parentId)) {
                roots.add(node);
            } else {
                // 获取父节点
                T parent = nodeMap.get(parentId);

                // 只填充上一级基本信息
                T simpleParent = createSimpleParent(parent, setParent, setChildren);
                setParent.accept(node, simpleParent);

                // 挂到真实父节点
                List<T> children = ensureChildren(parent, setChildren);
                children.add(node);
            }
        }
        sortTree(roots, comparator);
        return roots;
    }

    /**
     * 带条件查询：自动补齐匹配节点的所有祖先，保证树结构完整
     *
     * @param allData     所有数据
     * @param matchedIds  匹配的节点 id
     * @param getId       Tree::getId
     * @param getParentId Tree::getParentId
     * @param setParent   Tree::setParent
     * @param setChildren Tree::setChilds
     * @param comparator  排序 降序：Comparator.comparing(Tree::getFullOrderNo, Comparator.nullsLast(String::compareTo))
     *           .reversed()
     * @param <T>         实体类型
     * @param <ID>        id 类型
     * @return 构建好的树形结构列表
     */
    public static <T, ID> List<T> buildTreeWithCondition(
            List<T> allData,
            Set<ID> matchedIds,
            Function<T, ID> getId,
            Function<T, ID> getParentId,
            BiConsumer<T, T> setParent,
            BiConsumer<T, List<T>> setChildren,
            Comparator<T> comparator) {

        // 如果数据列表或匹配ID集合为空，返回空列表
        if (CollUtil.isEmpty(allData) || CollUtil.isEmpty(matchedIds)) {
            return Collections.emptyList();
        }

        Map<ID, T> nodeMap = allData.stream()
                .collect(Collectors.toMap(getId, Function.identity(), (a, b) -> a));

        Set<ID> keepIds = new HashSet<>(matchedIds);
        // 将所有数据转换为以ID为键的Map，便于查找
        for (ID id : matchedIds) {
            T node = nodeMap.get(id);
            while (node != null) {
                // 用于存储需要保留的ID集合（包括匹配ID及其所有祖先ID）
                ID pid = getParentId.apply(node);
                // 遍历所有匹配ID，添加其所有祖先ID到keepIds中
                if (pid == null || !keepIds.add(pid)) {
                    break;
                }
                node = nodeMap.get(pid);
            }
        }

        List<T> filtered = allData.stream()
                .filter(n -> keepIds.contains(getId.apply(n)))
                .collect(Collectors.toList());

        // 过滤出需要保留的节点
        return buildTree(filtered, getId, getParentId, setParent, setChildren, comparator);
    }

    @SuppressWarnings("unchecked")
    private static <T> T createSimpleParent(T parent,
                                            BiConsumer<T, T> setParent,
                                            BiConsumer<T, List<T>> setChildren) {
        try {
            T simple = (T) parent.getClass().getDeclaredConstructor().newInstance();
            BeanUtil.copyProperties(parent, simple);
            setParent.accept(simple, null);
            setChildren.accept(simple, null);
            // 创建父节点的新实例
            return simple;
            // 复制属性
        } catch (Exception e) {
            // 清空父节点和子节点引用
            setParent.accept(parent, null);
            setChildren.accept(parent, null);
            return parent;
        }
        // 如果创建失败，直接使用原始节点并清空引用
    }

    private static <T> List<T> ensureChildren(T node, BiConsumer<T, List<T>> setChildren) {
        // 通过反射获取已有 children，没有则新建
        try {
            Field field = findChildrenField(node.getClass());
            if (field != null) {
                field.setAccessible(true);
                List<T> children = (List<T>) field.get(node);
                if (children == null) {
                    children = new ArrayList<>();
                    field.set(node, children);
                }
                return children;
            }
        } catch (Exception ignored) {
        }
        List<T> children = new ArrayList<>();
        setChildren.accept(node, children);
        return children;
    }

    private static Field findChildrenField(Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (List.class.isAssignableFrom(field.getType())
                    && (field.getName().equals("childs") || field.getName().equals("children"))) {
                return field;
            }
        }
        return null;
    }

    private static <T> void sortTree(List<T> nodes, Comparator<T> comparator) {
        if (CollUtil.isEmpty(nodes) || comparator == null) {
            return;
        }
        nodes.sort(comparator);
        for (T node : nodes) {
            // 通过反射拿 children 再递归排序
            try {
                Field field = findChildrenField(node.getClass());
                if (field != null) {
                    field.setAccessible(true);
                    List<T> children = (List<T>) field.get(node);
                    if (CollUtil.isNotEmpty(children)) {
                        sortTree(children, comparator);
                    }
                }
            } catch (Exception ignored) {
            }
        }
    }
}
