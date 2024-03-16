package localgaji.albbaim.schedule.application.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.schedule.__commonDTO__.WorkTimeDTO;
import localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO;
import localgaji.albbaim.schedule.workTime.WorkTime;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO.*;

public class ApplicationResponse {
    @Schema(description = "알바 스케줄 신청 : 체크 리스트 조회")
    public record GetApplyFormResponse(
            List<List<WorkTimeChoice>> checklist
    ) {
    }

    @SuperBuilder @Getter
    public static class WorkTimeChoice extends WorkTimeDTO {
        Long workTimeId;
        Boolean isChecked;
        public WorkTimeChoice(WorkTime workTime, Boolean isChecked) {
            super(workTime.getWorkTimeName(),
                    workTime.getStartTime().toString(),
                    workTime.getEndTime().toString()
            );
            this.workTimeId = workTime.getWorkTimeId();
            this.isChecked = isChecked;
        }
    }

    @Schema(description = "매니저 : 신청자 명단 조회")
    public record GetApplyStatusResponse(
            List<List<WorkerListDTO>> applyStatus
    ) {
    }
    @Schema(description = "매니저 스케줄 모집 마감 : 추천 스케줄 조회")
    public record GetRecommendResponse(
            List<List<WorkTimeRecommendWorkers>> recommends
    ) {
    }

    @Getter
    public static class WorkTimeRecommendWorkers extends WorkTimeDTO {
        private final Long workTimeId;
        private final List<Worker> workerList;

        public WorkTimeRecommendWorkers(WorkTime workTime, List<Worker> workerList) {
            super(
                    workTime.getWorkTimeName(),
                    workTime.getStartTime().toString(),
                    workTime.getEndTime().toString()
            );
            this.workTimeId = workTime.getWorkTimeId();
            this.workerList = workerList;
        }
    }
}
