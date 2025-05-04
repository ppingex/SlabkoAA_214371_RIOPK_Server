package slabko.clients;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClientCreateDTO {

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
