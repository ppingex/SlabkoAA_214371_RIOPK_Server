package slabko.bookings;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RoomDTO {

    @NotNull
    private UUID id;

    @NotNull
    private String number;

    @NotNull
    private RoomType roomType;

    @NotNull
    private int numberOfBeds;

    @NotNull
    private double area;

    @NotNull
    private double price;

    private String additionalParameters;

}
