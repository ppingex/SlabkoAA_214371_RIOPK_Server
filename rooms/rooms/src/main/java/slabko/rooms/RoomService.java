package slabko.rooms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoomService {

    @Autowired
    private RoomRepository repo;

    @Autowired
    private RestClient bookingsService;

    @Transactional
    public List<RoomEntity> findAll() {

        log.info("Updating room statuses...");

        List<BookingDTO> currentBookings = getCurrentBookings();
        log.info("Found {} active bookings", currentBookings.size());
        currentBookings.forEach(booking ->
                log.info("Booking ID: {}, Room Number: {}, Dates: {} to {}",
                        booking.getId(),
                        booking.getRoomNumber(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate())
        );
        Set<UUID> occupiedRoomIds = currentBookings.stream()
                .map(booking -> {
                    RoomEntity room = findByNumber(booking.getRoomNumber());
                    if (room == null) {
                        log.warn("Room not found for number: {}", booking.getRoomNumber());
                        return null;
                    }
                    return room.getId();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        log.info("Occupied room IDs: {}", occupiedRoomIds);

        updateRoomStatuses(occupiedRoomIds);

        return repo.findAll();
    }

    public RoomEntity findById(UUID id) {
        return repo.findById(id).orElse(null);
    }

    public RoomEntity findByNumber(String number) {
        return repo.findByNumber(number).orElse(null);
    }

    public List<RoomEntity> findByNumberOfBeds(int beds) {
        return repo.findByNumberOfBeds(beds);
    }

    @Transactional
    public RoomEntity add(RoomCreateDTO createDTO){
        RoomEntity room = new RoomEntity();
        room.setNumber(createDTO.getNumber());
        room.setRoomType(createDTO.getRoomType());
        room.setRoomStatus(RoomStatus.FREE);
        room.setArea(createDTO.getArea());
        room.setNumberOfBeds(createDTO.getNumberOfBeds());
        room.setPrice(createDTO.getPrice());
        room.setAdditionalParameters(createDTO.getAdditionalParameters());
        return repo.save(room);
    }

    @Transactional
    public void delete(UUID id) {
        repo.deleteById(id);
    }

    private List<BookingDTO> getCurrentBookings() {
        try {
            ResponseEntity<List<BookingDTO>> response = bookingsService.get()
                    .uri("/bookings/current")
                    .retrieve()
                    .toEntity(new ParameterizedTypeReference<>() {});

            response.getBody();
            return response.getBody();
        } catch (Exception e) {

            return Collections.emptyList();
        }
    }

    @Transactional
    public void updateRoomStatuses(Set<UUID> occupiedRoomIds) {
        repo.findAllById(occupiedRoomIds).forEach(room -> {
            room.setRoomStatus(RoomStatus.OCCUPIED);
            repo.save(room);
            log.info("Marked room {} as OCCUPIED", room.getId());
        });

        repo.findByIdNotIn(occupiedRoomIds).forEach(room -> {
            room.setRoomStatus(RoomStatus.FREE);
            repo.save(room);
            log.info("Marked room {} as FREE", room.getId());
        });
    }

    @Transactional
    public RoomEntity update(UUID id, RoomUpdateDTO updateDTO) {
        RoomEntity room = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (updateDTO.getRoomType() != null) {
            room.setRoomType(updateDTO.getRoomType());
        }

        if (updateDTO.getNumberOfBeds() != null) {
            room.setNumberOfBeds(updateDTO.getNumberOfBeds());
        }

        if (updateDTO.getArea() != null) {
            room.setArea(updateDTO.getArea());
        }

        if (updateDTO.getPrice() != null) {
            room.setPrice(updateDTO.getPrice());
        }

        if (updateDTO.getAdditionalParameters() != null) {
            room.setAdditionalParameters(updateDTO.getAdditionalParameters());
        }

        return repo.save(room);
    }

}
