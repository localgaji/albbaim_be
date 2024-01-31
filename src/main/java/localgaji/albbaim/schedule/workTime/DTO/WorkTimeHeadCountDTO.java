package localgaji.albbaim.schedule.workTime.DTO;

import localgaji.albbaim.schedule.__commonDTO__.WorkTimeDTO;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class WorkTimeHeadCountDTO extends WorkTimeDTO {
    private final Integer headCount;
    public WorkTimeHeadCountDTO(String title, String startTime, String endTime, Integer headCount) {
        super(title, startTime, endTime);
        this.headCount = headCount;
    }
}