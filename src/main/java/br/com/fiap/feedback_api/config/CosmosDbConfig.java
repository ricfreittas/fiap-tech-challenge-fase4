package br.com.fiap.feedback_api.config;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.ConsistencyLevel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosmosDbConfig {

    @Bean
    public CosmosClient cosmosClient() {
        return new CosmosClientBuilder()
                .endpoint(System.getenv("COSMOS_ENDPOINT"))
                .key(System.getenv("COSMOS_KEY"))
                .gatewayMode()
                .consistencyLevel(ConsistencyLevel.SESSION)
                .buildClient();
    }
}