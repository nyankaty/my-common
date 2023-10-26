package com.company.common.spring.config;

import com.company.common.spring.config.properties.InfoConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(InfoConfigurationProperties.class)
public class MainApplicationConfig {

    private static final Logger log = LoggerFactory.getLogger(MainApplicationConfig.class);

    private final Environment env;
    private final InfoConfigurationProperties infoConfigurationProperties;

    public MainApplicationConfig(Environment env, InfoConfigurationProperties infoConfigurationProperties) {
        this.env = env;
        this.infoConfigurationProperties = infoConfigurationProperties;
    }

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

    public String getProperty(String name) {
        return this.env.getProperty(name);
    }

    public void logApplicationStartup() {
        log.debug("\n----------------------------------------------------------\n\t" +
                        "Project build info:\n\t" +
                        "Name: \t\t{}\n\t" +
                        "Version: \t{}\n\t" +
                        "Artifact: \t{}\n\t" +
                        "Description: \t{}" +
                        "\n----------------------------------------------------------",
                infoConfigurationProperties.getName(),
                infoConfigurationProperties.getVersion(),
                infoConfigurationProperties.getArtifact(),
                infoConfigurationProperties.getDescription());

        String protocol = Optional.ofNullable(this.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String serverPort = this.getProperty("server.port");
        String contextPath = Optional
                .ofNullable(this.getProperty("apiUrl"))
                .filter(StringUtils::isNotBlank)
                .orElse("/");
        String hostAddress = "localhost";

        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.debug("The host name could not be determined, using `localhost` as fallback");
        }

        log.debug("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}" +
                "\n----------------------------------------------------------",
                this.getProperty("application-short-name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles());
    }
}
