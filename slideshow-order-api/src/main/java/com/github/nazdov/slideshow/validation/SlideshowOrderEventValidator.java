package com.github.nazdov.slideshow.validation;

import com.github.nazdov.slideshow.dto.SlideshowOrder;
import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.core.validator.EventValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import reactor.util.function.Tuple2;

import static com.github.nazdov.slideshow.image.core.validator.EventValidator.expect;

@Slf4j
public class SlideshowOrderEventValidator implements EventValidator<String> {


    @Override
    public void validate(String event) {
        throw new UnsupportedOperationException("validate(String event) is not supported");
    }

    public void validate(Tuple2<String, String> request) {
        expect(ObjectUtils.isNotEmpty(request.getT1()),
                new ValidationException("variable 'id' is missing or invalid"));
        expect(ObjectUtils.isNotEmpty(request.getT2()),
                new ValidationException("variable 'order' is missing or invalid"));
        expect(validateOrder(request.getT2()),
                new ValidationException("invalid or missing order parameter. supported: ASC, DESC"));
    }

    public boolean validateOrder(String order) {
        try {
            SlideshowOrder.valueOf(order.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
