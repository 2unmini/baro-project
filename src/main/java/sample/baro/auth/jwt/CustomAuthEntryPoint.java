package sample.baro.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static sample.baro.exception.ErrorCode.INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(INVALID_TOKEN.getStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", INVALID_TOKEN.name());
        errorDetails.put("message", INVALID_TOKEN.getMessage());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("error", errorDetails);

        String json = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(json);
    }

}
