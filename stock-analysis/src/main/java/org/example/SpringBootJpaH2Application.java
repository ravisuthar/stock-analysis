package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * H2 console : http://localhost:8080/h2-ui/
 * Username =sa password=
 * <p>
 * swagger: http://localhost:8080/swagger-ui/index.html
 */
@SpringBootApplication

public class SpringBootJpaH2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaH2Application.class, args);
    }
}
