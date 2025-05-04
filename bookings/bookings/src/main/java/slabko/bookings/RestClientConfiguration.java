package slabko.bookings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import slabko.bookings.properties.IntegrationProperties;

@Configuration
public class RestClientConfiguration {

    @Bean(name = "clientRestClient")
    public RestClient clientService(IntegrationProperties integrationProperties){
        return RestClient.create(integrationProperties.clientsService());
    }

    @Bean(name = "roomRestClient")
    public RestClient roomService(IntegrationProperties integrationProperties){
        return RestClient.create(integrationProperties.roomsService());
    }
}
