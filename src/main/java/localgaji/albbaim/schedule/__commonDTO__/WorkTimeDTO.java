package localgaji.albbaim.schedule.__commonDTO__;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter @SuperBuilder @AllArgsConstructor
public class WorkTimeDTO {
    private String title;
    private String startTime;
    private String endTime;
}

