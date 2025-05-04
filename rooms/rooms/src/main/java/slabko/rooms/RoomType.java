package slabko.rooms;

import lombok.Getter;

@Getter
public enum RoomType {
    LUX("Люкс"),
    STANDARD("Стандарт"),
    ECONOMY("Эконом");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

}
