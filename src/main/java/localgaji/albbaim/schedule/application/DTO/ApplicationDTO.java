package localgaji.albbaim.schedule.application.DTO;

import localgaji.albbaim.schedule.__commonDTO__.WorkTimeDTO;

public class ApplicationDTO {
    public static class Choices extends WorkTimeDTO {
        Long workTimeId;
        Boolean isChecked;
        public Choices(String title, String startTime, String endTime, Long workTimeId, Boolean isChecked) {
            super(title, startTime, endTime);
            this.workTimeId = workTimeId;
            this.isChecked = isChecked;
        }
    }

    public record CheckedList (
            Long workTimeId,
            Boolean isChecked
    ) {
    }
}
