package jp_2026_Manoel_Souza.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("jp-capacitacao-2026")
                .displayName("API Principal")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("JP Capacitação 2026 API")
                        .description("API para o projeto de capacitação JP 2026.")
                        .version("v1.0.0")
                        .contact(new Contact().name("Manoel Souza").email("vinesouza9x9@gmail.com").url("https://github.com/Manoel-DJS"))
                        .license(new License().name("MIT License - Open Source").url("https://opensource.org/licenses/MIT")))
                .security(List.of(new SecurityRequirement().addList("Bearer Authentication")));
    }


}