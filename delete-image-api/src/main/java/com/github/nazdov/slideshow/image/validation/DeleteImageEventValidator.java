package com.github.nazdov.slideshow.image.validation;

import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.core.validator.EventValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import static com.github.nazdov.slideshow.image.core.validator.EventValidator.expect;

@Slf4j
public class DeleteImageEventValidator implements EventValidator<Long> {

    @Override
    public void validate(Long id) {
        expect(ObjectUtils.isNotEmpty(id),
                new ValidationException("Delete request data is missing or invalid"));
    }
}
