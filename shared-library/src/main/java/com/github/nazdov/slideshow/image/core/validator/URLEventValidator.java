package com.github.nazdov.slideshow.image.core.validator;

import java.util.regex.Pattern;

public interface URLEventValidator<T> extends EventValidator<T>{
    String URL_REGEX = "((http|https)://)(www.)?"
            + "[a-zA-Z0-9@:%._\\+~#?&//=]"
            + "{2,256}\\.[a-z]"
            + "{2,6}\\b([-a-zA-Z0-9@:%"
            + "._\\+~#?&//=]*)";

    Pattern URL_REGEX_PATTERN = Pattern.compile(URL_REGEX);

    default boolean isValidUrl(String url) {
        return URL_REGEX_PATTERN
                .matcher(url)
                .matches();
    }
}
