package localgaji.albbaim.schedule.week;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static localgaji.albbaim.schedule.week.DTO.WeekResponse.*;

@Service @RequiredArgsConstructor
public class WeekService {

    private final WeekRepository weekRepository;

    // 주 상태 조회
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

    // 새로운 week 엔티티 저장
    @Transactional
    public void createNewWeek(Week week) {
        week.addWeekToWorkplace();
        weekRepository.save(week);
    }

    // 가장 최근 모집했던 주 조회
    public Optional<Week> getLastWeek(User user) {
        List<Week> prevWeeks = Optional.ofNullable(user.getWorkplace())
                .orElseThrow(() -> new CustomException(ErrorType.FORBIDDEN))
                .getWeekList();
        // 모집한 적이 한번도 없을 경우
        if (prevWeeks.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(prevWeeks.get(prevWeeks.size() - 1));
    }
}
