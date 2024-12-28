package com.github.nazdov.slideshow.image.core.mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface DataMapper<T, R> {

    R mapFrom(T type);

    default List<R> mapFrom(List<T> typeList) {
        return typeList
                .stream()
                .map(this::mapFrom)
                .collect(Collectors.toList());
    }
}
