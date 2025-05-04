package slabko.clients;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {

    @Id
    @GeneratedValue
    private UUID id;
    private String firstName;
    private String secondName;
    private String patronymic;
    private String phoneNumber;
    @Column(unique = true)
    private String passportNum;
}
