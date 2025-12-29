// package com.example.demo.config;

// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.Components;
// import io.swagger.v3.oas.models.servers.Server;
// import io.swagger.v3.oas.models.security.SecurityScheme;
// import io.swagger.v3.oas.models.security.SecurityRequirement;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import java.util.List;

// @Configuration
// public class OpenApiConfig {

//     @Bean
//     public OpenAPI customOpenAPI() {

//         // JWT Security Scheme
//         SecurityScheme securityScheme = new SecurityScheme()
//                 .name("Authorization")
//                 .type(SecurityScheme.Type.HTTP)
//                 .scheme("bearer")
//                 .bearerFormat("JWT")
//                 .in(SecurityScheme.In.HEADER);

//         // Apply JWT globally
//         SecurityRequirement securityRequirement = new SecurityRequirement()
//                 .addList("Bearer Authentication");

//         return new OpenAPI()
//                 // Server with port number
//                 .servers(List.of(
//                         new Server().url("https://9152.pro604cr.amypo.ai/")
//                 ))
//                 // Swagger Authorize button
//                 .components(new Components()
//                         .addSecuritySchemes("Bearer Authentication", securityScheme)
//                 )
//                 .addSecurityItem(securityRequirement);
//     }
// }





// package com.example.demo.config;

// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.servers.Server;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import java.util.List;

// @Configuration
// public class SwaggerConfig {

//     @Bean
//     public OpenAPI customOpenAPI() {
//         return new OpenAPI()
//                 // You need to change the port as per your server
//                 .servers(List.of(
//                         new Server().url("https://9152.pro604cr.amypo.ai/")
//                 ));
//         }
// }



package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(new Info()
                        .title("My API")
                        .version("1.0")
                        .description("Authentication and Token Log API Documentation"))
                .servers(List.of(
                        new Server().url("https://9152.pro604cr.amypo.ai/")
                ))
                // 1. Add Global Security Requirement
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                // 2. Define the Security Scheme (JWT Bearer)
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
