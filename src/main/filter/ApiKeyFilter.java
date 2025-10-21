//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class ApiKeyFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private ApiKeyConfig apiKeyConfig;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String requestApiKey = request.getHeader("X-API-KEY");
//
//        if (apiKeyConfig.getApiKey().equals(requestApiKey)) {
//            filterChain.doFilter(request, response); // proceed if valid
//        } else {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Invalid API Key");
//            return;
//        }
//    }
//}