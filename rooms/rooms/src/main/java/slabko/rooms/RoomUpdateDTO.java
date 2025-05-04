package slabko.rooms;

import lombok.Data;

@Data
public class RoomUpdateDTO {

    private RoomType roomType;
    private Integer numberOfBeds;
    private Double area;
    private Double price;
    private String additionalParameters;

}
