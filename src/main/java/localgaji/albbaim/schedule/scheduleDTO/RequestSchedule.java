package localgaji.albbaim.schedule.scheduleDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class RequestSchedule {
    @Schema(description = "매니저 스케줄 모집 마감 : 스케줄 확정 (추천 스케줄 선택)")
    public record PostRecommendsRequest(
            @Schema(description = "선택한 스케줄 번호")
            String candidate
    ) {
    }

    @Schema(description = "매니저 스케줄 모집 마감 : 추천 후보 조회")
    public record GetRecommendsRequest(
            @Schema(description = "주 시작 날짜")
            String startWeekDate
    ) {
    }

    @Schema(description = "매니저 스케줄 모집 시작 : 정보 저장")
    public record PostOpenApplicationRequest(
            @Schema(description = "회원 타입")
            Boolean isAdmin
    ) {
    }

    @Schema(description = "매니저 스케줄 모집 시작 : 스케줄 템플릿 조회")
    public record GetTemplateRequest(
            @Schema(description = "주 시작 날짜")
            String startWeekDate
    ) {
    }

    @Schema(description = "매니저 : 모집 현황 조회")
    public record GetApplyStatusRequest(
            @Schema(description = "주 시작 날짜")
            String startWeekDate
    ) {
    }

    @Schema(description = "월간 스케줄 조회")
    public record GetMonthlyRequest(
            @Schema(description = "년도")
            int year,
            @Schema(description = "월")
            int month,
            @Schema(description = "회원 아이디")
            int userId
    ) {
    }

    @Schema(description = "일간 근무자 조회")
    public record GetDailyWorkersRequest(
            @Schema(description = "날짜")
            String selectedDate
    ) {
    }

    @Schema(description = "모집 가능 여부 조회")
    public record GetWeekStatusRequest(
            @Schema(description = "주 시작 날짜")
            String startWeekDate
    ) {
    }

    @Schema(description = "알바 스케줄 신청 : 체크 리스트 조회")
    public record GetApplyFormRequest(
            @Schema(description = "주 시작 날짜")
            String startWeekDate
    ) {
    }

    @Schema(description = "알바 스케줄 신청")
    public record PutApplyRequest(
            @Schema(description = "회원 타입")
            Boolean isAdmin
    ) {
    }
}
