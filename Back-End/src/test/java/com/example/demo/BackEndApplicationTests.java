package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ImportAutoConfiguration(exclude = ServletWebSecurityAutoConfiguration.class)
class BackEndApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto arranca sin problemas
    }
}
