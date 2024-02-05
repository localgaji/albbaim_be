package localgaji.albbaim.schedule.application.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ApplicationRequest {
    @Schema(description = "알바 스케줄 신청")
    public record PostApplyRequest(
            @Schema(description = "주 시작 날짜")
            String startWeekDate,
            @Schema(description = "신청 정보")
            List<List<HasWorkTimeChecked>> apply
    ) {
        public record HasWorkTimeChecked (
                Long workTimeId,
                Boolean isChecked
        ) {
        }
    }
}
