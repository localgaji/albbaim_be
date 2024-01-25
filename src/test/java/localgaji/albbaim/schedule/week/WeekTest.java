package localgaji.albbaim.schedule.week;

import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static localgaji.albbaim.__utils__.Samples.*;
import static org.assertj.core.api.Assertions.*;

class WeekTest {
    Week week;
    Workplace workplace;

    @BeforeEach
    void init() {
        workplace = someWorkplace();
        week = someWeek(workplace);
    }

    @Test
    void fixWeekly() {
        // when
        week.fixWeekly();

        // then
        assertThat(week.getHasFixed()).isTrue();
    }

    @Test
    void addWeekToWorkplace() {
        // when
        week.addWeekToWorkplace();

        // then
        assertThat(workplace.getWeekList()).contains(week);
    }
}