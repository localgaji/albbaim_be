package localgaji.albbaim.schedule.fixed;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static localgaji.albbaim.__core__.StringToLocalDate.*;
import static localgaji.albbaim.schedule.__commonDTO__.WorkerListDTO.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedRequest.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedRequest.PostFixRequest.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedResponse.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedResponse.GetMonthlyResponse.*;

@Service @RequiredArgsConstructor
public class FixedService {

    private final FixedRepository fixedRepository;
    private final WorkTimeService workTimeService;
    private final UserService userService;
    private final WeekService weekService;
    private final DateService dateService;

    // 저장 (추천에서)
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

    // 월간 확정 스케줄 조회
    public GetMonthlyResponse getMonthlyFixed(User user, Integer year, Integer month) {
        // 시작일이 포함된 주의 첫 일
        LocalDate firstStartWeekDate = getFirstStartWeekDate(year, month);

        // for : dateservice에서 date 가져오기 -> 근무 정보 담기, total 계산
        List<List<DailySchedule>> monthly = new ArrayList<>();

        LocalDate startWeekDate = firstStartWeekDate;

        while (startWeekDate.getMonthValue() <= month && startWeekDate.getYear() <= year) {
            List<DailySchedule> weekly = new ArrayList<>();

            Optional<Week> optWeek = weekService.getWeekByLocalDate(user, startWeekDate);

            for (int d=0; d<7; d++) {
                LocalDate nowDate = startWeekDate.plusDays(d);

                if (optWeek.isEmpty() || !optWeek.get().getHasFixed()) {
                    weekly.add(new DailySchedule(nowDate.toString(), false, new ArrayList<>()));
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
            monthly.add(weekly);
            startWeekDate = startWeekDate.plusDays(7);
        }

        TotalWorkTime totalWorkTime = new TotalWorkTime(0);

        return new GetMonthlyResponse(monthly, totalWorkTime);
    }

    private LocalDate getFirstStartWeekDate(int year, int month) {
        // 달의 시작일 구하기
        LocalDate firstDate = LocalDate.of(year, month, 1);
        // 시작일의 요일 (월 ~ 일 : 1 ~ 7)
        int firstDateDay = firstDate.getDayOfWeek().getValue();
        // 시작일이 포함된 주의 첫 일
        return firstDate.minusDays(firstDateDay - 1);
    }

    // 일간 확정 근무자 조회
    public GetDailyWorkersResponse getDailyWorkers(User user, String selectedDate) {
        // 일 entity 조회
        Date date = dateService.findByLocalDate(user, stringToLocalDate(selectedDate))
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        // date -> workTime -> fixed 조회
        List<WorkerListDTO> schedule = date.getWorkTimeList().stream().map(workTime -> {
            List<Worker> workerList = workTime.getFixedList().stream().map(u ->
                    new Worker(u.getUser())
            ).toList();
            return new WorkerListDTO(workTime, workerList);
        }).collect(Collectors.toList());

        return new GetDailyWorkersResponse(schedule);
    }

}
