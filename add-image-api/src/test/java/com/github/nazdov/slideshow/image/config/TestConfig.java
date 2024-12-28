package com.github.nazdov.slideshow.image.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {AddImageConfig.class})
public class TestConfig {


}
