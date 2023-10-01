package com.company.common.persistence;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

public abstract class BaseJPAAdapter<E, ID, R extends BaseJPARepository<E, ID>> {

    @Autowired
    protected R repository;

    protected BaseJPAAdapter() {

    }

    public List<E> findAll() {
        return this.repository.findAll();
    }

    public List<E> findAll(Specification<E> specification) {
        return this.repository.findAll(specification);
    }

    public Page<E> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    public List<E> findAll(Sort sort) {
        return this.repository.findAll(sort);
    }

    public E get(ID id) {
        return this.repository.findById(id).orElse(null);
    }

    public E save(E entity) {
        return this.repository.save(entity);
    }

    public List<E> saveAll(List<E> entity) {
        return this.repository.saveAll(entity);
    }

    public E saveAndFlush(E entity) {
        return this.repository.saveAndFlush(entity);
    }

    public List<E> saveAllAndFlush(List<E> entity) {
        List<E> temp = this.repository.saveAll(entity);
        this.repository.flush();
        return temp;
    }

    public E update(E entity) {
        return this.repository.save(entity);
    }

    public void delete(ID id) {
        this.repository.deleteById(id);
    }
}
