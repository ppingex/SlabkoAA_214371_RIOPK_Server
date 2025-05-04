package slabko.bookings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    List<BookingEntity> findAllByCheckInDateBetween(LocalDate startDate, LocalDate endDate);
    List<BookingEntity> findByClientId(UUID id);
    List<BookingEntity> findByRoomId(UUID id);
    List<BookingEntity> findByCheckInDateBeforeAndCheckOutDateAfter(LocalDate checkInDate, LocalDate checkOutDate);
//    @Query("""
//        SELECT b FROM BookingEntity b
//        WHERE b.roomId = :roomId
//        AND b.status <> 'CANCELLED'
//        AND (
//            (b.checkInDate BETWEEN :checkIn AND :checkOut) OR
//            (b.checkOutDate BETWEEN :checkIn AND :checkOut) OR
//            (b.checkInDate <= :checkIn AND b.checkOutDate >= :checkOut)
//        )
//    """)
//    List<BookingEntity> findConflictingBookings(
//            @Param("roomId") UUID roomId,
//            @Param("checkIn") LocalDate checkIn,
//            @Param("checkOut") LocalDate checkOut
//    );

    @Query("""
    SELECT b FROM BookingEntity b 
    WHERE b.roomId = :roomId 
    AND b.status <> 'CANCELLED'
    AND (
        (b.checkInDate <= :checkOut AND b.checkOutDate >= :checkIn)
    )
""")
    List<BookingEntity> findConflictingBookings(
            @Param("roomId") UUID roomId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );

    @Query("""
    SELECT b FROM BookingEntity b 
    WHERE b.status <> 'CANCELLED'
    AND b.checkInDate <= :today 
    AND b.checkOutDate >= :today
""")
    List<BookingEntity> findCurrentBookings(@Param("today") LocalDate today);

    List<BookingEntity> findByStatusNotAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqual(
            BookingStatus status, LocalDate checkIn, LocalDate checkOut);

    List<BookingEntity> findByStatusNotAndCheckOutDateBefore(
            BookingStatus status, LocalDate date);
}
