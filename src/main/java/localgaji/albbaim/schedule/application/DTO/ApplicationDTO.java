package localgaji.albbaim.schedule.application.DTO;

import localgaji.albbaim.schedule.__commonDTO__.WorkTimeDTO;
import lombok.experimental.SuperBuilder;

public class ApplicationDTO {
    @SuperBuilder
    public static class WorkTimeChoice extends WorkTimeDTO {
        Long workTimeId;
        Boolean isChecked;
        public WorkTimeChoice(String title, String startTime, String endTime, Long workTimeId, Boolean isChecked) {
            super(title, startTime, endTime);
            this.workTimeId = workTimeId;
            this.isChecked = isChecked;
        }
    }

    public record HasWorkTimeChecked (
            Long workTimeId,
            Boolean isChecked
    ) {
    }
}
