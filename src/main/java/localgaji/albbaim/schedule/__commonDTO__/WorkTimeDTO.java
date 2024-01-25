package localgaji.albbaim.schedule.__commonDTO__;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter @AllArgsConstructor @NoArgsConstructor @SuperBuilder
public class WorkTimeDTO {
    private String title;
    private String startTime;
    private String endTime;
}

