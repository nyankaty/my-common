package com.company.common.spring.factory.request;

import javax.validation.constraints.Min;

public class PagingRequest {

    public static final String ASC_SYMBOL = "asc";
    public static final String DESC_SYMBOL = "desc";

    @Min(value = 1, message = "Page index must be greater than 0")
    private int pageIndex = 1;

    @Min(value = 1, message = "Page size must be greater than 0")
    private int pageSize = 1;

    private String sortBy;

    private String sortOrder;

    public PagingRequest() {

    }

    public PagingRequest(int pageIndex, int pageSize, String sortBy, String sortOrder) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
