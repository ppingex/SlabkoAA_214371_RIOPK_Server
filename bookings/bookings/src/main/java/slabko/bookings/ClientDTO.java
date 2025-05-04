package slabko.bookings;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    @NotNull
    private UUID id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String secondName;

    private String patronymic;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String passportNum;
}
