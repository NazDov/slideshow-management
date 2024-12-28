package com.github.nazdov.slideshow.image.config;

import com.github.nazdov.slideshow.config.DeleteSlideshowConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {DeleteSlideshowConfig.class})
public class TestConfig { }
