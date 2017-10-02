package com.csipon.crm.dto.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karpunets
 * @since 21.05.2017
 */
public class ModelMapper {

    private ModelMapper() {
    }

    public static <F, T> T map(Mapper<F, T> mapper, F from, Class<T> toClass) {
        try {
            T to = toClass.newInstance();
            mapper.configure(from, to);
            return to;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <F, T> List<T> mapList(Mapper<F, T> mapper, List<F> from, Class<T> toClass) {
        List<T> result = new ArrayList<>();
        for (F f : from) {
            result.add(map(mapper, f, toClass));
        }
        return result;
    }
}
