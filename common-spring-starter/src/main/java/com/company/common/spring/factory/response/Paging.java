package com.company.common.spring.factory.response;

import java.util.List;

public class Paging<T> {

    private List<T> pageData;
    private long total;

    public Paging(List<T> pageData, long total) {
        this.pageData = pageData;
        this.total = total;
    }

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
