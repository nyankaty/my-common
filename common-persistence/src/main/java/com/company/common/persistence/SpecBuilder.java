package com.company.common.persistence;

import org.springframework.data.jpa.domain.Specification;

public interface SpecBuilder {

    static <T> Specification<T> equal(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.equal(root.get(key), value);
    }

    static <T> Specification<T> notEqual(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.notEqual(root.get(key), value);
    }

    static <T> Specification<T> greaterThan(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.greaterThan(root.get(key), value.toString());
    }

    static <T> Specification<T> greaterThanEqual(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.greaterThanOrEqualTo(root.get(key), value.toString());
    }

    static <T> Specification<T> lessThan(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.lessThanOrEqualTo(root.get(key), value.toString());
    }

    static <T> Specification<T> lessThanEqual(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.lessThanOrEqualTo(root.get(key), value.toString());
    }

    static <T> Specification<T> match(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.like(builder.lower(root.get(key)), "%" + value.toString().toLowerCase() + "%");
    }

    static <T> Specification<T> matchStart(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.like(builder.lower(root.get(key)), "%" + value.toString().toLowerCase());
    }

    static <T> Specification<T> matchEnd(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.like(builder.lower(root.get(key)), value.toString().toLowerCase() + "%");
    }

    static <T> Specification<T> in(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.in(root.get(key)).value(value);
    }

    static <T> Specification<T> notIn(String key, Object value) {
        return (root, criteriaQuery, builder) -> builder.not(root.get(key)).in(value);
    }
}

