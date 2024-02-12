package localgaji.albbaim.schedule.application;

import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.IntStream;

import static localgaji.albbaim.__utils__.Samples.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.GetRecommendResponse.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeRequest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendServiceTest {
    @InjectMocks
    private RecommendService recommendService;
    @Mock
    private WeekService weekService;

    private final User user = someUser();
    private Week week;

    @BeforeEach
    void init() {
        Workplace workplace = someWorkplace();
        user.updateWorkplace(workplace);

        PostOpenRequest open = postOpenRequest();

        week = open.toWeekEntity(workplace);
        List<Date> dateList = open.toDateEntities(week);
        List<List<WorkTime>> workTimeListWeekly = dateList.stream().map(open::toWorkTimeEntities).toList();
        dateList.forEach(Date::addDateToWeek);
        workTimeListWeekly.forEach(d ->
                d.forEach(WorkTime::addWorkTimeToDate));

        List<User> userList = IntStream.range(0, 50).mapToObj(i -> User.builder()
                .userId((long) i + 1)
                .userName("알바" + (i + 1))
                .isAdmin(false)
                .build()
        ).toList();

        week.getDateList().forEach(daily ->
                daily.getWorkTimeList().forEach(wt ->
                        userList.forEach(someUser -> {
                            Application newApp = Application.builder()
                                    .user(someUser)
                                    .workTime(wt)
                                    .build();
                            newApp.applyToWorkTime();
                        })
                )
        );
    }

    @Test
    void getRecommend() {
        // given
        when(weekService.getWeekByStartWeekDate(any(), any())).thenReturn(week);

        // when
        List<List<WorkTimeRecommendWorkers>> recommends =
                recommendService.getRecommend(user, week.getStartWeekDate().toString())
                        .recommends();

        // then
        assertAll(
                () -> recommends.forEach(daily ->
                        daily.forEach(workTimeWorkers ->
                                assertThat(workTimeWorkers.getWorkerList().size()).isEqualTo(10)
                        )
                )
        );
    }
}