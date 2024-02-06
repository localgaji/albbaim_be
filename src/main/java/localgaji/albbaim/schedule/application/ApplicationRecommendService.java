package localgaji.albbaim.schedule.application;

import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static localgaji.albbaim.__core__.StringToLocalDate.*;
import static localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.GetRecommendResponse.*;

@Service @RequiredArgsConstructor
public class ApplicationRecommendService {

    private final WeekService weekService;

    // 인원수가 미달이거나 딱 맞을 때 : 다 받기
    private List<List<WorkTimeRecommendWorkers>> noOverWorkTime(Week week, Map<User, Integer> userFixedTime) {

        return week.getDateList().stream().map(date ->
                date.getWorkTimeList().stream().map(workTime -> {
                    WorkTimeRecommendWorkers dto = new WorkTimeRecommendWorkers(workTime, new ArrayList<>());

                    // 인원수가 미달이거나 딱 맞으면 다 넣기
                    List<Application> applicationList = workTime.getApplicationList();

                    if (applicationList.size() <= workTime.getHeadcount()) {
                        for (Application application : applicationList) {
                            pushIntoMap(userFixedTime, application);
                            dto.getWorkerList().add(new Worker(application.getUser()));
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
                application.getWorkTime().getStartTime(),
                application.getWorkTime().getEndTime()
        ).toMinutes();

        if (userFixedTime.containsKey(applicant)) {
            userFixedTime.put(applicant, userFixedTime.get(applicant) + time);
        } else {
            userFixedTime.put(applicant, time);
        }
    }

    public GetRecommendResponse getRecommends(User user, String startWeekDate) {
        Week week = weekService.getWeekByStartWeekDate(user, startWeekDate);
        Map<User, Integer> userFixedTime = new HashMap<>();
        List<List<WorkTimeRecommendWorkers>> weekly = noOverWorkTime(week, userFixedTime);

        /* 남는 자리 채우기
            1. 이미 해당일 근무면 제외
            2. 유저별 총 근무 시간 보고 골고루 */
        for (int d = 0; d < 7; d++) {
            Date date = week.getDateList().get(d);
            List<WorkTime> workTimeList = date.getWorkTimeList();
            List<WorkTimeRecommendWorkers> dailyFixed = weekly.get(d);

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

                    boolean cannotWorkInThisDay = cannotWorkInThisDay(dailyFixed, w, applicant);

                    // 지원자를 해당 시간에 넣기
                    if (!cannotWorkInThisDay) {
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
        return new GetRecommendResponse(weekly);
    }

    private boolean cannotWorkInThisDay(List<WorkTimeRecommendWorkers> dailyFixed,
                                        int workTimeIndex,
                                        User applicant) {

        LocalTime startTime = stringToLocalTime(dailyFixed.get(workTimeIndex).getStartTime());
        LocalTime endTime = stringToLocalTime(dailyFixed.get(workTimeIndex).getEndTime());

        return dailyFixed.stream().anyMatch(wt -> {
            // 이날 다른 시간대에 일 하는지
            boolean isWorkThisTime = wt.getWorkerList().stream().anyMatch(wk ->
                    wk.userId().equals(applicant.getUserId())
            );
            // 일 안하면 false
            if (!isWorkThisTime) {
                return false;
            }
            // 겹치거나 띄엄띄엄이면 true, 연속해서 할 수 있으면 false
            LocalTime wtStartTime = stringToLocalTime(wt.getStartTime());
            LocalTime wtEndTime = stringToLocalTime(wt.getEndTime());

            boolean isNotContinuous = !wtEndTime.equals(startTime) &&
                    !wtStartTime.equals(endTime);
            if (isNotContinuous) {
                return true;
            }
            // 하루 근무 시간이 12시간 이상이면 true
            return Duration.between(wtStartTime, endTime).toMinutes() > 720 ||
                    Duration.between(startTime, wtEndTime).toMinutes() > 720;
        });
    }
}