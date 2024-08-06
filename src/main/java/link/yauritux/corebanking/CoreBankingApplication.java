package link.yauritux.corebanking;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        servers = {
                @Server(
                        url = "http://localhost:9000",
                        description = "CoreBanking API v1.0"
                )
        },
        info = @Info(
                title = "CoreBanking API Specs",
                version = "1.0",
                description = "Core Banking API Documentations build version 1.0",
                contact = @Contact(name = "M. Yauri M. Attamimi", email = "yauritux@gmail.com")))
public class CoreBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreBankingApplication.class, args);
    }

}
