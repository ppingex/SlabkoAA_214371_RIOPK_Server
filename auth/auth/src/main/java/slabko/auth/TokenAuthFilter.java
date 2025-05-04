package slabko.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/")) {
            filterChain.doFilter(request, response);
            return;
        }

//        String path = request.getRequestURI();
//        if (path.startsWith("/v3/api-docs") ||
//                path.startsWith("/swagger-ui") ||
//                path.startsWith("/swagger-resources") ||
//                path.startsWith("/webjars")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = request.getHeader("X-Auth-Token");
//        if (token == null) {
//            response.sendError(401, "Токен отсутствует");
//            return;
//        }
//
//        User user = userRepo.findByToken(token)
//                .orElseThrow(() -> new RuntimeException("Неверный токен"));
//
//        // Создаем аутентификацию
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                user.getUsername(),
//                null,
//                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())) // ROLE_ADMIN или ROLE_GUEST
//        );
//        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
