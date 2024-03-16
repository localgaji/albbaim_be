package localgaji.albbaim.schedule.workTime.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.__core__.WeekDay;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.workplace.Workplace;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

import static localgaji.albbaim.__core__.StringToLocalDate.*;

public class WorkTimeRequest {
    @Schema(description = "매니저 스케줄 모집 시작 : 정보 저장")
    public record PostOpenRequest(
            @Schema(description = "주 시작 날짜")
            String startWeekDate,
            @Schema(description = "해당 주차의 근무 시간대 정보")
            List<List<WorkTimeHeadCountDTO>> template
    ) {

        public Week toWeekEntity(Workplace workplace) {
            return Week.builder()
                    .workplace(workplace)
                    .startWeekDate(stringToLocalDate(startWeekDate))
                    .build();
        }
        public List<Date> toDateEntities(Week week) {
            LocalDate startWeek = week.getStartWeekDate();

            return Arrays.stream(WeekDay.values()).map(day -> Date.builder()
                    .localDate(startWeek.plusDays(day.getIndex()))
                    .week(week)
                    .build()
            ).toList();
        }

        public List<WorkTime> toWorkTimeEntities(Date date) {
            List<WorkTimeHeadCountDTO> workTimeDTOList = template.get(
                    Period.between(
                            stringToLocalDate(startWeekDate),
                            date.getLocalDate()
                    ).getDays());

            return workTimeDTOList.stream().map(dto ->
                    WorkTime.builder()
                            .date(date)
                            .workTimeName(dto.getTitle())
                            .startTime(stringToLocalTime(dto.getStartTime()))
                            .endTime(stringToLocalTime(dto.getEndTime()))
                            .headcount(dto.getHeadCount())
                            .build()
            ).toList();
        }
    }
}
