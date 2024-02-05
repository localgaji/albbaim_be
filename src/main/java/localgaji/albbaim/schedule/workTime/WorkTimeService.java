package localgaji.albbaim.schedule.workTime;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.schedule.date.DateService;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.workTime.DTO.WorkTimeHeadCountDTO;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static localgaji.albbaim.schedule.week.DTO.WeekResponse.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeRequest.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeResponse.*;

@Service
@RequiredArgsConstructor
public class WorkTimeService {

    private final WorkTimeRepository workTimeRepository;
    private final DateService dateService;
    private final WeekService weekService;

    // 시간대 정보 저장
    @Transactional
    public void saveWorkTimes(User user, PostOpenRequest request) {
        // 이미 모집 시작된 주일 때 예외 처리
        WeekStatusType status = weekService.findWeekStatus(user, request.startWeekDate());
        if (!status.equals(WeekStatusType.ALLOCATABLE)) {
            throw new CustomException(ErrorType.ALREADY_HAVE);
        }
        // 주 정보 저장
        Week week = request.toWeekEntity(user.getWorkplace());
        weekService.createNewWeek(week);

        List<Date> dateList = request.toDateEntities(week);
        for (Date date : dateList) {
            // 일 정보 저장
            dateService.createNewDate(date);

            List<WorkTime> workTimeList = request.toWorkTimeEntities(date);
            for (WorkTime workTime : workTimeList) {
                // 시간대 정보 저장
                createNewWorkTime(workTime);
            }
        }
    }

    // 가장 최근 모집했던 시간대 템플릿 가져오기
    public GetTemplateResponse getLastWorkTimeTemplate(User user) {
        // 최근 모집했던 주
        Optional<Week> lastWeek = weekService.getLastWeek(user);

        // 없으면 디폴트 시간대 템플릿
        if (lastWeek.isEmpty()) {
            return new GetTemplateResponse(defaultTemplate);
        }

        // 있으면 주에서 일들 다 꺼냄
        List<Date> dailyWorkTimes = lastWeek.get().getDateList();
        // 일들에서 시간대들 다 꺼냄 -> 시간대엔티티를 WorkTimeHeadCountDTO 로 변환
        List<List<WorkTimeHeadCountDTO>> template = dailyWorkTimes.stream().map(date ->
                date.getWorkTimeList().stream().map(
                        this::entityToDTO
                ).toList()
        ).toList();
        return new GetTemplateResponse(template);
    }

    // work time 엔티티 저장
    @Transactional
    private void createNewWorkTime(WorkTime workTime) {
        workTime.addWorkTimeToDate();
        workTimeRepository.save(workTime);
    }

    // 기본 WorkTime
    private final WorkTimeHeadCountDTO defaultTime = WorkTimeHeadCountDTO.builder()
            .title("오픈")
            .startTime("09:00")
            .endTime("12:00")
            .headCount(0)
            .build();

    // 기본 weekly worktime template
    private final List<List<WorkTimeHeadCountDTO>> defaultTemplate = Stream.generate(() ->
                    new ArrayList<>(Collections.singletonList(defaultTime)))
            .limit(7)
            .collect(Collectors.toList());


    private WorkTimeHeadCountDTO entityToDTO(WorkTime workTime) {
        return WorkTimeHeadCountDTO.builder()
                .title(workTime.getWorkTimeName())
                .startTime(workTime.getStartTime().toString())
                .endTime(workTime.getEndTime().toString())
                .headCount(workTime.getHeadcount())
                .build();
    }

    public WorkTime findWorkTimeById(Long workTimeId) {
        return workTimeRepository.findById(workTimeId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
    }
}
