package localgaji.albbaim.schedule.week;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static localgaji.albbaim.__utils__.Samples.*;
import static localgaji.albbaim.schedule.week.DTO.WeekResponse.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WeekServiceTest {
    @InjectMocks
    private WeekService weekService;
    @Mock
    private WeekRepository weekRepository;

    User user;
    String startWeekDate;
    Workplace workplace;

    @BeforeEach
    void before() {
        user = someUser();
        startWeekDate = "2030-04-01";
        workplace = someWorkplace();
        user.updateWorkplace(workplace);
    }

    @DisplayName("주차 상태 확인 : allocatable")
    @Test
    void findWeekStatus_allocatable() {
        // when
        WeekStatusType weekStatusType = weekService.findWeekStatus(user, startWeekDate);

        // then
        assertThat(weekStatusType).isEqualTo(WeekStatusType.ALLOCATABLE);
    }

    @DisplayName("주차 상태 확인 : inProgress")
    @Test
    void findWeekStatus_inProgress() {
        // given
        Week week = someWeek(workplace);
        week.addWeekToWorkplace();

        // when
        WeekStatusType weekStatusType = weekService.findWeekStatus(user, startWeekDate);

        // then
        assertThat(weekStatusType).isEqualTo(WeekStatusType.IN_PROGRESS);
    }

    @DisplayName("주차 상태 확인 : closed")
    @Test
    void findWeekStatus_closed() {
        // given
        Week week = someWeek(workplace);
        week.addWeekToWorkplace();
        week.fixWeekly();

        // when
        WeekStatusType weekStatusType = weekService.findWeekStatus(user, startWeekDate);

        // then
        assertThat(weekStatusType).isEqualTo(WeekStatusType.CLOSED);
    }

    @DisplayName("새로운 week 엔티티 저장")
    @Test
    void createNewWeek() {
        // given
        Week week = someWeek(workplace);

        // when
        weekService.createNewWeek(week);

        // then
        verify(weekRepository).save(week);
        assertThat(workplace.getWeekList()).contains(week);
    }

    @DisplayName("가장 최근 모집한 주 : 한번도 없을 때")
    @Test
    void getLastWeek_nothing() {
        // given

        // when
        Optional<Week> optWeek = weekService.getLastWeek(user);

        // then
        assertThat(optWeek).isEmpty();
    }

    @DisplayName("가장 최근 모집한 주 : 1개 있을 때")
    @Test
    void getLastWeek_isOne() {
        // given
        Week week = someWeek(workplace);
        week.addWeekToWorkplace();

        // when
        Optional<Week> optWeek = weekService.getLastWeek(user);

        // then
        assertThat(optWeek)
                .isPresent()
                .contains(week);
    }

    @DisplayName("가장 최근 모집한 주 : 여러개 있을 때")
    @Test
    void getLastWeek_isMany() {
        // given
        Week week1 = someWeek(workplace);
        week1.addWeekToWorkplace();
        Week week2 = someWeek(workplace);
        week2.addWeekToWorkplace();

        // when
        Optional<Week> optWeek = weekService.getLastWeek(user);

        // then
        assertThat(optWeek)
                .isPresent()
                .contains(week2);
    }

    @DisplayName("string date 로 Week 찾기")
    @Test
    void getWeekByStartWeekDate() {
        // given
        Week week = someWeek(workplace);
        week.addWeekToWorkplace();

        // when
        Week foundWeek = weekService.getWeekByStartWeekDate(user, week.getStartWeekDate().toString());

        // then
        assertThat(foundWeek).isEqualTo(week);
    }

    @DisplayName("string date 로 Week 찾기 실패")
    @Test
    void getWeekByStartWeekDate_fail() {
        // given
        Week week = someWeek(workplace);

        // when, then
        Assertions.assertThatThrownBy(
                () -> weekService.getWeekByStartWeekDate(user, week.getStartWeekDate().toString())
        ).isInstanceOf(CustomException.class);
    }
}