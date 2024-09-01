package be.jimsa.iotproject.config.document;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Component;

@Component
@OpenAPIDefinition(
        info = @Info(
                title = "Project microservice REST API documentation",
                description = "Project microservice, based on Restful APIs as a Cloud-Native-Service",
                version = "v0.0.9",
                contact = @Contact(
                        name = "Iman Salehi",
                        url = "https://www.linkedin.com/in/jimsa/",
                        email = "cse.isalehi@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Source code repository",
                url = "https://github.com/j-imsa/Microservices-Tutorial"
        ),
        servers = {
                @Server(
                        description = "Dev, local",
                        url = "http://localhost:8088/"
                ),
                @Server(
                        description = "Dev, Server",
                        url = "http://152.11.42.185:8090/"
                )
        }
)
public class AppDocument {
}
