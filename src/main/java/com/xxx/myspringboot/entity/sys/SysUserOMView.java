package com.xxx.myspringboot.entity.sys;

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


    /**
     * 该用户所拥有的岗位ids
     */
    @TableField("post_ids")
    private String postIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public long[] getPostIdArray() {
        if (StringUtils.isBlank(postIds)) return new long[0];
        return Arrays.stream(postIds.split(",")).filter(StringUtils::isNotBlank).mapToLong(Long::parseLong).toArray();
    }

    /**
     * 该用户所拥有的岗位names
     */
    @TableField("post_names")
    private String postNames;
}
