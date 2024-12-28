package com.github.nazdov.slideshow.image.core.validator;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public interface ImageURLEventValidator<T> extends URLEventValidator<T> {
    String IMAGE_REGEX = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";
    Pattern IMAGE_REGEX_PATTERN = Pattern.compile(IMAGE_REGEX);


    default boolean isValidImage(String url) {
        try {
            return IMAGE_REGEX_PATTERN
                    .matcher(getImageFilePath(url))
                    .matches();
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * Will strip the url of any query params or args returning only file name and extension
     * or will throw exception otherwise
     * e.g from https://public/images/someName.jpg?token=x to someName.jpg
     *
     * @param url of potential image
     * @return clean image filePath with name + extension
     * @throws URISyntaxException
     */
    static String getImageFilePath(String url) throws URISyntaxException {
        return Paths
                .get(new URI(url).getPath())
                .getFileName()
                .toString();
    }
}
