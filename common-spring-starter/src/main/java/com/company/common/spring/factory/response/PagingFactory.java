package com.company.common.spring.factory.response;

import com.company.common.spring.factory.request.PagingRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PagingFactory {

    public Pageable createPageable(PagingRequest request) {
        if (StringUtils.isNotEmpty(request.getSortBy())) {
            Sort sort = Sort.by(request.getSortBy());
            sort = "asc".equalsIgnoreCase(request.getSortOrder()) ? sort.ascending() : sort.descending();
            return PageRequest.of(request.getPageIndex() - 1, request.getPageSize(), sort);
        } else {
            return PageRequest.of(request.getPageIndex() - 1, request.getPageSize());
        }
    }

}
