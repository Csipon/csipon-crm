package com.csipon.crm.scheduler.cacher;

/**
 * Created by Pasha on 13.05.2017.
 */
public abstract class Cache<T> {
    public abstract void fillCache();

    public abstract void cleanCache();
}
