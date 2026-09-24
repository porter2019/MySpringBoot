package com.xxx.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 后台用户完整信息
 */
@Setter
@Getter
@ToString
@TableName("sys_user_om_view")
public class SysUserOMView extends SysUser {

    private static final long serialVersionUID = 1L;

    /**
     * 该用户所拥有的组ids
     */
    @TableField("role_ids")
    private String roleIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long[] getRoleIdArray() {
        if (StringUtils.isBlank(roleIds)) return new long[0];
        return Arrays.stream(roleIds.split(",")).filter(StringUtils::isNotBlank).mapToLong(Long::parseLong).toArray();
    }

    /**
     * 该用户所拥有的组names
     */
    @TableField("role_names")
    private String roleNames;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String[] getRoleNames() {
        if (StringUtils.isBlank(roleNames)) return new String[0];
        return Arrays.stream(roleNames.split(",")).filter(StringUtils::isNotBlank).toArray(String[]::new);
    }

}
