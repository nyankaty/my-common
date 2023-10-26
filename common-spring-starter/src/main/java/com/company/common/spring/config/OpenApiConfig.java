package com.company.common.spring.config;

import com.company.common.spring.config.properties.SwaggerConfigurationProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SwaggerConfigurationProperties.class)
public class OpenApiConfig {

    private final SwaggerConfigurationProperties swaggerConfigurationProperties;

    public OpenApiConfig(SwaggerConfigurationProperties swaggerConfigurationProperties) {
        this.swaggerConfigurationProperties = swaggerConfigurationProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return (new OpenAPI()).info((new Info())
                .title(this.swaggerConfigurationProperties.getTitle())
                .description(this.swaggerConfigurationProperties.getDescription())
                .contact((new Contact())
                        .name(this.swaggerConfigurationProperties.getContactName())
                        .url(this.swaggerConfigurationProperties.getContactUrl()))
                .version(this.swaggerConfigurationProperties.getVersion())
        );
    }

    @Configuration
    @ConditionalOnProperty(
            value = {"swagger.auth.enabled"},
            havingValue = "true"
    )
    @ConfigurationProperties(
            prefix = "swagger.auth"
    )
    public static class ConfigAuthOpenApi {
        private final OpenAPI openAPI;
        private boolean enabled;
        private String name;
        private Type type;
        private String bearerFormat;
        private String scheme;
        private String description;
        private In in;

        public ConfigAuthOpenApi(OpenAPI openAPI) {
            this.type = Type.HTTP;
            this.bearerFormat = "JWT";
            this.scheme = "bearer";
            this.description = "Header Authorization : token ";
            this.in = In.HEADER;
            this.openAPI = openAPI;
        }

        @Bean
        public void addSecurity() {
            SecurityScheme securityScheme = new SecurityScheme();
            securityScheme.setName(this.name);
            securityScheme.setType(Type.HTTP);
            securityScheme.setBearerFormat(this.bearerFormat);
            securityScheme.setScheme(this.scheme);
            securityScheme.setDescription(this.description);
            securityScheme.setIn(this.in);
            this.openAPI.schemaRequirement(this.name, securityScheme);
        }

        public OpenAPI getOpenAPI() {
            return this.openAPI;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public String getName() {
            return this.name;
        }

        public Type getType() {
            return this.type;
        }

        public String getBearerFormat() {
            return this.bearerFormat;
        }

        public String getScheme() {
            return this.scheme;
        }

        public String getDescription() {
            return this.description;
        }

        public In getIn() {
            return this.in;
        }

        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public void setType(final Type type) {
            this.type = type;
        }

        public void setBearerFormat(final String bearerFormat) {
            this.bearerFormat = bearerFormat;
        }

        public void setScheme(final String scheme) {
            this.scheme = scheme;
        }

        public void setDescription(final String description) {
            this.description = description;
        }

        public void setIn(final In in) {
            this.in = in;
        }

        public String toString() {
            return "OpenApiConfig.ConfigAuthOpenApi(openAPI=" + this.getOpenAPI() + ", enabled=" + this.isEnabled() + ", name=" + this.getName() + ", type=" + this.getType() + ", bearerFormat=" + this.getBearerFormat() + ", scheme=" + this.getScheme() + ", description=" + this.getDescription() + ", in=" + this.getIn() + ")";
        }
    }

}

