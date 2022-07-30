package com.oowanghan.atlantis.framework.web.vo;

import java.util.List;

public class PageResult<T> {

    private List<T> data;

    private long total;

    private PageResult(List<T> data, long total) {
        this.data = data;
        this.total = total;
    }

    public static <T> PageResult<T> instance(List<T> data, long total) {
        return new PageResult<>(data, total);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}