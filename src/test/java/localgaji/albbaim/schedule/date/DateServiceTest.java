package localgaji.albbaim.schedule.date;

import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static localgaji.albbaim.__utils__.Samples.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DateServiceTest {
    @InjectMocks
    private DateService dateService;
    @Mock
    private DateRepository dateRepository;

    private Week week;
    private Workplace workplace;

    @BeforeEach
    void init() {
        workplace = someWorkplace();
        week = someWeek(workplace);
        week.addWeekToWorkplace();
    }

    @Test
    void createNewDate() {
        // given
        Date date = someDate(week);

        // when
        dateService.createNewDate(date);

        // then
        verify(dateRepository).save(date);
        assertThat(week.getDateList()).contains(date);
    }

    @Test
    void findByLocalDate() {
        // given
        User user = someUser();
        user.updateWorkplace(workplace);
        Date date = someDate(week);
        date.addDateToWeek();

        // when
        Optional<Date> foundDate = dateService.findByLocalDate(user, date.getLocalDate());

        // then
        assertThat(foundDate.orElseThrow().getLocalDate()).isEqualTo(date.getLocalDate());
    }
}