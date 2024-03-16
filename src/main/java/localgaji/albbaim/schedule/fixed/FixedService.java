package localgaji.albbaim.schedule.fixed;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.__core__.WeekDay;
import localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.date.DateService;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.schedule.workTime.WorkTimeService;
import localgaji.albbaim.user.User;
import localgaji.albbaim.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static localgaji.albbaim.__core__.StringToLocalDate.*;
import static localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedRequest.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedResponse.*;

@Service @RequiredArgsConstructor
public class FixedService {

    private final FixedRepository fixedRepository;
    private final WorkTimeService workTimeService;
    private final UserService userService;
    private final WeekService weekService;
    private final DateService dateService;
    private static final int WEEK_DATES_COUNT = 7;

    /** 저장 (추천에서) */
    @Transactional
    public void saveFixed(User user, String startWeekDate, PostFixRequest request) {
        // hasFixed 바꾸기
        Week week = weekService.getWeekByStartWeekDate(user, startWeekDate);
        week.fixWeekly();

        // fixed entity 저장
        List<List<WorkTimeWorkers>> schedule = request.weeklyWorkerListWannaFix();
        schedule.forEach(daily ->
                daily.forEach(time -> {
                        WorkTime workTime = workTimeService.findWorkTimeById(time.getWorkTimeId());

                        // week 안에 있는 workTime 맞는지 확인

                        // 저장
                        for (Worker worker : time.getWorkerList()) {
                            User userWorker = userService.findUserById(worker.userId());
                            Fixed fixed = Fixed.builder()
                                    .user(userWorker)
                                    .workTime(workTime)
                                    .build();
                            fixedRepository.save(fixed);
                            fixed.addNewFixed();
                        }
                })
        );
    }

    /** 일간 확정 근무자 조회 */
    public GetDailyWorkersResponse getDailyWorkers(User user, String selectedDate) {
        // 일 entity 조회
        Date date = dateService.findByLocalDate(user, stringToLocalDate(selectedDate))
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        // 확정되지 않은 일일 때
        if (!date.getWeek().getHasFixed()) {
            throw new CustomException(ErrorType.NOT_FOUND);
        }

        // date -> workTime -> fixed 조회
        List<WorkerListDTO> schedule = date.getWorkTimeList().stream().map(workTime -> {
            List<Worker> workerList = workTime.getFixedList().stream().map(u ->
                    new Worker(u.getUser())
            ).toList();
            return new WorkerListDTO(workTime, workerList);
        }).collect(Collectors.toList());

        return new GetDailyWorkersResponse(schedule);
    }

    /** 월간 확정 스케줄 조회 */
    public GetMonthlyResponse getMonthlyFixed(User user, Integer year, Integer month) {
        // 달력의 시작일 (첫번째 월요일)
        LocalDate firstDateOfMonthly = getFirstStartWeekDate(year, month);

        // 주 리스트 생성
        List<LocalDate> weeksOfThisMonth = new ArrayList<>();
        int week = 0;
        while (is_week_in_month(firstDateOfMonthly.plusWeeks(week), year, month)) {
            LocalDate startWeekDate = firstDateOfMonthly.plusWeeks(week);
            weeksOfThisMonth.add(startWeekDate);
            week++;
        }

        // 스케줄 담기
        List<List<DailySchedule>> monthly = weeksOfThisMonth.stream().map(swd ->
                weekly(user, swd)
        ).toList();

        return new GetMonthlyResponse(monthly);
    }

    private boolean is_week_in_month(LocalDate startWeekDate, int year, int month) {
        LocalDate lastDateOfMonth = LocalDate.of(year, month, 1)
                .plusMonths(1)
                .minusDays(1);
        return startWeekDate.isBefore(lastDateOfMonth);
    }

    private List<DailySchedule> weekly(User user, LocalDate startWeekDate) {
        List<DailySchedule> weekly = new ArrayList<>();

        for (WeekDay day : WeekDay.values()) {
            LocalDate nowDate = getLocalDateByDay(startWeekDate, day);

            if (has_not_fixed(user, startWeekDate)) {
                weekly.add(emptyDailySchedule(nowDate));
                continue;
            }

            // user FixedList -> 해당일에 속한 time만 찾아서 -> 가공
            List<String> dailyWorkTimeList = user.getFixedList().stream()
                    .filter(fixed ->
                            fixed.getWorkTime().getDate().getLocalDate().isEqual(nowDate)
                    ).map(fixed ->
                            fixed.getWorkTime().getWorkTimeName()
                    ).toList();

            weekly.add(new DailySchedule(nowDate.toString(), true, dailyWorkTimeList));
        }
        return weekly;
    }

    private DailySchedule emptyDailySchedule(LocalDate date) {
        return new DailySchedule(
                date.toString(),
                false,
                new ArrayList<>()
        );
    }

    private boolean has_not_fixed(User user, LocalDate swd) {
        Optional<Week> optWeek = weekService.findWeekByLocalDate(user, swd);
        return optWeek.isEmpty() || !optWeek.get().getHasFixed();
    }

    private LocalDate getLocalDateByDay(LocalDate startWeekDate, WeekDay day) {
        return startWeekDate.plusDays(day.getIndex());
    }

    private LocalDate getFirstStartWeekDate(int year, int month) {
        int FIRST_DATE_OF_MONTH = 1;
        // 달의 시작일 구하기
        LocalDate firstDate = LocalDate.of(year, month, FIRST_DATE_OF_MONTH);
        // 시작일의 요일 (월 ~ 일 : 1 ~ 7)
        int firstDateDay = firstDate.getDayOfWeek().getValue();
        // 달력의 시작일 (첫번째 월요일)
        return firstDate.minusDays(firstDateDay - 1);
    }

    public GetMonthlyResponse getMonthlyFixed1(User user, Integer year, Integer month) {
        // 달력의 시작일 (첫번째 월요일)
        LocalDate firstOfDate = getFirstStartWeekDate(year, month);

        List<List<DailySchedule>> monthly = new ArrayList<>();

        LocalDate startWeekDate = firstOfDate;
        while (is_week_in_month(startWeekDate, year, month)) {
            monthly.add(weekly(user, startWeekDate));
            startWeekDate = startWeekDate.plusDays(WEEK_DATES_COUNT);
        }

        return new GetMonthlyResponse(monthly);
    }
    private List<DailySchedule> weekly1(User user, LocalDate startWeekDate) {
        if (has_not_fixed(user, startWeekDate)) {
            return Arrays.stream(WeekDay.values()).map(day ->
                    emptyDailySchedule(getLocalDateByDay(startWeekDate, day))
            ).toList();
        }

        return Arrays.stream(WeekDay.values()).map(day -> {
            LocalDate nowDate = getLocalDateByDay(startWeekDate, day);

            List<String> dailyWorkTimeList = user.getFixedList().stream()
                    .filter(fixed ->
                            fixed.getWorkTime().getDate().getLocalDate().isEqual(nowDate)
                    ).map(fixed ->
                            fixed.getWorkTime().getWorkTimeName()
                    ).toList();

            return new DailySchedule(nowDate.toString(), true, dailyWorkTimeList);

        }).toList();
    }

}
