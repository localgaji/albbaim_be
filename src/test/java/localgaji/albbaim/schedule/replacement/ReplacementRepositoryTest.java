package localgaji.albbaim.schedule.replacement;

import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.date.DateRepository;
import localgaji.albbaim.schedule.fixed.Fixed;
import localgaji.albbaim.schedule.fixed.FixedRepository;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekRepository;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.schedule.workTime.WorkTimeRepository;
import localgaji.albbaim.user.User;
import localgaji.albbaim.user.UserRepository;
import localgaji.albbaim.workplace.Workplace;
import localgaji.albbaim.workplace.WorkplaceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

import static localgaji.albbaim.__utils__.Samples.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReplacementRepositoryTest {

    @Autowired
    ReplacementRepository replacementRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    WorkplaceRepository workplaceRepository;
    @Autowired
    WorkTimeRepository workTimeRepository;
    @Autowired
    WeekRepository weekRepository;
    @Autowired
    DateRepository dateRepository;
    @Autowired
    FixedRepository fixedRepository;


    @Test
    void findWithPessimisticLock() {

    }

    @Test
    void findByWeekIdAndExpirationTimeBeforeAndHasFoundFalse() {
        // given
        saveReplacement(10);
        Week week = weekRepository.findById(1L).orElseThrow();

        // when
        Slice<Replacement> replacementPage = replacementRepository.findByWeekAndExpirationTimeAfterAndHasFoundFalse(
                week,
                LocalDateTime.now(),
                PageRequest.of(0, 5, Sort.Direction.ASC, "expirationTime")
        );

        // then
        assertThat(replacementPage.getContent().size()).isEqualTo(5);
        assertThat(replacementPage.hasNext()).isTrue();
    }

    private void saveReplacement(int amount) {
        User user = someUser();
        Workplace workplace = someWorkplace();
        Week week = someWeek(workplace);
        Date date = someDate(week);
        WorkTime workTime = someWorkTime(date);

        workplaceRepository.save(workplace);
        userRepository.save(user);
        user.updateWorkplace(workplace);

        weekRepository.save(week);
        week.addWeekToWorkplace();

        dateRepository.save(date);
        date.addDateToWeek();

        workTimeRepository.save(workTime);
        workTime.addWorkTimeToDate();

        for (int i = 1; i <= amount; i++) {
            Fixed fixed = Fixed.builder()
                    .workTime(workTime)
                    .user(user)
                    .build();
            fixedRepository.save(fixed);
            fixed.addNewFixed();

            Replacement replacement = Replacement.builder()
                    .fixed(fixed)
                    .expirationTime(LocalDateTime.of(2030,4,1,0,0, i))
                    .week(week)
                    .build();

            replacementRepository.save(replacement);
            replacement.addReplacementToWeek();
        }
    }
}
