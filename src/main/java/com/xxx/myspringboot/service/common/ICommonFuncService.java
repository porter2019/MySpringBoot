package com.xxx.myspringboot.service.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.dto.output.AutoCompleteOutput;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 通用功能服务接口
 * 该接口提供了生成业务编号的通用方法，包括普通版本和并发安全版本
 */
public interface ICommonFuncService {
    /**
     * 生成当日流水号
     *
     * @param prefix      前缀，如 "GT" + yyyyMMdd
     * @param seqLen      流水号位数
     * @param queryLatest 查当日最大编号的函数式接口，不接收参数，返回String(当前数据库最新的流水号)
     * @return 生成的流水号字符串
     */
    String generateNextCode(String prefix, int seqLen, Supplier<String> queryLatest);

    /**
     * 生成当日流水号，并发安全版本
     * 在多线程环境下也能安全生成流水号的方法
     *
     * @param bizKey      业务标识，用于区分不同业务的流水号生成
     * @param prefix      前缀，如 "GT" + yyyyMMdd
     * @param seqLen      流水号位数
     * @param queryLatest 查当日最大编号的函数式接口，不接收参数，返回String(当前数据库最新的流水号)
     * @return 生成的流水号字符串
     */
    String generateNextCodeSafe(String bizKey, String prefix, int seqLen, Supplier<String> queryLatest);


    /**
     * 通用自动补全查询
     *
     * @param service    实体对应的 IService（如 sysPostService）
     * @param wrapper    wrapper
     * @param idGetter   实体的 ID 取值函数，如 SysPost::getId
     * @param nameGetter 实体的名称取值函数，如 SysPost::getName
     * @param <T>        实体类型
     * @return List<AutoCompleteOutput>
     */
    <T> List<AutoCompleteOutput> autoSearchField(IService<T> service, LambdaQueryWrapper<T> wrapper, Function<T, Long> idGetter, Function<T, String> nameGetter);
}
