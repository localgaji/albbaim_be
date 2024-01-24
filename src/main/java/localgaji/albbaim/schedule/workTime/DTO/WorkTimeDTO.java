package localgaji.albbaim.schedule.workTime.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class WorkTimeDTO {
    private String title;
    private String startTime;
    private String endTime;
}

