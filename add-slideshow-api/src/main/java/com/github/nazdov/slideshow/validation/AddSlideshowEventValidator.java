package com.github.nazdov.slideshow.validation;

import com.github.nazdov.slideshow.dto.AddSlideshowRequest;
import com.github.nazdov.slideshow.image.core.exception.ValidationException;
import com.github.nazdov.slideshow.image.core.validator.ImageURLEventValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;
import java.util.Optional;

import static com.github.nazdov.slideshow.image.core.validator.EventValidator.expect;

@Slf4j
public class AddSlideshowEventValidator implements ImageURLEventValidator<AddSlideshowRequest> {

    @Override
    public void validate(AddSlideshowRequest addSlideshowRequest) {
        expect(ObjectUtils.isNotEmpty(addSlideshowRequest),
                new ValidationException("AddSlideshowRequest is missing or invalid"));
        expect(ObjectUtils.isNotEmpty(addSlideshowRequest.getImageIds())
                        || ObjectUtils.isNotEmpty(addSlideshowRequest.getImageUrls()),
                new ValidationException("imageUrls or imageIds are missing"));


        Optional.ofNullable(addSlideshowRequest.getImageUrls())
                .stream()
                .filter(ObjectUtils::isNotEmpty)
                .flatMap(Collection::stream)
                .forEach(imageUrl -> {
                    expect(isValidUrl(imageUrl),
                            new ValidationException("URL is invalid"));
                    expect(isValidImage(imageUrl),
                            new ValidationException("Image request contains invalid image"));
                });

    }

}
