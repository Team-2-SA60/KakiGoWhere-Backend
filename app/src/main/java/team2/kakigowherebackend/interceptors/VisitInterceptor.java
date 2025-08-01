package team2.kakigowherebackend.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import team2.kakigowherebackend.service.StatService;

@Slf4j
@Component
public class VisitInterceptor implements HandlerInterceptor {
    private final StatService statService;

    public VisitInterceptor(StatService statService) {
        this.statService = statService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // To check if user visit is unique
        HttpSession session = request.getSession(true);

        if (session.getAttribute("visited") == null) {
            LocalDate date = statService.addVisit();
            log.info("New visitor logged into {}", date);
            session.setAttribute("visited", true);
        }
        return true;
    }
}
