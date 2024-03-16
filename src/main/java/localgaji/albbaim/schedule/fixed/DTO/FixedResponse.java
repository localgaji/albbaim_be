package localgaji.albbaim.schedule.fixed.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO;

import java.util.List;

public class FixedResponse {

    @Schema(description = "월간 스케줄 조회")
    public record GetMonthlyResponse(
            @Schema(description = "월간 스케줄 (1주차 월 ~ )")
            List<List<DailySchedule>> monthly
    ) {
    }

    public record DailySchedule(
            String date,
            Boolean hasFixed,
            List<String> workTimes
    ) {
    }

    @Schema(description = "일간 근무자 조회")
    public record GetDailyWorkersResponse(
            List<WorkerListDTO> schedule
    ) {
    }

}
