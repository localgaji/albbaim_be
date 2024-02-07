package localgaji.albbaim.schedule.fixed;

import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import org.junit.jupiter.api.Test;

import static localgaji.albbaim.__utils__.Samples.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class FixedTest {

    @Test
    void addNewFixed() {
        // given
        User user = someUser();
        WorkTime workTime = someWorkTime(someDate(someWeek(someWorkplace())));
        Fixed fixed = someFixed(user, workTime);

        // when
        fixed.addNewFixed();

        // then
        assertAll(
                () -> assertThat(user.getFixedList()).contains(fixed),
                () -> assertThat(workTime.getFixedList()).contains(fixed)
        );
    }
}