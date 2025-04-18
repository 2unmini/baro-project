package sample.baro.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import sample.baro.auth.CustomUserDetails;
import sample.baro.domain.Role;
import sample.baro.domain.User;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }


        try {
            String token = authorization.substring(TOKEN_PREFIX.length());

            if (!jwtUtil.validateToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            log.info("추출한 토큰: {}", token);
            String role = jwtUtil.getRole(token);
            String username = jwtUtil.getUsername(token);
            log.info("토큰에서 추출한 username = {}", username);
            log.info("Role.of(role) 결과 = {}", Role.of(role));

            User user = User.builder()
                    .username(username)
                    .role(Role.of(role))
                    .build();

            CustomUserDetails customUser = new CustomUserDetails(user);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        } catch (Exception e) {
            log.info("에러발생");
        }

        filterChain.doFilter(request, response);
    }

}

