package slabko.bookings;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingService {

    private final BookingRepository repo;
    private final ClientService clientService;
    private final RoomService roomService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, ClientService clientService, RoomService roomService) {
        this.repo = bookingRepository;
        this.clientService = clientService;
        this.roomService = roomService;
    }

    public List<BookingDTO> getAll() {
        List<BookingEntity> bookings = repo.findAll();
        return bookings.stream().map(this::convertToDTO).toList();
    }

    private BookingDTO convertToDTO(BookingEntity booking) {
        ClientDTO client = clientService.getClientById(booking.getClientId());
        RoomDTO room = roomService.getRoomById(booking.getRoomId());

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setClientFirstName(client.getFirstName());
        bookingDTO.setClientLastName(client.getSecondName());
        bookingDTO.setClientPatronymic(client.getPatronymic());
        bookingDTO.setClientPhone(client.getPhoneNumber());
        bookingDTO.setRoomNumber(room.getNumber());
        bookingDTO.setRoomType(room.getRoomType().name());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setStatus(booking.getStatus().name());

        return bookingDTO;
    }

    public List<BookingDTO> findAllByCurrMonth() {
        LocalDate now = LocalDate.now();
        List<BookingEntity> bookings = repo.findAllByCheckInDateBetween(
                now.withDayOfMonth(1), now.withDayOfMonth(now.lengthOfMonth()));
        return bookings.stream().map(this::convertToDTO).toList();
    }

    public List<BookingDTO> findByClientPassportNum(String passportNum) {
        ClientDTO client = clientService.findByPassportNum(passportNum);

        if (client == null) {
            return List.of();
        }

        List<BookingEntity> bookings = repo.findByClientId(client.getId());
        return bookings.stream().map(this::convertToDTO).toList();
    }

    public List<BookingDTO> findByRoomNum(String roomNum) {
        RoomDTO room = roomService.findByNumber(roomNum);

        if (room == null) {
            return List.of();
        }

        List<BookingEntity> bookings = repo.findByRoomId(room.getId());
        return bookings.stream().map(this::convertToDTO).toList();
    }

    @Transactional
    public BookingDTO addBooking(BookingCreateDTO bookingCreateDTO) {
        if (bookingCreateDTO.getCheckInDate().isAfter(bookingCreateDTO.getCheckOutDate())) {
            log.info("Дата выезда должна быть после даты заезда");
            return null;
            //throw new IllegalArgumentException("Дата выезда должна быть после даты заезда");
        }
        ClientDTO client = clientService.findByPassportNum(bookingCreateDTO.getClientPassportNum());
        RoomDTO room = roomService.findByNumber(bookingCreateDTO.getRoomNumber());

        List<BookingEntity> conflictingBookings = repo.findConflictingBookings(
                room.getId(),
                bookingCreateDTO.getCheckInDate(),
                bookingCreateDTO.getCheckOutDate()
        );
        if (!conflictingBookings.isEmpty()) {
            log.info("Dates are conflicting");
            return null;
        }

        BookingEntity booking = new BookingEntity();
        booking.setClientId(client.getId());
        booking.setRoomId(room.getId());
        booking.setCheckInDate(bookingCreateDTO.getCheckInDate());
        booking.setCheckOutDate(bookingCreateDTO.getCheckOutDate());
        booking.setStatus(BookingStatus.PENDING);

        BookingEntity savedBooking = repo.save(booking);
        return convertToDTO(savedBooking);
    }

    @Transactional
    public void deleteBooking(UUID bookingId) {
        repo.deleteById(bookingId);
    }

    @Transactional
    public BookingDTO cancelBooking(UUID bookingId) {
        BookingEntity booking = repo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        BookingEntity updatedBooking = repo.save(booking);
        return convertToDTO(updatedBooking);
    }

    public List<BookingDTO> findCurrentBookings() {
        LocalDate today = LocalDate.now();
        List<BookingEntity> bookingEntities = repo.findCurrentBookings(today);
        return convertToDTO(bookingEntities);
    }

    private List<BookingDTO> convertToDTO(List<BookingEntity> bookingEntities) {
        return bookingEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public boolean isRoomAvailable(UUID roomId, LocalDate checkIn, LocalDate checkOut) {
        List<BookingEntity> conflictingBookings = repo.findConflictingBookings(
                roomId,
                checkIn,
                checkOut
        );
        return conflictingBookings.isEmpty();
    }

    public List<RoomDTO> findAvailableRooms(int bedCount, LocalDate checkIn, LocalDate checkOut) {
        List<RoomDTO> suitableRooms = roomService.findRoomsByBedCount(bedCount);

        return suitableRooms.stream()
                .filter(room -> isRoomAvailable(room.getId(), checkIn, checkOut))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateBookingStatuses() {
        LocalDate today = LocalDate.now();

        // Обновляем бронирования на "Текущие"
        List<BookingEntity> currentBookings = repo.findByStatusNotAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                BookingStatus.CANCELLED, today, today);

        currentBookings.forEach(booking -> {
            if (booking.getStatus() != BookingStatus.CURRENT) {
                booking.setStatus(BookingStatus.CURRENT);
                repo.save(booking);
                log.info("Updated booking {} to CURRENT", booking.getId());
            }
        });

        // Обновляем бронирования на "Прошедшие"
        List<BookingEntity> pastBookings = repo.findByStatusNotAndCheckOutDateBefore(
                BookingStatus.CANCELLED, today);

        pastBookings.forEach(booking -> {
            if (booking.getStatus() != BookingStatus.PAST) {
                booking.setStatus(BookingStatus.PAST);
                repo.save(booking);
                log.info("Updated booking {} to PAST", booking.getId());
            }
        });
    }

    public BookingEntity findById(UUID id) {
        return repo.findById(id).orElse(null);
    }
}
