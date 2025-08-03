package team2.kakigowherebackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team2.kakigowherebackend.interceptors.LoggingInterceptor;
import team2.kakigowherebackend.interceptors.VisitInterceptor;

@Component
public class WebAppConfig implements WebMvcConfigurer {
    private final LoggingInterceptor loggingInterceptor;
    private final VisitInterceptor visitInterceptor;

    @Value("${cors.allowed.origin}")
    private String allowedOrigin;

    public WebAppConfig(LoggingInterceptor loggingInterceptor, VisitInterceptor visitInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
        this.visitInterceptor = visitInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitInterceptor);
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
