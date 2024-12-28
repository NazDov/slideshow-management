package com.github.nazdov.slideshow.image.validation;

import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.dto.AddImageRequest;
import com.github.nazdov.slideshow.image.validation.AddImageEventValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class AddImageEventValidatorTest {

    private AddImageEventValidator validator;

    @BeforeEach
    public void init() {
        validator = new AddImageEventValidator();
    }

    @Test
    public void testValidate_whenMissingPayload_throwValidationException() {
        AddImageRequest noData = null;
        assertThrows(ValidationException.class,
                () -> validator.validate(noData),
                "AddImageRequest is missing or invalid");
    }


    @Test
    public void testValidate_whenMissingUrl_throwValidationException() {

        assertThrows(ValidationException.class,
                () -> validator.validate(AddImageRequest.builder()
                        .title("some title")
                        .description("some description")
                        .duration(5L)
                        .build()),
                "URL is missing");
    }

    @Test
    public void testValidate_whenMissingDuration_throwValidationException() {

        assertThrows(ValidationException.class,
                () -> validator.validate(AddImageRequest.builder()
                        .url("https://i.imgur.com/CzXTtJV.jpg")
                        .title("some title")
                        .description("some description")
                        .build()),
                "Image request duration field is invalid");
    }


    @Test
    public void testValidate_whenInvalidUrl_throwValidationException() {

        assertThrows(ValidationException.class,
                () -> validator.validate(AddImageRequest.builder()
                        //missing protocol
                        .url("example.com")
                        .title("some title")
                        .description("some description")
                        .duration(5L)
                        .build()),
                "URL is invalid");

        assertThrows(ValidationException.class,
                () -> validator.validate(AddImageRequest.builder()
                        //bad protocol
                        .url("ht:/example.com")
                        .title("some title")
                        .description("some description")
                        .duration(5L)
                        .build()
                ),
                "URL is invalid");

        assertThrows(ValidationException.class,
                () -> validator.validate(AddImageRequest.builder()
                        //invalid domain
                        .url("http://example")
                        .title("some title")
                        .description("some description")
                        .duration(5L)
                        .build()
                ),
                "URL is invalid");
    }

    @Test
    public void testValidate_whenInvalidImageUrl_throwValidationException() {

        assertThrows(ValidationException.class,
                () -> validator.validate(AddImageRequest.builder()
                        .url("https://i.imgur.com/CzXTtJV.docx")
                        .title("some title")
                        .description("some description")
                        .duration(5L)
                        .build()),
                "Image request contains invalid image");
    }

    @Test
    public void testValidate_whenValidPayload_doNothing() {

        assertDoesNotThrow(
                () -> validator.validate(AddImageRequest.builder()
                        .url("https://i.imgur.com/CzXTtJV.png")
                        .title("some title")
                        .description("some description")
                        .duration(5L)
                        .build())
        );
    }
}
