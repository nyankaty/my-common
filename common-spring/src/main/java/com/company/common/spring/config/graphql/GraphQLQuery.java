package com.company.common.spring.config.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class GraphQLQuery {

    @JsonProperty("variables")
    private Map<String, Object> variables;

    @JsonProperty("query")
    private String query;

    public static GraphQLQuery.GraphQLQueryBuilder builder() {
        return new GraphQLQuery.GraphQLQueryBuilder();
    }

    public Map<String, Object> getVariables() {
        return this.variables;
    }

    public String getQuery() {
        return this.query;
    }

    @JsonProperty("variables")
    public void setVariables(final Map<String, Object> variables) {
        this.variables = variables;
    }

    @JsonProperty("query")
    public void setQuery(final String query) {
        this.query = query;
    }

    public String toString() {
        return "GraphQLQuery(variables=" + this.getVariables() + ", query=" + this.getQuery() + ")";
    }

    public GraphQLQuery(final Map<String, Object> variables, final String query) {
        this.variables = variables;
        this.query = query;
    }

    public GraphQLQuery() {
    }

    public static class GraphQLQueryBuilder {
        private Map<String, Object> variables;
        private String query;

        GraphQLQueryBuilder() {
        }

        @JsonProperty("variables")
        public GraphQLQuery.GraphQLQueryBuilder variables(final Map<String, Object> variables) {
            this.variables = variables;
            return this;
        }

        @JsonProperty("query")
        public GraphQLQuery.GraphQLQueryBuilder query(final String query) {
            this.query = query;
            return this;
        }

        public GraphQLQuery build() {
            return new GraphQLQuery(this.variables, this.query);
        }

        public String toString() {
            return "GraphQLQuery.GraphQLQueryBuilder(variables=" + this.variables + ", query=" + this.query + ")";
        }
    }
}

