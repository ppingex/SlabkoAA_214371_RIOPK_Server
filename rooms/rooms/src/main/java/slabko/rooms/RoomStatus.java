package slabko.rooms;

import lombok.Getter;

@Getter
public enum RoomStatus {
    OCCUPIED("Занят"),
    FREE("Свободен");

    private final String displayName;

    RoomStatus(String displayName) {
        this.displayName = displayName;
    }

}
