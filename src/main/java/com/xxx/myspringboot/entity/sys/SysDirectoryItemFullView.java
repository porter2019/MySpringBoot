package com.xxx.myspringboot.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 字典项完整信息
 */
@Setter
@Getter
@ToString
@TableName("sys_directory_item_full_view")
public class SysDirectoryItemFullView extends SysDirectoryItem {

    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类编码
     */
    private String categoryCode;
}
