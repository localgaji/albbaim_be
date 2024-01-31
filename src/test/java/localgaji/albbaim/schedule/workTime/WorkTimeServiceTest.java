package localgaji.albbaim.schedule.workTime;

import localgaji.albbaim.schedule.__commonDTO__.WorkTimeDTO;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.date.DateService;
import localgaji.albbaim.schedule.week.DTO.WeekResponse;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.DTO.WorkTimeHeadCountDTO;
import localgaji.albbaim.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static localgaji.albbaim.__utils__.Samples.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeRequest.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeRequest.PostOpenRequest.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeResponse.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkTimeServiceTest {
    @InjectMocks
    private WorkTimeService workTimeService;
    @Mock
    private WorkTimeRepository workTimeRepository;
    @Mock
    private DateService dateService;
    @Mock
    private WeekService weekService;

    private User user;

    @BeforeEach
    void init() {
        user = someUser();
        user.updateWorkplace(someWorkplace());
    }

    @DisplayName("시간대 정보 저장")
    @Test
    void saveWorkTimes() {
        // given
        PostOpenRequest request = postOpenRequest();

        when(weekService.findWeekStatus(any(), any())).thenReturn(WeekResponse.WeekStatusType.ALLOCATABLE);

        // when
        workTimeService.saveWorkTimes(user, request);

        // then
        assertAll(
                () -> verify(workTimeRepository, times(7)).save(any(WorkTime.class)),
                () -> verify(dateService, times(7)).createNewDate(any(Date.class)),
                () -> verify(weekService).createNewWeek(any(Week.class))
        );
    }

    @DisplayName("최근 저장 템플릿 가져오기 : 없을 때")
    @Test
    void getLastWorkTimeNoLast() {
        // given
        when(weekService.getLastWeek(any()))
                .thenReturn(Optional.empty());

        // when
        GetTemplateResponse response = workTimeService.getLastWorkTimeTemplate(user);
        List<List<WorkTimeHeadCountDTO>> template = response.template();

        // then
        assertAll(
                () -> assertThat(template.size()).isEqualTo(7),
                () -> assertThat(template.get(0).size()).isEqualTo(1),
                () -> assertThat(template.get(0).get(0).getTitle()).isEqualTo("오픈")
        );
    }


    @DisplayName("최근 저장 템플릿 가져오기 : 있을 때")
    @Test
    void getLastWorkTimeHaveLast() {
        // given
        Week week = someWeek(someWorkplace());
        for (int i = 0; i < 7; i++) {
            Date date = someDate(week);
            date.addDateToWeek();

            WorkTime workTime = someWorkTime(date);
            workTime.addWorkTimeToDate();
        }

        when(weekService.getLastWeek(any()))
                .thenReturn(Optional.of(week));

        // when
        GetTemplateResponse response = workTimeService.getLastWorkTimeTemplate(user);
        List<List<WorkTimeHeadCountDTO>> template = response.template();

        // then
        assertAll(
                () -> assertThat(template.size()).isEqualTo(7),
                () -> assertThat(template.get(0).size()).isEqualTo(1),
                () -> assertThat(template.get(0).get(0).getTitle()).isEqualTo("샘플")
        );
    }
    private PostOpenRequest postOpenRequest() {
        WorkTimeHeadCountDTO dto = WorkTimeHeadCountDTO.builder()
                .title("미들")
                .startTime("09:00")
                .endTime("12:00")
                .headCount(10)
                .build();

        List<List<WorkTimeHeadCountDTO>> template = Stream.generate(() ->
                        new ArrayList<>(Collections.singletonList(dto)))
                .limit(7)
                .collect(Collectors.toList());
        return new PostOpenRequest("2030-04-01", template);
    }
}