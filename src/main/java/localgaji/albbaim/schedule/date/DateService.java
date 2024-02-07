package localgaji.albbaim.schedule.date;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class DateService {
    private final DateRepository dateRepository;

    // date 엔티티 저장
    @Transactional
    public void createNewDate(Date date) {
        dateRepository.save(date);
        date.addDateToWeek();
    }

    public Optional<Date> findByLocalDate(User user, LocalDate findDate) {
        Workplace workplace = Optional.ofNullable(user.getWorkplace())
                .orElseThrow(() -> new CustomException(ErrorType.FORBIDDEN));

        for (Week week : workplace.getWeekList()) {
            for (Date date : week.getDateList()) {
                if (date.getLocalDate().equals(findDate)) {
                    return Optional.of(date);
                }
            }
        }
        return Optional.empty();
    }
}
