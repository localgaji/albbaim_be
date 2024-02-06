package localgaji.albbaim.schedule.fixed.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO.*;

public class FixedRequest {
    @Schema(description = "주간 근무 확정")
    public record PostRequest(
            List<List<WorkTimeWorkers>> weeklyWorkerListWannaFix
    ) {
        @AllArgsConstructor @Getter
        public static class WorkTimeWorkers {
            private Long workTimeId;
            private List<Worker> workerList;
        }
    }
}
