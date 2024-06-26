package com.artistry.artistry.config;

import com.artistry.artistry.auth.AuthInterceptor;
import com.artistry.artistry.auth.MemberArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberArgumentResolver memberArgumentResolver;
    private final AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry){
        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/api/**")
//                .addPathPatterns("/api/members/me")
//                .addPathPatterns("/api/applications/**")
                .excludePathPatterns("/api/images/**")
                .excludePathPatterns("/api/auth/**")
                .excludePathPatterns("/api/members/{id:[0-9]\\d*}")
                .excludePathPatterns("/api/members")
                .excludePathPatterns("/api/members/nickname")
                .excludePathPatterns("/api/tags/**")
                .excludePathPatterns("/api/roles/**")
                .excludePathPatterns("/api/portfolios/**")
                .excludePathPatterns("/api/teams/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(memberArgumentResolver);
    }
}
