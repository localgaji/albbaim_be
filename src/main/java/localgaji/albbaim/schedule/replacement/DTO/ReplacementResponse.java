package localgaji.albbaim.schedule.replacement.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.schedule.replacement.Replacement;
import localgaji.albbaim.schedule.workTime.WorkTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReplacementResponse {
    @Schema(description = "모집 중인 대타 리스트")
    public record GetReplacementList(
            List<ReplacementInfo> replacementList,
            Integer pageNumber,
            Boolean hasNext
    ) {
    }

    public static class ReplacementInfo {
        public final LocalDate date;
        public final LocalTime startTime;
        public final LocalTime endTime;

        public ReplacementInfo(Replacement replacement) {
            WorkTime workTime = replacement.getFixed().getWorkTime();

            this.date = workTime.getDate().getLocalDate();
            this.startTime = workTime.getStartTime();
            this.endTime = workTime.getEndTime();
        }
    }
}
