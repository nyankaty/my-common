package com.company.common.persistence.support;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseJPARepository<E, ID> extends JpaRepository<E, ID>, JpaSpecificationExecutor<E> {

}
