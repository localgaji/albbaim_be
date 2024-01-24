package localgaji.albbaim.schedule.workTime;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.schedule.workTime.DTO.WorkTimeDTO;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.date.DateRepository;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekRepository;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static localgaji.albbaim.schedule.workTime.DTO.Request.*;
import static localgaji.albbaim.schedule.workTime.DTO.Response.*;

@Service
@RequiredArgsConstructor
public class WorkTimeService {

    private final WeekRepository weekRepository;
    private final DateRepository dateRepository;
    private final WorkTimeRepository workTimeRepository;

    // 시간대 정보 저장
    @Transactional
    public void saveWorkTimes(User user, PostOpenRequest request) {
        // 주 정보 저장
        Week week = request.toWeekEntity(user.getWorkplace());
        week.addWeekToWorkplace();
        weekRepository.save(week);

        List<Date> dateList = request.toDateEntities(week);
        for (Date date : dateList) {
            // 일 정보 저장
            dateRepository.save(date);
            date.addDateToWeek();

            List<WorkTime> workTimeList = request.toWorkTimeEntities(date);
            for (WorkTime workTime : workTimeList) {
                // 시간대 정보 저장
                workTime.addWorkTimeToDate();
                workTimeRepository.save(workTime);
            }
        }
    }

    // 가장 최근 모집했던 시간대 템플릿 가져오기
    public GetTemplateResponse getLastWorkTime(User user) {
        // 최근 모집했던 주
        Optional<Week> lastWeek = getLastWeek(user);

        // 없으면 디폴트 시간대 템플릿
        if (lastWeek.isEmpty()) {
            return new GetTemplateResponse(defaultTemplate);
        }

        // 있으면 주에서 일들 다 꺼냄
        List<Date> dailyWorkTimes = lastWeek.get().getDateList();
        // 일들에서 시간대들 다 꺼냄 -> 시간대엔티티를 WorkTimeDTO 로 변환
        List<List<WorkTimeDTO>> template = dailyWorkTimes.stream().map(date ->
                date.getWorkTimeList().stream().map(
                        this::entityToDTO
                ).toList()
        ).toList();
        return new GetTemplateResponse(template);
    }

    private final WorkTimeDTO defaultTime = WorkTimeDTO.builder()
            .title("오픈")
            .startTime("09:00")
            .endTime("12:00")
            .build();
    private final List<List<WorkTimeDTO>> defaultTemplate = Stream.generate(() ->
                    new ArrayList<>(Collections.singletonList(defaultTime)))
            .limit(7)
            .collect(Collectors.toList());

    // (해당 그룹의) 가장 최근 모집했던 주 조회
    private Optional<Week> getLastWeek(User user) {
        List<Week> prevWeeks = Optional.ofNullable(user.getWorkplace())
                .orElseThrow(() -> new CustomException(ErrorType.GROUP_NOT_FOUND))
                .getWeekList();
        // 모집한 적이 한번도 없을 경우
        if (prevWeeks.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(prevWeeks.get(prevWeeks.size() - 1));
    }
    private WorkTimeDTO entityToDTO(WorkTime workTime) {
        return WorkTimeDTO.builder()
                .title(workTime.getWorkTimeName())
                .startTime(workTime.getStartTime())
                .endTime(workTime.getEndTime())
                .build();
    }
}
