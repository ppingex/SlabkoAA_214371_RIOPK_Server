package slabko.bookings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import slabko.bookings.properties.IntegrationProperties;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingsController {

    @Autowired
    private BookingService service;

    @GetMapping()
    public List<BookingDTO> getAll() {
        service.updateBookingStatuses();
        return service.getAll();
    }

    @GetMapping("/{id}")
    public BookingEntity findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping("/current-month")
    public List<BookingDTO> findAllByCurrMonth() {
        service.updateBookingStatuses();
        return service.findAllByCurrMonth();
    }

    @GetMapping("/client/{passportNum}")
    public List<BookingDTO> findByClientPassportNum(@PathVariable String passportNum) {
        service.updateBookingStatuses();
        return service.findByClientPassportNum(passportNum);
    }

    @GetMapping("/room/{roomNum}")
    public List<BookingDTO> findByRoomNum(@PathVariable String roomNum) {
        service.updateBookingStatuses();
        return service.findByRoomNum(roomNum);
    }

    @PostMapping("/")
    public ResponseEntity<BookingDTO> addBooking(@RequestBody BookingCreateDTO bookingDTO) {
        BookingDTO createdBooking = service.addBooking(bookingDTO);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable UUID bookingId) {
        service.deleteBooking(bookingId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingDTO> cancelBooking(@PathVariable UUID bookingId) {
        BookingDTO canceledBooking = service.cancelBooking(bookingId);
        return ResponseEntity.ok(canceledBooking);
    }

    @GetMapping("/current")
    public ResponseEntity<List<BookingDTO>> getCurrentBookings() {
        service.updateBookingStatuses();
        List<BookingDTO> currentBookings = service.findCurrentBookings();
        return ResponseEntity.ok(currentBookings);
    }

    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomDTO>> findAvailableRooms(
            @RequestParam int bedCount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut) {

        List<RoomDTO> availableRooms = service.findAvailableRooms(bedCount, checkIn, checkOut);
        return ResponseEntity.ok(availableRooms);
    }

}
