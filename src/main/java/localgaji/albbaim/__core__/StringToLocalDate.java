package localgaji.albbaim.__core__;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalDate {
    public static LocalDate stringToLocalDate(String stringDate) {
        return LocalDate.parse(stringDate, DateTimeFormatter.ISO_DATE);
    }

    public static LocalTime stringToLocalTime(String stringTime) {
        return LocalTime.parse(stringTime, DateTimeFormatter.ofPattern("HH:mm"));
    }
}
