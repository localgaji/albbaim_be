package localgaji.albbaim.schedule.workTime.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class WorkTimeResponse {
    @Schema(description = "매니저 스케줄 모집 시작 : 스케줄 템플릿 조회")
    public record GetTemplateResponse(
            List<List<WorkTimeHeadCountDTO>> template
    ) {
    }
}
