package slabko.bookings;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RestClient restClient;

    public RoomService(@Qualifier("roomRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public RoomDTO getRoomById(UUID roomId) {
        return restClient.get()
                .uri("/rooms/{id}", roomId)
                .retrieve()
                .body(RoomDTO.class);
    }

    public RoomDTO findByNumber(String number) {
        return restClient.get()
                .uri("/rooms/num/{number}", number)
                .retrieve()
                .body(RoomDTO.class);
    }

    public List<RoomDTO> findRoomsByBedCount(int bedCount) {
        return restClient.get()
                .uri("/rooms/beds/{beds}", bedCount)
                .retrieve()
                .body(new ParameterizedTypeReference<List<RoomDTO>>() {});
    }
}
