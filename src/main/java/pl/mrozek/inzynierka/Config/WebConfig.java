package pl.mrozek.inzynierka.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

//    @Override
//    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer
//                .setUseSuffixPatternMatch(false)
//                .setUseTrailingSlashMatch(false)
//                .setUseRegisteredSuffixPatternMatch(false)
////                .setPathMatcher(antPathMatcher())
////                .setUrlPathHelper(urlPathHelper())
//                .addPathPrefix("/api",
//                        HandlerTypePredicate.forAnnotation(RestController.class));
//    }
}
