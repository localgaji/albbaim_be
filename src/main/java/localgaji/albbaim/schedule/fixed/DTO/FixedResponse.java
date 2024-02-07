package localgaji.albbaim.schedule.fixed.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO;

import java.util.List;

public class FixedResponse {

    @Schema(description = "월간 스케줄 조회")
    public record GetMonthlyResponse(
            @Schema(description = "월간 스케줄 (1주차 월 ~ )")
            List<List<DailySchedule>> monthly,
            @Schema(description = "이번주 / 이번달 총 근무 시간")
            TotalWorkTime totalWorkTime
    ) {
        public record DailySchedule(
                String date,
                Boolean hasFixed,
                List<String> workTimes
        ) {
        }
        public record TotalWorkTime(
                String monthly
        ) {
            public TotalWorkTime(int minutes) {
                this(String.valueOf(minutes / 60));
            }
        }
    }

    @Schema(description = "일간 근무자 조회")
    public record GetDailyWorkersResponse(
            List<WorkerListDTO> schedule
    ) {
    }

}
