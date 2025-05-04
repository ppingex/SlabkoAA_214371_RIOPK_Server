package slabko.bookings.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("integration")
@Validated
public record IntegrationProperties(@NotBlank String clientsService, @NotBlank String roomsService) {


}
