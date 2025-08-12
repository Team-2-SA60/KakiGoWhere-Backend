package team2.kakigowherebackend.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import team2.kakigowherebackend.service.StatService;

@Slf4j
@Component
public class PlaceVisitInterceptor implements HandlerInterceptor {
    private static final String ATTR_PLACE_ID = "__placeId";
    private static final String SESSION_VISITED_PLACES = "visitedPlaces";
    private static final String SESSION_VISITED_PLACES_DATE = "visitedPlacesDate";

    private final StatService statService;

    public PlaceVisitInterceptor(StatService statService) {
        this.statService = statService;
    }

    // extract placeId from the GET request
    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if ("GET".equalsIgnoreCase(request.getMethod())) {
            @SuppressWarnings("unchecked")
            Map<String, String> pathVars =
                    (Map<String, String>)
                            request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

            if (pathVars != null) {
                // specifically look for {placeId} in /api/places/id/{placeId}
                String idStr = pathVars.get("placeId");
                if (idStr != null) {
                    try {
                        Long placeId = Long.valueOf(idStr);
                        request.setAttribute(ATTR_PLACE_ID, placeId);
                    } catch (NumberFormatException ignored) {
                        // skip counting if non-numeric
                    }
                }
            }
        }
        return true;
    }

    // counts once per place per session (per day, in case session left overnight)
    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        Object placeIdObj = request.getAttribute(ATTR_PLACE_ID);
        if (placeIdObj == null) return;
        int status = response.getStatus();
        if (status < 200 || status >= 300) return;

        Long placeId = (Long) placeIdObj;
        HttpSession session = request.getSession(true);

        LocalDate today = LocalDate.now();
        LocalDate lastDate = (LocalDate) session.getAttribute(SESSION_VISITED_PLACES_DATE);
        if (lastDate == null || !today.equals(lastDate)) {
            session.setAttribute(SESSION_VISITED_PLACES_DATE, today);
            session.setAttribute(SESSION_VISITED_PLACES, new HashSet<Long>());
        }

        @SuppressWarnings("unchecked")
        Set<Long> visited = (Set<Long>) session.getAttribute(SESSION_VISITED_PLACES);
        if (visited == null) {
            visited = new HashSet<>();
            session.setAttribute(SESSION_VISITED_PLACES, visited);
        }

        if (!visited.contains(placeId)) {
            statService.addPlaceVisit(placeId);
            visited.add(placeId);
            session.setAttribute(SESSION_VISITED_PLACES, visited);
            log.info("Logged place page visit: {}", placeId);
        }
    }
}
