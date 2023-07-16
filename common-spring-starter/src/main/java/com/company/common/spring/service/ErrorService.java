package com.company.common.spring.service;

import org.springframework.stereotype.Service;

@Service
public interface ErrorService {
    String getErrorDetail(String errorCode, String language);
}
