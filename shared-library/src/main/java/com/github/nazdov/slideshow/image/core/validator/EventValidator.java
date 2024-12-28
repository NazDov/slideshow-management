package com.github.nazdov.slideshow.image.core.validator;

import reactor.util.function.Tuple2;

public interface EventValidator<T> {
    void validate(T event);

    default void validate(Tuple2<T, T> event) {}

    /**
     * Tests a condition and throws an exception if false.
     *
     * @param condition if false will result in a RuntimeException thrown
     * @param exception of type RuntimeException thrown if condition was false
     */
    static void expect(boolean condition, RuntimeException exception) {
        if (!condition) {
            throw exception;
        }
    }
}
