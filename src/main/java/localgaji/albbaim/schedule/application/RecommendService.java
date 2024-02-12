package localgaji.albbaim.schedule.application;

import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.GetRecommendResponse.*;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final WeekService weekService;

    public GetRecommendResponse getRecommend(User user, String startWeekDate) {
        Week week = weekService.getWeekByStartWeekDate(user, startWeekDate);
        MultiValueMap<WorkTime, User> weeklyWorkTimeWorkersMap = new RecommendLogic(week).getRecommendMap();
        return mapToDto(week, weeklyWorkTimeWorkersMap);
    }

    private GetRecommendResponse mapToDto(Week week,
                                          MultiValueMap<WorkTime, User> weeklyWorkTimeWorkersMap) {

        LocalDate startWeekDate = week.getStartWeekDate();

        List<List<WorkTimeRecommendWorkers>> recommend = IntStream.range(0, 7).mapToObj(i ->
                new ArrayList<WorkTimeRecommendWorkers>()
        ).collect(Collectors.toList());

        weeklyWorkTimeWorkersMap.keySet().forEach(workTime -> {
                    int dayIndex = (int) Duration.between(startWeekDate, workTime.getDate().getLocalDate()).toDays();
                    List<User> fixedUserList = weeklyWorkTimeWorkersMap.get(workTime);
                    WorkTimeRecommendWorkers dto = new WorkTimeRecommendWorkers(
                            workTime,
                            fixedUserList.stream().map(Worker::new).toList()
                    );
                    recommend.get(dayIndex).add(dto);
                }
        );
        return new GetRecommendResponse(recommend);
    }
}
