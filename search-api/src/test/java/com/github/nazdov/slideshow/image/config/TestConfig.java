package com.github.nazdov.slideshow.image.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {SearchConfig.class})
public class TestConfig { }
