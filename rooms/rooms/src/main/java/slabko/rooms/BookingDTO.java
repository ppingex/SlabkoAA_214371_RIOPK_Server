package slabko.rooms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class BookingDTO {

    @NotNull
    private UUID id;

    @NotBlank
    private String clientFirstName;

    @NotBlank
    private String clientLastName;

    @NotBlank
    private String clientPatronymic;

    @NotBlank
    private String clientPhone;

    @NotBlank
    private String roomNumber;

    @NotBlank
    private String roomType;

    @NotBlank
    private LocalDate checkInDate;

    @NotBlank
    private LocalDate checkOutDate;

    @NotBlank
    private String status;

}
