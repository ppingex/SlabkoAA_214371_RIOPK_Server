package slabko.rooms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomCreateDTO {

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
