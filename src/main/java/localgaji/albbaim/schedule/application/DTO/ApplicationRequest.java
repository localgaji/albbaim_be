package localgaji.albbaim.schedule.application.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import static localgaji.albbaim.schedule.application.DTO.ApplicationDTO.*;

public class ApplicationRequest {
    @Schema(description = "알바 스케줄 신청")
    public record PostApplyRequest(
            @Schema(description = "신청 정보")
            List<List<CheckedList>> apply
    ) {
    }
}
