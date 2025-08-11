package team2.kakigowherebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team2.kakigowherebackend.interceptors.LoggingInterceptor;
import team2.kakigowherebackend.interceptors.PlaceVisitInterceptor;
import team2.kakigowherebackend.interceptors.VisitInterceptor;

@Component
public class WebAppConfig implements WebMvcConfigurer {
    private final LoggingInterceptor loggingInterceptor;
    private final VisitInterceptor visitInterceptor;
    private final PlaceVisitInterceptor placeVisitInterceptor;

    @Value("${cors.allowed.origin}")
    private String allowedOrigin;

    public WebAppConfig(
            LoggingInterceptor loggingInterceptor,
            VisitInterceptor visitInterceptor,
            PlaceVisitInterceptor placeVisitInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
        this.visitInterceptor = visitInterceptor;
        this.placeVisitInterceptor = placeVisitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitInterceptor);
        // restrict placeVisitInterceptor to api/places/id/{placeId} path
        registry.addInterceptor(placeVisitInterceptor).addPathPatterns("/api/places/id/**");
        registry.addInterceptor(loggingInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true);
    }
}
