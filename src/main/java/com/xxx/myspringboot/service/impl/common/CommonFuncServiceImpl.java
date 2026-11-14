package com.xxx.myspringboot.service.impl.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.IService;
import com.xxx.myspringboot.dto.output.AutoCompleteOutput;
import com.xxx.myspringboot.service.common.ICommonFuncService;
import com.xxx.myspringboot.util.DailyCodeGeneratorUtil;
import com.xxx.myspringboot.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonFuncServiceImpl implements ICommonFuncService {

    private final RedisUtil redisUtil;

    /**
     * 生成当日流水号
     *
     * @param prefix      前缀，如 "GT" + yyyyMMdd
     * @param seqLen      流水号位数
     * @param queryLatest 查当日最大编号的函数式接口，不接收参数，返回String(当前数据库最新的流水号)
     * @return 生成的流水号字符串
     */
    @Override
    public String generateNextCode(String prefix, int seqLen, Supplier<String> queryLatest) {
        String lastCode = queryLatest.get();
        String seq = DailyCodeGeneratorUtil.nextSeq(lastCode, prefix.length(), seqLen);
        return prefix + seq;
    }

    /**
     * 生成当日流水号，并发安全版本
     * 在多线程环境下也能安全生成流水号的方法
     *
     * @param bizKey      业务标识，用于区分不同业务的流水号生成
     * @param prefix      前缀，如 "GT" + yyyyMMdd
     * @param seqLen      流水号位数
     * @param queryLatest 查查当日最大编号的函数式接口，不接收参数，返回String(当前数据库最新的流水号)
     * @return 生成的流水号字符串
     */
    @Override
    public String generateNextCodeSafe(String bizKey, String prefix, int seqLen, Supplier<String> queryLatest) {
        String lockKey = "code:lock:" + bizKey + ":" + prefix;

        String token = null;
        try {
            token = redisUtil.lock(lockKey, Duration.ofSeconds(10), Duration.ofSeconds(3));

            String lastCode = queryLatest.get();
            String seq = DailyCodeGeneratorUtil.nextSeq(lastCode, prefix.length(), seqLen);
            return prefix + seq;

        } catch (IllegalStateException e) {
            throw new RuntimeException("生成编码失败，请稍后重试", e);
        } finally {
            if (token != null) {
                try {
                    redisUtil.unlock(lockKey, token);
                } catch (Exception e) {
                    log.warn("释放编码锁失败, lockKey={}", lockKey, e);  // 不覆盖原异常
                }
            }
        }
    }

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
    public <T> List<AutoCompleteOutput> autoSearchField(
            IService<T> service,
            LambdaQueryWrapper<T> wrapper,
            Function<T, Long> idGetter,
            Function<T, String> nameGetter) {

        if (service == null) {
            throw new IllegalArgumentException("service 不能为空");
        }
        if (idGetter == null || nameGetter == null) {
            throw new IllegalArgumentException("idGetter/nameGetter 不能为空");
        }

        List<T> list = service.list(wrapper);
        return list.stream()
                .map(e -> {
                    AutoCompleteOutput output = new AutoCompleteOutput();
                    output.setId(idGetter.apply(e));
                    output.setName(nameGetter.apply(e));
                    return output;
                })
                .toList();
    }
}
