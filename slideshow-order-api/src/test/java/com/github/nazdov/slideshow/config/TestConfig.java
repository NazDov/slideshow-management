package com.github.nazdov.slideshow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {SlideshowOrderConfig.class})
public class TestConfig { }
