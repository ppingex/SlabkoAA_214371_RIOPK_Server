package slabko.rooms;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService service;

    @GetMapping()
    public List<RoomEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public RoomEntity findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping("/num/{number}")
    public RoomEntity findByNumber(@PathVariable String number) {
        return service.findByNumber(number);
    }

    @GetMapping("/beds/{beds}")
    public List<RoomEntity> findByNumberOfBeds(@PathVariable int beds) {
        return service.findByNumberOfBeds(beds);
    }

    @PostMapping()
    public RoomEntity add( @RequestBody @Valid RoomCreateDTO createDTO) {
        return service.add(createDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoomEntity> updateRoom(
            @PathVariable UUID id,
            @RequestBody @Valid RoomUpdateDTO updateDTO) {
        RoomEntity updatedRoom = service.update(id, updateDTO);
        return ResponseEntity.ok(updatedRoom);
    }
}
