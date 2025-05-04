package slabko.rooms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {
    Optional<RoomEntity> findByNumber(String number);
    List<RoomEntity> findByNumberOfBeds(int beds);

    @Query("SELECT r FROM RoomEntity r WHERE r.id NOT IN :ids")
    List<RoomEntity> findByIdNotIn(@Param("ids") Collection<UUID> ids);

}
