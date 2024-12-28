package com.github.nazdov.slideshow.validation;

import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.core.validator.EventValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import reactor.util.function.Tuple2;

import static com.github.nazdov.slideshow.image.core.validator.EventValidator.expect;

@Slf4j
public class ProofOfPlayEventValidator implements EventValidator<Long> {


    @Override
    public void validate(Long event) {
        throw new UnsupportedOperationException("validate(String event) is not supported");
    }

    public void validate(Tuple2<Long, Long> request) {
        expect(ObjectUtils.isNotEmpty(request.getT1()),
                new ValidationException("variable 'slideshowId' is missing or invalid"));
        expect(ObjectUtils.isNotEmpty(request.getT2()),
                new ValidationException("variable 'imageId' is missing or invalid"));
    }

}
