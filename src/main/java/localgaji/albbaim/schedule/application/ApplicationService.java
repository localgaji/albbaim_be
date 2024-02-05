package localgaji.albbaim.schedule.application;

import localgaji.albbaim.schedule.__commonDTO__.WorkTimeWorkerListDTO;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.schedule.workTime.WorkTimeService;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static localgaji.albbaim.schedule.__commonDTO__.WorkTimeWorkerListDTO.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationRequest.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationRequest.PostApplyRequest.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.GetApplyFormResponse.*;

@Service @RequiredArgsConstructor
public class ApplicationService {

    private final WeekService weekService;
    private final ApplicationRepository applicationRepository;
    private final WorkTimeService workTimeService;

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
                // 기존 신청 여부
                Boolean isChecked = getApplicationByWorkTime(user, workTime).isPresent();
                // 해당 시간대 정보 + 신청자 정보 dto 생성
                WorkTimeChoice dto = new WorkTimeChoice(workTime, isChecked);
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
                // workTime entity 조회
                WorkTime workTime = workTimeService.findWorkTimeById(workTimeChecked.workTimeId());

                // 해당 workTime 의 applicants 중, 해당 유저가 신청한 entity 찾기
                Optional<Application> opt = getApplicationByWorkTime(user, workTime);

                // 선택했을 때
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
                    application.applyToWorkTime();
                }
                // 선택 해제일 때
                else {
                    // false : 이미 없을 때
                    if (opt.isEmpty()) {
                        continue;
                    }
                    // false : 있을 때 -> 없애기
                    Application application = opt.get();
                    application.deleteInWorkTime();
                    applicationRepository.delete(application);

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
