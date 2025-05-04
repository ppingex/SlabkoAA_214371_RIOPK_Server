package slabko.clients;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
public class ClientController {

    public void setService(ClientService service) {
        this.service = service;
    }

    @Autowired
    private ClientService service;

    @GetMapping()
    public List<ClientEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ClientEntity findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping("/passport/{passportNum}")
    public ClientEntity findByPassportNum(@PathVariable String passportNum) {
        return service.findByPassportNum(passportNum);
    }

    @PostMapping()
    public ClientEntity add( @RequestBody @Valid ClientCreateDTO createDTO) {
        return service.add(createDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientEntity> updateClient(
            @PathVariable UUID id,
            @RequestBody @Valid ClientUpdateDTO updateDTO) {
        ClientEntity updatedClient = service.update(id, updateDTO);
        return ResponseEntity.ok(updatedClient);
    }
}
