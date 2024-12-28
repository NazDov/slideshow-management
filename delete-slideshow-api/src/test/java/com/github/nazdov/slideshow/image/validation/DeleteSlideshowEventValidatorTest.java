package com.github.nazdov.slideshow.image.validation;

import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.validation.DeleteSlideshowEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class DeleteSlideshowEventValidatorTest {

    private DeleteSlideshowEventValidator validator;

    @BeforeEach
    public void init() {
        validator = new DeleteSlideshowEventValidator();
    }

    @Test
    public void testValidate_whenMissingPayload_throwValidationException() {

        assertThrows(ValidationException.class,
                () -> validator.validate((Long) null),
                "Delete slideshow id is missing");
    }

    @Test
    public void testValidate_whenValidPayload_doNothing() {

        assertDoesNotThrow(
                () -> validator.validate(1L)
        );
    }
}
