package slabko.rooms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository repo;

    @Mock
    private RestClient bookingsService;

    @InjectMocks
    private RoomService service;

    @Test
    void findByNumber_ShouldReturnRoom_WhenExists() {
        // Arrange
        String number = "101";
        RoomEntity room = new RoomEntity();
        room.setNumber(number);
        when(repo.findByNumber(number)).thenReturn(Optional.of(room));

        // Act
        RoomEntity result = service.findByNumber(number);

        // Assert
        assertNotNull(result);
        assertEquals(number, result.getNumber());
    }

    @Test
    void add_ShouldSetDefaultStatusAsFree() {
        // Arrange
        RoomCreateDTO dto = new RoomCreateDTO();
        dto.setNumber("101");

        // Act
        service.add(dto);

        // Assert
        ArgumentCaptor<RoomEntity> captor = ArgumentCaptor.forClass(RoomEntity.class);
        verify(repo).save(captor.capture());
        assertEquals(RoomStatus.FREE, captor.getValue().getRoomStatus());
    }
}
