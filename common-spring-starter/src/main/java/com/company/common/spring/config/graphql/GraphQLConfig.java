package com.company.common.spring.config.graphql;

import com.apollographql.federation.graphqljava.Federation;
import graphql.kickstart.tools.SchemaParser;
import graphql.schema.GraphQLSchema;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
        value = {"graphql.servlet.enabled", "app.log-graphql-enabled"},
        havingValue = "true"
)
public class GraphQLConfig {

    @Bean
    public GraphQLSchema customSchema(SchemaParser schemaParser) {
        return Federation.transform(schemaParser.makeExecutableSchema()).fetchEntities(env -> env.getArgument("representations")).build();
    }
}
