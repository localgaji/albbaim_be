package localgaji.albbaim.schedule.application;

import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static localgaji.albbaim.__utils__.Samples.*;
import static org.assertj.core.api.Assertions.*;

class ApplicationTest {
    private User user;
    private WorkTime workTime;
    private Application application;

    @BeforeEach
    void init () {
        user = someUser();
        Workplace workplace = someWorkplace();
        Week week = someWeek(workplace);
        Date date = someDate(week);
        workTime = someWorkTime(date);
        application = someApplication(user, workTime);
    }

    @DisplayName("workTime application list에 추가")
    @Test
    void applyToWorkTime() {
        // given

        // when
        application.applyToWorkTime();

        // then
        assertThat(workTime.getApplicationList()).contains(application);
    }

    @DisplayName("workTime application list에서 삭제")
    @Test
    void deleteToWorkTime() {
        // given
        application.applyToWorkTime();

        // when
        application.deleteInWorkTime();

        // then
        assertThat(workTime.getApplicationList()).doesNotContain(application);
    }
}