package localgaji.albbaim.schedule.week;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static localgaji.albbaim.schedule.__DTO__.ResponseSchedule.*;

@Service @RequiredArgsConstructor
public class WeekService {
    public WeekStatusType findWeekStatus(User user, String startWeekDate) {
        // 소속 매장 조회
        Workplace workplace = Optional.ofNullable(user.getWorkplace())
                .orElseThrow(() -> new CustomException(ErrorType.FORBIDDEN));

        // 매장의 week list 중에서 startWeekDate 가 일치하는 Week 조회
        Optional<Week> opt = workplace.getWeekList().stream()
                .filter(week ->
                        week.getStartWeekDate().equals(LocalDate.parse(startWeekDate, DateTimeFormatter.ISO_DATE))
                ).findAny();

        // startWeekDate 와 일치하는 주가 없을 때
        if (opt.isEmpty()) {
            return WeekStatusType.ALLOCATABLE;
        }
        // 마감되었을 때
        if (opt.get().getHasFixed()) {
            return WeekStatusType.CLOSED;
        }
        // 모집 중일 때
        return WeekStatusType.IN_PROGRESS;
    }
}
