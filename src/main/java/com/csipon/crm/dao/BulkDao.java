package com.csipon.crm.dao;

import java.util.Set;

/**
 * Created by Pasha on 28.04.2017.
 */
public interface BulkDao<T> {
    boolean bulkUpdate(Set<Long> ids, T object);
}
