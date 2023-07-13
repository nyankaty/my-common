package com.company.common.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
@SuppressWarnings("java:S119")
public interface BaseJPARepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}
