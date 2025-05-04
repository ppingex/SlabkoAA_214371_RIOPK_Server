package slabko.bookings;

import lombok.Getter;

@Getter
public enum BookingStatus {
    PENDING("Предстоящее"),
    CURRENT("Текущее"),
    PAST("Прошедщее"),
    CANCELLED("Отменено");

    private final String displayName;

    BookingStatus(String displayName) {
        this.displayName = displayName;
    }

}
