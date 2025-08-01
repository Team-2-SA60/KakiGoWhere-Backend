package team2.kakigowherebackend.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TouristInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // To check if tourist is logged in
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute("tourist") == null) {
            String message = "Tourist not logged in";
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(message);
            log.info("Outgoing Response: Status = {}, Reason = {}", response.getStatus(), message);
            return false;
        }

        return true;
    }
}
