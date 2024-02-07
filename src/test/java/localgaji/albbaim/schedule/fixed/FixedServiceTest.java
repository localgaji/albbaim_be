package localgaji.albbaim.schedule.fixed;

import localgaji.albbaim.__utils__.Samples;
import localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.date.DateService;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.schedule.workTime.WorkTimeService;
import localgaji.albbaim.user.User;
import localgaji.albbaim.user.UserService;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static localgaji.albbaim.__utils__.Samples.*;
import static localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedRequest.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedRequest.PostFixRequest.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedResponse.GetMonthlyResponse.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeRequest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FixedServiceTest {
    @InjectMocks
    private FixedService fixedService;
    @Mock
    private FixedRepository fixedRepository;
    @Mock
    private WorkTimeService workTimeService;
    @Mock
    private UserService userService;
    @Mock
    private WeekService weekService;
    @Mock
    private DateService dateService;

    private final User user = Samples.someUser();
    private final PostOpenRequest open = postOpenRequest();
    private Week week;

    @BeforeEach
    void init() {
        Workplace workplace = someWorkplace();

        week = open.toWeekEntity(workplace);
        List<Date> dateList = open.toDateEntities(week);
        List<List<WorkTime>> workTimeListWeekly = dateList.stream().map(open::toWorkTimeEntities).toList();
        dateList.forEach(Date::addDateToWeek);
        workTimeListWeekly.forEach(d ->
                d.forEach(WorkTime::addWorkTimeToDate));
    }

    @Test
    void saveFixed() {
        // given
        List<List<WorkTimeWorkers>> weeklyWorkerListWannaFix = week.getDateList().stream().map(date ->
                date.getWorkTimeList().stream().map(workTime ->
                        new WorkTimeWorkers(workTime.getWorkTimeId(), List.of(new Worker(user)))
                ).toList()
        ).toList();
        PostFixRequest request = new PostFixRequest(weeklyWorkerListWannaFix);

        when(weekService.getWeekByStartWeekDate(user, open.startWeekDate()))
                .thenReturn(week);
        when(workTimeService.findWorkTimeById(any())).thenReturn(someWorkTime(someDate(week)));
        when(userService.findUserById(any())).thenReturn(user);

        // when
        fixedService.saveFixed(user, open.startWeekDate(), request);

        // then
        verify(fixedRepository, times(21)).save(any(Fixed.class));
    }

    @Test
    void getMonthlyFixed() {
        // given
        week.getDateList().forEach(date ->
                date.getWorkTimeList().forEach(workTime -> {
                    Fixed fixed = someFixed(user, workTime);
                    fixed.addNewFixed();
                })
        );
        week.fixWeekly();

        when(weekService.getWeekByLocalDate(any(), any()))
                .thenReturn(Optional.of(week));
        // when
        List<List<DailySchedule>> monthly = fixedService.getMonthlyFixed(
                user,
                week.getStartWeekDate().getYear(),
                week.getStartWeekDate().getMonthValue()
        ).monthly();

        // then
        assertThat(monthly.get(0).get(0).workTimes().size()).isEqualTo(3);
    }

    @Test
    void getDailyWorkers() {
        // given
        User user1 = someUser();
        User user2 = someUser();
        week.getDateList().forEach(date ->
                date.getWorkTimeList().forEach(workTime -> {
                    Fixed fixed1 = someFixed(user1, workTime);
                    Fixed fixed2 = someFixed(user2, workTime);
                    fixed1.addNewFixed();
                    fixed2.addNewFixed();
                })
        );
        week.fixWeekly();
        when(dateService.findByLocalDate(any(), any()))
                .thenReturn(Optional.of(week.getDateList().get(0)));

        // when
        List<WorkerListDTO> schedule = fixedService.getDailyWorkers(
                user,
                week.getStartWeekDate().toString()
        ).schedule();

        // then
        assertAll(
                () -> assertThat(schedule.size()).isEqualTo(3),
                () -> assertThat(schedule.get(0).getWorkerList().size()).isEqualTo(2)
        );
    }
}