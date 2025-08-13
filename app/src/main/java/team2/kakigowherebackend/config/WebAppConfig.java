package team2.kakigowherebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team2.kakigowherebackend.interceptors.AdminInterceptor;
import team2.kakigowherebackend.interceptors.LoggingInterceptor;
import team2.kakigowherebackend.interceptors.PlaceVisitInterceptor;
import team2.kakigowherebackend.interceptors.VisitInterceptor;

@Component
public class WebAppConfig implements WebMvcConfigurer {
    private final LoggingInterceptor loggingInterceptor;
    private final VisitInterceptor visitInterceptor;
    private final PlaceVisitInterceptor placeVisitInterceptor;
    private final AdminInterceptor adminInterceptor;

    @Value("${cors.allowed.origin}")
    private String[] allowedOrigin;

    public WebAppConfig(
            LoggingInterceptor loggingInterceptor,
            VisitInterceptor visitInterceptor,
            PlaceVisitInterceptor placeVisitInterceptor,
            AdminInterceptor adminInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
        this.visitInterceptor = visitInterceptor;
        this.placeVisitInterceptor = placeVisitInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // don't count visits for admin
        registry.addInterceptor(visitInterceptor)
                .excludePathPatterns("/api/admin/**", "/api/auth/admin/**", "/api/interests");
        // restrict placeVisitInterceptor to api/places/id/{placeId} path
        registry.addInterceptor(placeVisitInterceptor).addPathPatterns("/api/places/id/**");
        // to log all API requests
        registry.addInterceptor(loggingInterceptor);
        // admin session interceptor
        registry.addInterceptor(adminInterceptor).addPathPatterns("/api/admin/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true);
    }
}
