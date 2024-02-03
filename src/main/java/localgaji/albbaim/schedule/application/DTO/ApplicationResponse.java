package localgaji.albbaim.schedule.application.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.schedule.__commonDTO__.WorkTimeWorkerListDTO;

import java.util.List;

import static localgaji.albbaim.schedule.application.DTO.ApplicationDTO.*;

public class ApplicationResponse {
    @Schema(description = "알바 스케줄 신청 : 체크 리스트 조회")
    public record GetApplyFormResponse(
            List<List<WorkTimeChoice>> selected
    ) {
    }
    @Schema(description = "매니저 : 신청자 명단 조회")
    public record GetApplyStatusResponse(
            List<List<WorkTimeWorkerListDTO>> applyStatus
    ) {
    }
    @Schema(description = "매니저 스케줄 모집 마감 : 추천 후보 조회")
    public record GetRecommendsResponse(
            List<List<List<WorkTimeWorkerListDTO>>> recommends
    ) {
    }
}