package localgaji.albbaim.schedule.scheduleDTO;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ResponseSchedule {

    @Schema(description = "매니저 스케줄 모집 마감 : 추천 후보 조회")
    public record GetRecommendsResponse(
            List<List<List<WorkTimeWorkerList>>> recommends
    ) {

    }

    @Schema(description = "매니저 스케줄 모집 시작 : 스케줄 템플릿 조회")
    public record GetTemplateResponse(
            List<List<WorkTimeData>> template
    ) {
    }

    @Schema(description = "매니저 : 신청자 명단 조회")
    public record GetApplyStatusResponse(
            List<List<WorkTimeWorkerList>> applyStatus
    ) {
    }

    @Schema(description = "월간 스케줄 조회")
    public record GetMonthlyResponse(
            @Schema(description = "월간 스케줄 (1주차 월 ~ )")
            List<DailyWorkTime> schedule,
            @Schema(description = "이번주 / 이번달 총 근무 시간")
            TotalWorkTime work_summary
    ) {
        private record DailyWorkTime(
                String date,
                List<String> workTime
        ) {
        }
        private record TotalWorkTime(
                String weekly,
                String monthly
        ) {
        }
    }

    @Schema(description = "일간 근무자 조회")
    public record GetDailyWorkersResponse(
            List<WorkTimeData> schedule
    ) {
    }

    @Schema(description = "모집 가능 여부 조회")
    public record GetWeekStatusResponse(
            String weekStatus
    ) {
    }

    @Schema(description = "알바 스케줄 신청 : 체크 리스트 조회")
    public record GetApplyFormResponse(
            Selected selected

    ) {
        private static class Selected extends WorkTimeData {
            Boolean isChecked;
        }
    }

    private static class WorkTimeWorkerList extends WorkTimeData {
        List<Worker> workerList;
    }

    private static class WorkTimeData {
            String title;
            String startTime;
            String endTime;
    }

    private record Worker(
            String userId,
            String name
    ) {
    }
}
