package com.github.nazdov.slideshow.image.validation;

import com.github.nazdov.slideshow.image.core.validator.ImageURLEventValidator;
import com.github.nazdov.slideshow.image.dto.AddImageRequest;
import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import static com.github.nazdov.slideshow.image.core.validator.EventValidator.expect;

@Slf4j
public class AddImageEventValidator implements ImageURLEventValidator<AddImageRequest> {

    @Override
    public void validate(AddImageRequest addImageRequest) {
        expect(ObjectUtils.isNotEmpty(addImageRequest),
                new ValidationException("AddImageRequest is missing or invalid"));
        expect(ObjectUtils.isNotEmpty(addImageRequest.getUrl()),
                new ValidationException("URL is missing"));
        expect(ObjectUtils.isNotEmpty(addImageRequest.getDuration()),
                new ValidationException("Image request duration field is invalid"));
        expect(isValidUrl(addImageRequest.getUrl()),
                new ValidationException("URL is invalid"));
        expect(isValidImage(addImageRequest.getUrl()),
                new ValidationException("Image request contains invalid image"));
    }
}
