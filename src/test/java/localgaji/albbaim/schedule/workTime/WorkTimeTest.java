package localgaji.albbaim.schedule.workTime;

import localgaji.albbaim.schedule.date.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static localgaji.albbaim.__utils__.Samples.*;
import static org.assertj.core.api.Assertions.*;

class WorkTimeTest {

    Date date;
    WorkTime workTime;

    @BeforeEach
    void set() {
        date = someDate(someWeek(someWorkplace()));
        workTime = someWorkTime(date);
    }

    @DisplayName("date 업데이트")
    @Test
    void addWorkTimeToDate() {
        // given
        Date date = someDate(someWeek(someWorkplace()));
        WorkTime workTime = someWorkTime(date);

        // when
        workTime.addWorkTimeToDate();

        // then
        assertThat(date.getWorkTimeList().get(0)).isSameAs(workTime);
    }

}