package com.xxx.myspringboot.service.impl.log;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.xxx.myspringboot.common.PageResult;
import com.xxx.myspringboot.dto.event.DataChangeEvent;
import com.xxx.myspringboot.dto.input.log.LogActionPageInput;
import com.xxx.myspringboot.entity.log.LogAction;
import com.xxx.myspringboot.mapper.log.LogActionMapper;
import com.xxx.myspringboot.service.log.ILogActionService;
import com.xxx.myspringboot.util.DiffUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 操作日志 服务实现类
 */
@Service
public class LogActionServiceImpl extends ServiceImpl<LogActionMapper, LogAction> implements ILogActionService {
    /**
     * 获取分页列表
     *
     * @param input input
     * @return 分页对象
     */
    public PageResult<LogAction> getPageList(LogActionPageInput input) {
        var wrapper = new LambdaQueryWrapper<LogAction>();
        wrapper.eq(input.getType() != null && input.getType() > 0, LogAction::getType, input.getType());
        wrapper.eq(input.getClientType() != null && input.getClientType() > 0, LogAction::getClientType, input.getClientType());
        wrapper.like(StringUtils.isNotBlank(input.getOperator()), LogAction::getOperator, input.getOperator());

        return baseMapper.myPageQuery(wrapper, input.getPageInfo());
    }

    /**
     * 保存变更日志
     *
     * @param dto dto
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveChangeLog(DataChangeEvent dto) {
        LogAction entity = new LogAction();
        entity.setClientType(dto.getClientType());
        entity.setType(dto.getChangeType().ordinal());
        entity.setOperator(dto.getOperator());
        entity.setClassName(dto.getEntityClassName());
        entity.setLocal(dto.getLocal());

        StringBuilder sb = new StringBuilder();
        sb.append("旧数据：").append(dto.getOldDataJson());
        sb.append("，新数据：").append(dto.getNewDataJson());
        // 更新才做diff
        if (dto.getChangeType() == SqlCommandType.UPDATE) {
            Map<String, Object> fieldChanges = DiffUtil.diff(
                    dto.getOldDataJson(),
                    dto.getNewDataJson()
            );
            sb.append("，变更字段：").append(JSONUtil.toJsonStr(fieldChanges));
        }

        entity.setContent(sb.toString());
        baseMapper.insert(entity);
    }

}
