package com.oowanghan.atlantis.framework.web.vo;

import cn.hutool.core.util.StrUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequest<T> {

    private T searchParam;

    private int page = 1;

    private int pageSize = 10;

    private String sortField;

    private String sortType;

    public Pageable makePageable() {
        int page = this.getPage();
        int pageSize = this.getPageSize();

        if (StrUtil.isBlank(this.getSortType()) || StrUtil.isBlank(this.sortField)) {
            return org.springframework.data.domain.PageRequest.of(Math.max(page - 1, 0), pageSize);
        } else {
            Sort sort = Sort.by(Sort.Direction.fromString(this.getSortType()), sortField);
            return org.springframework.data.domain.PageRequest.of(Math.max(page - 1, 0), pageSize, sort);
        }
    }

    public boolean isAsc() {
        if (StrUtil.isNotBlank(this.sortType)) {
            if ("ASC".equals(this.sortType.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    public T getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(T searchParam) {
        this.searchParam = searchParam;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
