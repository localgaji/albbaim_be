package localgaji.albbaim.schedule.week.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class WeekResponse {

    @Schema(description = "모집 가능 여부 조회")
    public record GetWeekStatusResponse(
            String weekStatus
    ) {
        public GetWeekStatusResponse(WeekStatusType weekStatusType) {
            this(weekStatusType.getStatus());
        }
    }
    @AllArgsConstructor
    @Getter
    public enum WeekStatusType {
        ALLOCATABLE("allocatable"),
        IN_PROGRESS("inProgress"),
        CLOSED("closed");

        private final String status;
    }
}
