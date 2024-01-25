package localgaji.albbaim.schedule.date;

import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @BeforeEach
    void init() {
        week = someWeek(someWorkplace());
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
}