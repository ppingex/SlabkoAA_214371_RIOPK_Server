package slabko.clients;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков
        ClientController controller = new ClientController();
        controller.setService(clientService); // Устанавливаем мок сервиса
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void findById_ShouldReturn200_WhenClientExists() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        ClientEntity client = new ClientEntity();
        client.setId(id);

        when(clientService.findById(id)).thenReturn(client);

        // Act & Assert
        mockMvc.perform(get("/clients/{id}", id))
                .andExpect(status().isOk());
    }
}
