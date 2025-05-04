package slabko.clients;

import lombok.Data;

@Data
public class ClientUpdateDTO {
    private String firstName;
    private String secondName;
    private String patronymic;
    private String phoneNumber;
}
