package localgaji.albbaim.schedule.application;

import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.*;

@RequiredArgsConstructor
public class RecommendLogic {

    private final Week week;

    // { 근무자 : 확정된 근무 시간 } map
    private final Map<User, Integer> userFixedTimeSum = new HashMap<>();

    // { 시간대 : 근무자리스트 } map
    private final MultiValueMap<WorkTime, User> weeklyWorkTimeWorkersMap = new LinkedMultiValueMap<>();

    public MultiValueMap<WorkTime, User> getRecommendMap() {
        // { 시간대 : 근무자리스트 } map 초기화
        init_weeklyWorkTimeWorkersMap();

        // 지원자가 미달이거나 딱 맞는 경우 다 넣기
        addWorkers_inMinorTime();

        // duration 긴 순서대로 정렬된 시간대 리스트
        List<WorkTime> workTimes_sortedByDuration = workTimes_sortedByDuration();

        // 지원자가 넘치는 경우 : userTotalFixedTime 적은 순서대로 넣기
        for (WorkTime workTime : workTimes_sortedByDuration) {
            addWorker_inMajorTime(workTime);
        }
        return weeklyWorkTimeWorkersMap;
    }

    // { 시간대 : 근무자리스트 } map 생성
    private void init_weeklyWorkTimeWorkersMap() {
        week.getDateList().forEach(date ->
                date.getWorkTimeList().forEach(workTime ->
                    weeklyWorkTimeWorkersMap.put(workTime, new ArrayList<>())
                )
        );
    }

    // duration 순으로 정렬된 workTime 리스트를 생성
    private List<WorkTime> workTimes_sortedByDuration() {
        List<WorkTime> weeklyWorkTimes_sortedByDuration = new ArrayList<>(weeklyWorkTimeWorkersMap.keySet());
        weeklyWorkTimes_sortedByDuration
                .sort(Comparator.comparingInt(workTime ->
                                -Duration.between(workTime.getStartTime(), workTime.getEndTime()).toMinutesPart()
                        )
                );
        return weeklyWorkTimes_sortedByDuration;
    }


    // 지원자 수가 미달이거나 딱 맞을 때 : 다 넣기
    private void addWorkers_inMinorTime() {
        weeklyWorkTimeWorkersMap.keySet().forEach(workTime -> {
            List<Application> applicationList = workTime.getApplicationList();
            List<User> fixedUserList = new ArrayList<>();

            // 인원수가 미달이거나 딱 맞으면 다 넣기
            if (applicationList.size() <= workTime.getHeadcount()) {
                for (Application application : applicationList) {
                    pushInto_userFixedTimeSum(application);
                    fixedUserList.add(application.getUser());
                }
            }
            weeklyWorkTimeWorkersMap.put(workTime, fixedUserList);
        });
    }

    // 지원자 수 초과된 시간대 : 조건에 맞는 지원자 넣기
    private void addWorker_inMajorTime(WorkTime workTime) {
        List<User> fixedWorkers_ofThisTime = weeklyWorkTimeWorkersMap.get(workTime);
        List<Application> applicationList = workTime.getApplicationList();

        // 해당 시간 이미 채워졌다면
        if (fixedWorkers_ofThisTime.size() == applicationList.size()) {
            return;
        }

        // applicationList : 해당 신청자의 확정근무시간이 적은 순서대로 정렬
        sort_list_by_userFixedTimeSum(applicationList);

        // 확정 근무시간이 적은 순서대로 지원자 채우기
        for (Application application : applicationList) {
            User applicant = application.getUser();

            // 지원자를 해당 시간에 넣기
            if (cannotWork_inThisTime(workTime, applicant)) {
                continue;
            }
            fixedWorkers_ofThisTime.add(applicant);
            pushInto_userFixedTimeSum(application);

            // 다 찼으면 종료
            if (fixedWorkers_ofThisTime.size() == workTime.getHeadcount()) {
                break;
            }
        }
    }

    private void sort_list_by_userFixedTimeSum(List<Application> applicationList) {
        applicationList
                .sort(Comparator.comparingInt(application ->
                        userFixedTimeSum.getOrDefault(application.getUser(), 0))
                );
    }

    // { 유저 : 확정된 총 근무 시간 } map 업데이트
    private void pushInto_userFixedTimeSum(Application application) {
        User applicant = application.getUser();
        // 원래 map 에 있던 시간
        int prevTime = userFixedTimeSum.getOrDefault(applicant, 0);
        // 추가할 시간 계산
        int newTime = (int) Duration.between(
                application.getWorkTime().getStartTime(),
                application.getWorkTime().getEndTime()
        ).toMinutes();
        // map 에 넣기
        userFixedTimeSum.put(applicant, prevTime + newTime);
    }

    // 일 할 수 없는 조건
    private boolean cannotWork_inThisTime(WorkTime thisWorkTime, User applicant) {
        // 이 시간대의 같은날 다른 시간대
        List<WorkTime> sameDateWorkTimeList = weeklyWorkTimeWorkersMap.keySet().stream().filter(time ->
                time.getDate().equals(thisWorkTime.getDate())
        ).toList();

        return sameDateWorkTimeList.stream().anyMatch(workTime -> {
            // 이 시간대(같은날 다른 시간대)에 일 하는지 : 일 안하면 false
            if (!willThisUserWork_inThisTime(applicant, workTime)) {
                return false;
            }
            // 겹치거나 띄엄띄엄이면 true, 연속해서 할 수 있으면 false
            if (isNotContinuous(workTime, thisWorkTime)) {
                return true;
            }
            // 하루 근무 시간이 12시간 이상이면 true
            return isTooLongWork_inTheDay(workTime, thisWorkTime);
        });
    }

    private boolean willThisUserWork_inThisTime(User user, WorkTime workTime) {
        return weeklyWorkTimeWorkersMap.get(workTime).stream().anyMatch(fixedUser ->
                fixedUser.equals(user)
        );
    }
    private boolean isNotContinuous(WorkTime workTime1, WorkTime workTime2) {
        return !workTime1.getEndTime().equals(workTime2.getStartTime()) &&
                !workTime1.getStartTime().equals(workTime2.getEndTime());
    }

    private boolean isTooLongWork_inTheDay(WorkTime workTime1, WorkTime workTime2) {
        return Duration.between(
                workTime1.getStartTime(),
                workTime2.getEndTime()
        ).toMinutes() > 720 ||
                Duration.between(
                        workTime2.getStartTime(),
                        workTime1.getEndTime()
                ).toMinutes() > 720;
    }
}
