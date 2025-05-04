package slabko.clients;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repo;

    public List<ClientEntity> findAll() {
        return repo.findAll();
    }

    public ClientEntity findById(UUID id) {
        return repo.findById(id).orElse(null);
    }

    public ClientEntity findByPassportNum(String passportNum){
        return repo.findByPassportNum(passportNum).orElse(null);
    }

    @Transactional
    public ClientEntity add(ClientCreateDTO createDTO){
        ClientEntity client = new ClientEntity();
        client.setFirstName(createDTO.getFirstName());
        client.setSecondName(createDTO.getSecondName());
        client.setPatronymic(createDTO.getPatronymic());
        client.setPhoneNumber(createDTO.getPhoneNumber());
        client.setPassportNum(createDTO.getPassportNum());
        return repo.save(client);
    }

    @Transactional
    public void delete(UUID id) {
        repo.deleteById(id);
    }

    @Transactional
    public ClientEntity update(UUID id, ClientUpdateDTO updateDTO) {
        ClientEntity client = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        if (updateDTO.getFirstName() != null) {
            client.setFirstName(updateDTO.getFirstName());
        }
        if (updateDTO.getSecondName() != null) {
            client.setSecondName(updateDTO.getSecondName());
        }
        if (updateDTO.getPatronymic() != null) {
            client.setPatronymic(updateDTO.getPatronymic());
        }
        if (updateDTO.getPhoneNumber() != null) {
            client.setPhoneNumber(updateDTO.getPhoneNumber());
        }

        return repo.save(client);
    }

}
