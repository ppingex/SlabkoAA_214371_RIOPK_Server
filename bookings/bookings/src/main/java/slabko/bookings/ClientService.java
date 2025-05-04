package slabko.bookings;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Service
public class ClientService {

    private final RestClient restClient;

    public ClientService(@Qualifier("clientRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public ClientDTO getClientById(UUID clientId) {
        return restClient.get()
                .uri("/clients/{id}", clientId)
                .retrieve()
                .body(ClientDTO.class);
    }

    public ClientDTO findByPassportNum(String passportNum) {
        return restClient.get()
                .uri("/clients/passport/{passportNum}", passportNum)
                .retrieve()
                .body(ClientDTO.class);
    }
}
