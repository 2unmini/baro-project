package sample.baro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String API_TITLE = "API 문서";
    private static final String API_DESCRIPTION = "바로인턴 과제";
    private static final String API_VERSION = "1.0.0";

    @Bean
    public OpenAPI OpenAPIConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .description(API_DESCRIPTION)
                        .version(API_VERSION));
    }
}
