package com.company.common.persistence;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.company.common"})
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class}
)
public class CommonJPAConfig {

}
