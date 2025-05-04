package slabko.clients;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository repo;

    @InjectMocks
    private ClientService service;

    @Test
    void findById_ShouldReturnClient_WhenExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        ClientEntity client = new ClientEntity();
        client.setId(id);
        when(repo.findById(id)).thenReturn(Optional.of(client));

        // Act
        ClientEntity result = service.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void add_ShouldSaveClientWithCorrectData() {
        // Arrange
        ClientCreateDTO dto = new ClientCreateDTO();
        dto.setFirstName("John");
        dto.setSecondName("Doe");
        dto.setPassportNum("1234567890");

        // Act
        service.add(dto);

        // Assert
        ArgumentCaptor<ClientEntity> captor = ArgumentCaptor.forClass(ClientEntity.class);
        verify(repo).save(captor.capture());
        ClientEntity savedClient = captor.getValue();
        assertEquals("John", savedClient.getFirstName());
        assertEquals("Doe", savedClient.getSecondName());
        assertEquals("1234567890", savedClient.getPassportNum());
    }
}
