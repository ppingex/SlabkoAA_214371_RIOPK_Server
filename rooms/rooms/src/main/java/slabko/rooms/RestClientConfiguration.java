package slabko.rooms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import slabko.rooms.properties.IntegrationProperties;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient bookingsService(IntegrationProperties integrationProperties){
        return RestClient.create(integrationProperties.bookingsService());
    }
}
