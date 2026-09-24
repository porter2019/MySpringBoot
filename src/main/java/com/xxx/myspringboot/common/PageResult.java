package com.xxx.myspringboot.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@ToString
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 页码 */
    private Integer pageIndex;

    /** 页容量 */
    private Integer pageSize;

    /** 总条数 */
    private Long totalCount;

    /** 总页数 */
    private Long totalPages;

    /** 当前页集合 */
    private List<T> items;

    /** 是否有上一页 */
    private Boolean hasPrevPages;

    /** 是否有下一页 */
    private Boolean hasNextPages;

    public PageResult() {
    }

    public PageResult(Integer pageIndex, Integer pageSize, Long totalCount, List<T> items) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalCount = totalCount == null ? 0L : totalCount;
        this.items = items == null ? Collections.emptyList() : items;
        this.totalPages = (this.pageSize == null || this.pageSize <= 0)
                ? 0L
                : (this.totalCount + this.pageSize - 1) / this.pageSize;
        this.hasPrevPages = this.pageIndex != null && this.pageIndex > 1;
        this.hasNextPages = this.pageIndex != null && this.pageIndex < this.totalPages;
    }
}