package com.github.nazdov.slideshow.validation;

import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class SlideshowOrderValidationTest {

    private SlideshowOrderEventValidator validator;

    @BeforeEach
    public void init() {
        validator = new SlideshowOrderEventValidator();
    }

    @Test
    public void testValidate_whenMissingIdVariable_throwValidationException() {

        Tuple2<String, String> payload = Tuples.of("", "asc");
        assertThrows(ValidationException.class,
                () -> validator.validate(payload),
                "variable 'id' is missing or invalid");
    }

    @Test
    public void testValidate_whenMissingOrderParam_throwValidationException() {

        Tuple2<String, String> payload = Tuples.of("1", "");
        assertThrows(ValidationException.class,
                () -> validator.validate(payload),
                "variable 'order' is missing or invalid");
    }

    @Test
    public void testValidate_whenInvalidOrderParam_throwValidationException() {

        Tuple2<String, String> payload = Tuples.of("1", "has to be either 'asc' or 'desc'");
        assertThrows(ValidationException.class,
                () -> validator.validate(payload),
                "invalid or missing order parameter. supported: ASC, DESC");
    }

}
