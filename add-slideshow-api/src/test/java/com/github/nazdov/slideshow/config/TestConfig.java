package com.github.nazdov.slideshow.config;

import com.github.nazdov.slideshow.config.AddSlideshowConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {AddSlideshowConfig.class})
public class TestConfig {


}
