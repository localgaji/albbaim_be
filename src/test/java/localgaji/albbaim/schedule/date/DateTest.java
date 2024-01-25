package localgaji.albbaim.schedule.date;

import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static localgaji.albbaim.__utils__.Samples.*;
import static org.assertj.core.api.Assertions.*;

class DateTest {
    Week week;
    Workplace workplace;
    Date date ;

    @BeforeEach
    void init() {
        workplace = someWorkplace();
        week = someWeek(workplace);
        date = someDate(week);
    }

    @Test
    void addDateToWeek() {
        // when
        date.addDateToWeek();

        // then
        assertThat(week.getDateList()).contains(date);
    }
}