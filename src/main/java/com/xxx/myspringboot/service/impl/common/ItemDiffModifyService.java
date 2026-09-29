package com.xxx.myspringboot.service.impl.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * 明细表修改服务
 */
public class ItemDiffModifyService {
    /**
     * 同步主表明细方法
     * 根据前端提交的明细列表，删除不存在的明细，新增或修改存在的明细
     *
     * @param mapper           明细Mapper
     * @param items            明细列表
     * @param masterId         主表Id entity.getId()
     * @param idGetter         明细Id，例如 ContractItem::getId
     * @param foreignKeyGetter 外键，例如 ContractItem::getContractId
     * @param foreignKeySetter 外键Setter，例如 ContractItem::setContractId
     */
    public static <T> void sync(
            BaseMapper<T> mapper,
            List<T> items,
            Long masterId,
            SFunction<T, Long> idGetter,
            SFunction<T, Long> foreignKeyGetter,
            BiConsumer<T, Long> foreignKeySetter
    ) {
        if (masterId == null) {
            throw new IllegalArgumentException("主表Id不能为空");
        }

        if (items == null) {
            items = new ArrayList<>();
        }

        // 前端提交的已有明细Id
        Set<Long> incomingIds = items.stream()
                .map(idGetter)
                .filter(Objects::nonNull)
                .filter(id -> id > 0)
                .collect(Collectors.toSet());

        // 删除不存在的明细
        var deleteWrapper = Wrappers.<T>lambdaQuery()
                .eq(foreignKeyGetter, masterId);

        if (!incomingIds.isEmpty()) {
            deleteWrapper.notIn(idGetter, incomingIds);
        }

        mapper.delete(deleteWrapper);

        // 新增 / 修改
        for (T item : items) {

            Long id = idGetter.apply(item);

            // 自动设置外键
            foreignKeySetter.accept(item, masterId);

            if (id == null || id == 0) {
                // 新增
                mapper.insert(item);
            } else {
                // 修改
                mapper.updateById(item);
            }
        }
    }
}
