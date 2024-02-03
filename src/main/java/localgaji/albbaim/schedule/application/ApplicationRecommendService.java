package localgaji.albbaim.schedule.application;

import localgaji.albbaim.schedule.__commonDTO__.WorkTimeWorkerListDTO;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static localgaji.albbaim.schedule.__commonDTO__.WorkTimeWorkerListDTO.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.*;

@Service @RequiredArgsConstructor
public class ApplicationRecommendService {

    private final WeekService weekService;

    // 인원수가 미달이거나 딱 맞을 때 : 다 받기
    private List<List<WorkTimeWorkerListDTO>> noOverWorkTime(Week week, Map<User, Integer> userFixedTime) {

        return week.getDateList().stream().map(date ->
                date.getWorkTimeList().stream().map(workTime -> {
                    List<Worker> workerList = new ArrayList<>();
                    WorkTimeWorkerListDTO dto = new WorkTimeWorkerListDTO(workTime, workerList);

                    // 인원수가 미달이거나 딱 맞으면 다 넣기
                    List<Application> applicationList = workTime.getApplicationList();

                    if (applicationList.size() <= workTime.getHeadcount()) {
                        for (Application application : applicationList) {
                            pushIntoMap(userFixedTime, application);
                            workerList.add(new Worker(application.getUser()));
                        }
                    }
                    return dto;
                }).collect(Collectors.toList())
        ).toList();
    }

    // { 유저 : 확정된 총 근무 시간 } map 업데이트
    private void pushIntoMap(Map<User, Integer> userFixedTime, Application application) {
        User applicant = application.getUser();
        int time = (int) Duration.between(
                application.getWorkTime().getEndTime(),
                application.getWorkTime().getEndTime()
        ).toMinutes();

        if (userFixedTime.containsKey(applicant)) {
            userFixedTime.put(applicant, userFixedTime.get(applicant) + time);
        } else {
            userFixedTime.put(applicant, time);
        }
    }

    public GetRecommendsResponse getRecommends(User user, String startWeekDate) {
        Week week = weekService.getWeekByStartWeekDate(user, startWeekDate);
        Map<User, Integer> userFixedTime = new HashMap<>();
        List<List<WorkTimeWorkerListDTO>> weekly = noOverWorkTime(week, userFixedTime);

        /* 남는 자리 채우기
            1. 이미 해당일 근무면 제외
            2. 유저별 총 근무 시간 보고 골고루 */
        for (int d = 0; d < 7; d++) {
            Date date = week.getDateList().get(d);
            List<WorkTime> workTimeList = date.getWorkTimeList();
            List<WorkTimeWorkerListDTO> dailyFixed = weekly.get(d);

            for (int w = 0; w < workTimeList.size(); w++) {
                WorkTime workTime = workTimeList.get(w);
                List<Application> applicationList = workTime.getApplicationList();
                List<Worker> fixedWorkersOfThisTime = dailyFixed.get(w).getWorkerList();

                // 해당 시간 이미 채워졌다면
                if (fixedWorkersOfThisTime.size() == applicationList.size()) {
                    continue;
                }

                // applicationList를 확정된 근무시간이 적은 순서대로 정렬
                applicationList
                        .sort(Comparator.comparingInt(application ->
                                userFixedTime.getOrDefault(application.getUser(), 0))
                        );

                // 확정 근무시간이 적은 순서대로 지원자 채우기
                for (Application application : applicationList) {
                    User applicant = application.getUser();

                    // 이미 직원이 해당일 근무일이면 패스
                    boolean alreadyFixedInThisDay = dailyFixed.stream().anyMatch(time ->
                            time.getWorkerList().stream().anyMatch(worker ->
                                    worker.userId().equals(applicant.getUserId())));

                    // 지원자를 해당 시간에 넣기
                    if (!alreadyFixedInThisDay) {
                        fixedWorkersOfThisTime.add(new Worker(applicant));
                        pushIntoMap(userFixedTime, application);

                        // 다 찼으면 종료
                        if (fixedWorkersOfThisTime.size() == workTime.getHeadcount()) {
                            break;
                        }
                    }
                }
            }
        }

        return new GetRecommendsResponse(List.of(weekly));
    }
}