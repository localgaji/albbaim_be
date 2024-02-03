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
import java.util.stream.IntStream;

import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.*;

@Service @RequiredArgsConstructor
public class ApplicationRecommendService {

    private final WeekService weekService;

    // 인원수가 미달이거나 딱 맞을 때 : 다 받기
    private List<List<List<User>>> noOverWorkTime(Week week, Map<User, Integer> userFixedTime) {

        return week.getDateList().stream().map(date ->
                        date.getWorkTimeList().stream().map(workTime -> {
                            List<Application> applicationList = workTime.getApplicationList();
                            List<User> userList = new ArrayList<>();

                            // 인원수가 미달이거나 딱 맞으면 다 넣기
                            if (applicationList.size() <= workTime.getHeadcount()) {
                                for (Application application : applicationList) {
                                    pushIntoMap(userFixedTime, application);
                                    userList.add(application.getUser());
                                }
                            }
                            return userList;
                        }).collect(Collectors.toList())
                ).toList();
    }

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
        List<List<List<User>>> weekly = noOverWorkTime(week, userFixedTime);

        /* 남는 자리 채우기
            1. 이미 해당일 근무면 제외
            2. 유저별 총 근무 시간 보고 골고루 */
        for (int d = 0; d < 7; d++) {
            Date date = week.getDateList().get(d);
            List<WorkTime> workTimeList = date.getWorkTimeList();
            List<List<User>> dailyFixed = weekly.get(d);

            for (int w = 0; w < workTimeList.size(); w++) {
                WorkTime workTime = workTimeList.get(w);
                List<Application> applicationList = workTime.getApplicationList();
                // 채워야할 인원 이미 다 채워졌다면
                if (applicationList.size() <= workTime.getHeadcount()) {
                    continue;
                }

                // applicationList를 확정된 근무시간이 적은 순서대로 정렬
                workTime.getApplicationList()
                        .sort(Comparator.comparingInt(application ->
                                userFixedTime.getOrDefault(application.getUser(), 0))
                );

                List<User> workTimeFixed = dailyFixed.get(w);

                // 확정 근무시간이 적은 순서대로 지원자 채우기
                for (Application application : workTime.getApplicationList()) {
                    User applicant = application.getUser();

                    // 이미 직원이 해당일 근무일이면 패스
                    boolean alreadyWorkInDay = dailyFixed
                            .stream().flatMap(List::stream)
                            .anyMatch(u -> u.equals(applicant));

                    if (!alreadyWorkInDay) {
                        workTimeFixed.add(applicant);
                        pushIntoMap(userFixedTime, application);

                        if (workTimeFixed.size() == workTime.getHeadcount()) {
                            break;
                        }
                    }
                }
            }
        }

        return toDTO(weekly, week);
    }

    private GetRecommendsResponse toDTO(List<List<List<User>>> weekly, Week week) {
        List<List<WorkTimeWorkerListDTO>> weeklyData = new ArrayList<>();

        IntStream.range(0, 7).forEach(d -> {
            List<WorkTime> workTimeList = week.getDateList().get(d).getWorkTimeList();
            List<WorkTimeWorkerListDTO> daily = new ArrayList<>();

            IntStream.range(0, workTimeList.size()).forEach(w -> {
                WorkTime workTime = workTimeList.get(w);
                WorkTimeWorkerListDTO dto = WorkTimeWorkerListDTO.builder()
                        .title(workTime.getWorkTimeName())
                        .startTime(workTime.getStartTime().toString())
                        .endTime(workTime.getEndTime().toString())
                        .workerList(weekly.get(d).get(w).stream().map(WorkTimeWorkerListDTO.Worker::new).toList())
                        .build();
                daily.add(dto);
            });
            weeklyData.add(daily);
        });

        return new GetRecommendsResponse(List.of(weeklyData));
   }
}




