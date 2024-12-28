package com.github.nazdov.slideshow.image.config;

import com.github.nazdov.slideshow.image.config.DeleteImageConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {DeleteImageConfig.class})
public class TestConfig {


}
