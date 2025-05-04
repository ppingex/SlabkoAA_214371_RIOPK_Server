package slabko.rooms.properties;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("integration")
@Validated
public record IntegrationProperties(@NotBlank String bookingsService) {
}
