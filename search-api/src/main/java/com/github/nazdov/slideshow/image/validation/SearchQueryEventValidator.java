package com.github.nazdov.slideshow.image.validation;

import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.core.validator.EventValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import static com.github.nazdov.slideshow.image.core.validator.EventValidator.expect;

@Slf4j
public class SearchQueryEventValidator implements EventValidator<String> {

    @Override
    public void validate(String query) {
        expect(ObjectUtils.isNotEmpty(query),
                new ValidationException("query is missing or invalid"));
    }
}
