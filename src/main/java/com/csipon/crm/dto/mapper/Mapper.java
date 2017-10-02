package com.csipon.crm.dto.mapper;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
@FunctionalInterface
public interface Mapper<F, T> {
      void configure(F from, T to);
}
