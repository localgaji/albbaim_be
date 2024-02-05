package localgaji.albbaim.schedule.application;

import localgaji.albbaim.schedule.__commonDTO__.WorkTimeWorkerListDTO;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.schedule.workTime.WorkTimeService;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.IntStream;

import static localgaji.albbaim.__utils__.Samples.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationRequest.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.GetApplyFormResponse.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeRequest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @InjectMocks
    private ApplicationService applicationService;
    @Mock
    private WeekService weekService;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private WorkTimeService workTimeService;

    private User user;
    private Week week;

    @BeforeEach
    void init () {
        user = someUser();
        Workplace workplace = someWorkplace();

        PostOpenRequest open = postOpenRequest();

        week = open.toWeekEntity(workplace);
        List<Date> dateList = open.toDateEntities(week);
        List<List<WorkTime>> workTimeListWeekly = dateList.stream().map(open::toWorkTimeEntities).toList();
        dateList.forEach(Date::addDateToWeek);
        workTimeListWeekly.forEach(d ->
                d.forEach(WorkTime::addWorkTimeToDate));

        week.getDateList().forEach(daily ->
                daily.getWorkTimeList().forEach(wt ->
                        IntStream.range(0, 1).forEach(a -> {
                            Application newApp = Application.builder()
                                    .user(user)
                                    .workTime(wt)
                                    .build();
                            newApp.applyToWorkTime();
                        })
                )
        );
    }

    @DisplayName("해당 주차의 시간대 별 지원자 리스트 조회")
    @Test
    void getApplicationList() {
        // given
        when(weekService.getWeekByStartWeekDate(any(), any())).thenReturn(week);

        // when
        List<List<WorkTimeWorkerListDTO>> applyStatus = applicationService.getApplicationList(someUser(),
                week.getStartWeekDate().toString())
                .applyStatus();

        // then
        assertAll(
                () -> assertThat(applyStatus.size()).isEqualTo(7),
                () -> assertThat(applyStatus.get(0).size()).isEqualTo(3),
                () -> assertThat(applyStatus.get(0).get(0).getWorkerList().size()).isEqualTo(1)
        );
    }

    @DisplayName("해당 주차의 시간대 신청 선택지 조회 : 기존 아무것도 선택 안했을때")
    @Test
    void getApplicationCheckForm_nothingChecked() {
        // given
        when(weekService.getWeekByStartWeekDate(any(), any())).thenReturn(week);

        // when
        List<List<WorkTimeChoice>> selected = applicationService
                .getApplicationCheckForm(someUser(), week.getStartWeekDate().toString())
                .selected();

        // then
        assertAll(
                () -> assertThat(selected.size()).isEqualTo(7),
                () -> assertThat(selected.get(0).size()).isEqualTo(3),
                () -> assertThat(selected.get(0).get(0).getIsChecked()).isFalse()
        );
    }

    @DisplayName("해당 주차의 시간대 신청 선택지 조회 : 이미 선택한 항목일때")
    @Test
    void getApplicationCheckForm_alreadyCheckedAll() {
        // given
        when(weekService.getWeekByStartWeekDate(any(), any())).thenReturn(week);

        // when
        List<List<WorkTimeChoice>> selected = applicationService
                .getApplicationCheckForm(user, week.getStartWeekDate().toString())
                .selected();

        // then
        assertAll(
                () -> assertThat(selected.size()).isEqualTo(7),
                () -> assertThat(selected.get(0).size()).isEqualTo(3),
                () -> assertThat(selected.get(0).get(0).getIsChecked()).isTrue()
        );
    }

    @DisplayName("해당 주차의 신청 선택 여부 저장 : 모두 해제 -> 모두 체크")
    @Test
    void saveApplication_checkAll() {
        // given
        List<List<PostApplyRequest.HasWorkTimeChecked>> apply =
                week.getDateList().stream().map(d ->
                        d.getWorkTimeList().stream().map(wt ->
                                new PostApplyRequest.HasWorkTimeChecked(wt.getWorkTimeId(), true)
                        ).toList()
                ).toList();

        PostApplyRequest request = new PostApplyRequest(week.getStartWeekDate().toString(), apply);
        WorkTime workTime = week.getDateList().get(0).getWorkTimeList().get(0);
        when(workTimeService.findWorkTimeById(any())).thenReturn(workTime);

        // when
        applicationService.saveApplication(someUser(), request);

        // then
        verify(applicationRepository).save(any());
    }

    @DisplayName("해당 주차의 신청 선택 여부 저장 : 모두 체크 -> 모두 해제")
    @Test
    void saveApplication_check_cancel() {
        // given
        List<List<PostApplyRequest.HasWorkTimeChecked>> apply =
                week.getDateList().stream().map(d ->
                        d.getWorkTimeList().stream().map(wt ->
                                new PostApplyRequest.HasWorkTimeChecked(wt.getWorkTimeId(), false)
                        ).toList()
                ).toList();

        PostApplyRequest request = new PostApplyRequest(week.getStartWeekDate().toString(), apply);
        WorkTime workTime = week.getDateList().get(0).getWorkTimeList().get(0);
        when(workTimeService.findWorkTimeById(any())).thenReturn(workTime);

        // when
        applicationService.saveApplication(user, request);

        // then
        verify(applicationRepository).delete(any());
    }
}