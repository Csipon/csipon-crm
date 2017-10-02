package com.csipon.crm.dao;

/**
 * Created by Pasha on 28.04.2017.
 */
public interface CrudDao<T> {

    Long create(T object);

    Long update(T object);

    Long delete(Long id);

    Long delete(T object);

    T findById(Long id);
}
