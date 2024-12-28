package com.bank.cash_crunch_helper.config.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Cash Crunch Helper")
                        .description("Cash Crunch Helper provides API to manage loans and interest rates. " +
                                "It stands out as an application where you can easily set interest rates,  " +
                                "create and list loans,  " +
                                "manage customers and help them to pay loans and installments. " +
                                "API accesses are secured with JWT.")
                        .contact(new Contact().email("melihdumanli@hotmail.com"))
                        .license(new License().name("Source Code").url("https://github.com/melihdumanli/cash-crunch-helper"))
                        .version("1.0.0")
                );
    }
}