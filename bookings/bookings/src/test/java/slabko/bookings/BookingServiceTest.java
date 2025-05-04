package slabko.bookings;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository repo;

    @Mock
    private ClientService clientService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private BookingService service;


    @Test
    void findAvailableRooms_ShouldReturnRoomsThatAreAvailable() {
        // Arrange
        int bedCount = 2;
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(3);

        RoomDTO room1 = new RoomDTO();
        room1.setId(UUID.randomUUID());
        room1.setNumber("101");
        room1.setNumberOfBeds(bedCount);

        RoomDTO room2 = new RoomDTO();
        room2.setId(UUID.randomUUID());
        room2.setNumber("102");
        room2.setNumberOfBeds(bedCount);

        when(roomService.findRoomsByBedCount(bedCount)).thenReturn(List.of(room1, room2));
        when(repo.findConflictingBookings(any(), any(), any())).thenReturn(Collections.emptyList());

        // Act
        List<RoomDTO> result = service.findAvailableRooms(bedCount, checkIn, checkOut);

        // Assert
        assertEquals(2, result.size());
        verify(repo, times(2)).findConflictingBookings(any(), any(), any());
    }


    @Test
    void updateBookingStatuses_ShouldUpdateStatusesCorrectly() {
        // Arrange
        LocalDate today = LocalDate.now();

        BookingEntity currentBooking = new BookingEntity();
        currentBooking.setId(UUID.randomUUID());
        currentBooking.setStatus(BookingStatus.PENDING);
        currentBooking.setCheckInDate(today.minusDays(1));
        currentBooking.setCheckOutDate(today.plusDays(1));

        BookingEntity pastBooking = new BookingEntity();
        pastBooking.setId(UUID.randomUUID());
        pastBooking.setStatus(BookingStatus.PENDING);
        pastBooking.setCheckInDate(today.minusDays(5));
        pastBooking.setCheckOutDate(today.minusDays(1));

        when(repo.findByStatusNotAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
                eq(BookingStatus.CANCELLED), eq(today), eq(today)))
                .thenReturn(List.of(currentBooking));

        when(repo.findByStatusNotAndCheckOutDateBefore(
                eq(BookingStatus.CANCELLED), eq(today)))
                .thenReturn(List.of(pastBooking));

        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        service.updateBookingStatuses();

        // Assert
        assertEquals(BookingStatus.CURRENT, currentBooking.getStatus());
        assertEquals(BookingStatus.PAST, pastBooking.getStatus());
        verify(repo, times(2)).save(any());
    }

}
