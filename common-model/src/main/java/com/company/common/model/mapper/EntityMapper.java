package com.company.common.model.mapper;

import java.util.List;

public interface EntityMapper<E, T> {

    T toObject(E entity);

    List<T> toObject(List<E> listEntities);

    E toEntity(T object);

    List<E> toEntity(List<T> listObject);
}
