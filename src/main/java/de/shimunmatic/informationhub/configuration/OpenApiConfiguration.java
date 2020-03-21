package de.shimunmatic.informationhub.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers = {
                @Server(description = "Production", url = "https://api.information-hub.shimunmatic.de"),
                @Server(description = "Local", url = "http://localhost:8090")
        }
)
public class OpenApiConfiguration {
}
