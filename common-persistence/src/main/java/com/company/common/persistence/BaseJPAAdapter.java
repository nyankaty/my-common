package com.company.common.persistence;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@SuppressWarnings("java:S119")
public abstract class BaseJPAAdapter<T, ID, R extends BaseJPARepository<T, ID>> {

    @Autowired
    protected R repository;

    protected BaseJPAAdapter() {

    }

    public List<T> findAll() {
        return this.repository.findAll();
    }

    public List<T> findAll(Specification<T> specification) {
        return this.repository.findAll(specification);
    }

    public Page<T> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    public List<T> findAll(Sort sort) {
        return this.repository.findAll(sort);
    }

    public T get(ID id) {
        return this.repository.findById(id).orElse(null);
    }

    public T save(T entity) {
        return this.repository.save(entity);
    }

    public List<T> saveAll(List<T> entity) {
        return this.repository.saveAll(entity);
    }

    public T saveAndFlush(T entity) {
        return this.repository.saveAndFlush(entity);
    }

    public List<T> saveAllAndFlush(List<T> entity) {
        List<T> temp = this.repository.saveAll(entity);
        this.repository.flush();
        return temp;
    }

    public T update(T entity) {
        return this.repository.save(entity);
    }

    public void delete(ID id) {
        this.repository.deleteById(id);
    }
}
