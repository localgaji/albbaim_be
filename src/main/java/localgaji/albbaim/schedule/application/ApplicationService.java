package localgaji.albbaim.schedule.application;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.schedule.__commonDTO__.WorkTimeWorkerListDTO;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.schedule.workTime.WorkTimeRepository;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static localgaji.albbaim.schedule.__commonDTO__.WorkTimeWorkerListDTO.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationDTO.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationRequest.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.*;

@Service @RequiredArgsConstructor
public class ApplicationService {

    private final WeekService weekService;
    private final ApplicationRepository applicationRepository;
    private final WorkTimeRepository workTimeRepository;

    // 해당 주의 모든 날짜 -> 모든 시간대 -> 모든 신청자 리스트 조회
    public GetApplyStatusResponse getApplicationList(User user, String startWeekDateString) {
        // 주 entity 찾기
        Week foundWeek = weekService.getWeekByStartWeekDate(user, startWeekDateString);

        // 주 -> 일 -> 시간대 -> 신청자 정보를 시간대 중심으로 가공
        List<List<WorkTimeWorkerListDTO>> weekly = new ArrayList<>();

        for (Date date : foundWeek.getDateList()) {
            List<WorkTimeWorkerListDTO> daily = new ArrayList<>();
            for (WorkTime workTime : date.getWorkTimeList()) {
                // 해당 시간대 신청자 리스트에서 필요한 정보만 worker dto 로 가공
                List<Worker> workers = workTime.getApplicationList()
                        .stream()
                        .map(application -> new Worker(application.getUser()))
                        .toList();
                // 해당 시간대 정보 + 신청자 정보 dto 생성
                WorkTimeWorkerListDTO dto = WorkTimeWorkerListDTO.builder()
                        .title(workTime.getWorkTimeName())
                        .startTime(workTime.getStartTime().toString())
                        .endTime(workTime.getEndTime().toString())
                        .workerList(workers)
                        .build();
                daily.add(dto);
            }
            weekly.add(daily);
        }
        return new GetApplyStatusResponse(weekly);
    }

    // 해당 주의 스케줄 신청 선택리스트 조회
    public GetApplyFormResponse getApplicationCheckForm(User user, String startWeekDateString) {
        // 주 entity 찾기
        Week foundWeek = weekService.getWeekByStartWeekDate(user, startWeekDateString);

        // 주 -> 일 -> 시간대 -> 신청여부 정보를 시간대 중심으로 가공
        List<List<WorkTimeChoice>> weekly = new ArrayList<>();

        for (Date date : foundWeek.getDateList()) {
            List<WorkTimeChoice> daily = new ArrayList<>();
            for (WorkTime workTime : date.getWorkTimeList()) {
                // 해당 시간대 정보 + 신청자 정보 dto 생성
                WorkTimeChoice dto = WorkTimeChoice.builder()
                        .workTimeId(workTime.getWorkTimeId())
                        .title(workTime.getWorkTimeName())
                        .startTime(workTime.getStartTime().toString())
                        .endTime(workTime.getEndTime().toString())
                        // application 존재하는지
                        .isChecked(getApplicationByWorkTime(user, workTime).isPresent())
                        .build();
                daily.add(dto);
            }
            weekly.add(daily);
        }
        return new GetApplyFormResponse(weekly);
    }

    // 신청 정보 저장
    @Transactional
    public void saveApplication(User user, PostApplyRequest request) {
        List<List<HasWorkTimeChecked>> weekly = request.apply();
        for (List<HasWorkTimeChecked> daily : weekly) {
            for (HasWorkTimeChecked workTimeChecked : daily) {
                WorkTime workTime = workTimeRepository.findById(workTimeChecked.workTimeId())
                        .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

                Optional<Application> opt = getApplicationByWorkTime(user, workTime);

                if (workTimeChecked.isChecked()) {
                    // true : 이미 있을 때
                    if (opt.isPresent()) {
                        continue;
                    }
                    // true : 없을 때 -> 만들기
                    Application application = Application.builder()
                            .user(user)
                            .workTime(workTime)
                            .build();
                    applicationRepository.save(application);
                } else {
                    // false : 이미 없을 때
                    if (opt.isEmpty()) {
                        continue;
                    }
                    // false : 있을 때 -> 없애기
                    applicationRepository.delete(opt.get());
                }
            }
        }
    }

    // 해당 시간대 -> 신청 정보
    private Optional<Application> getApplicationByWorkTime(User user, WorkTime workTime) {
        return workTime.getApplicationList()
                .stream()
                .filter(application -> application.getUser().equals(user))
                .findAny();
    }
}
