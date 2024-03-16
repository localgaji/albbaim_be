package localgaji.albbaim.schedule.week;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static localgaji.albbaim.__core__.StringToLocalDate.stringToLocalDate;
import static localgaji.albbaim.schedule.week.DTO.WeekResponse.*;

@Service @RequiredArgsConstructor
public class WeekService {

    private final WeekRepository weekRepository;

    // 주 상태 조회
    public WeekStatusType findWeekStatus(User user, String startWeekDate) {
        // 매장의 week list 중에서 startWeekDate 가 일치하는 Week 조회
        Optional<Week> opt = getWeekListByUser(user)
                .stream()
                .filter(week ->
                        week.getStartWeekDate().equals(stringToLocalDate(startWeekDate))
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
        List<Week> prevWeeks = getWeekListByUser(user);

        // 모집한 적이 한번도 없을 경우
        if (prevWeeks.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(prevWeeks.get(prevWeeks.size() - 1));
    }

    // 해당 유저가 속한 매장의 week list 중, 시작 날짜와 일치하는 주 찾기
    public Week getWeekByStartWeekDate(User user, String startWeekDateString) {
        // 유저의 매장의 주 리스트 조회
        List<Week> weekList = getWeekListByUser(user);

        LocalDate startWeekDate = stringToLocalDate(startWeekDateString);

        // 매장 주 리스트 중 시작 날짜가 일치하는 주를 찾기
        return weekList.stream()
                .filter(week -> week.getStartWeekDate().isEqual(startWeekDate))
                .findAny()
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
    }

    public Optional<Week> findWeekByLocalDate(User user, LocalDate startWeekDate) {
        return getWeekListByUser(user).stream()
                .filter(week -> week.getStartWeekDate().isEqual(startWeekDate))
                .findAny();
    }

    // 해당 유저가 속한 매장의 week list
    private List<Week> getWeekListByUser(User user) {
        return Optional.ofNullable(user.getWorkplace())
                .orElseThrow(() -> new CustomException(ErrorType.FORBIDDEN))
                .getWeekList();
    }
}
