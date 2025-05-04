package slabko.bookings;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingCreateDTO {

    @NotBlank
    private String clientPassportNum;

    @NotBlank
    private String roomNumber;

    @NotBlank
    private LocalDate checkInDate;

    @NotBlank
    private LocalDate checkOutDate;

}
