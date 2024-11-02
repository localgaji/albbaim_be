package localgaji.albbaim.schedule.replacement;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.schedule.fixed.Fixed;
import localgaji.albbaim.schedule.fixed.FixedRepository;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.week.WeekService;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static localgaji.albbaim.schedule.replacement.DTO.ReplacementResponse.*;

@Service @RequiredArgsConstructor
public class ReplacementService {

    private final FixedRepository fixedRepository;
    private final ReplacementRepository replacementRepository;
    private final WeekService weekService;

    /** 모집 중인 대타 리스트 (주별) */
    public GetReplacementList getReplacementList(User user, String startWeekDate, int page) {
        Week week = weekService.getWeekByStartWeekDate(user, startWeekDate);

        Slice<Replacement> replacementPage = replacementRepository.findByWeekFetchJoin(
                week,
                LocalDateTime.now(),
                PageRequest.of(page - 1, 5, Sort.Direction.ASC, "expirationTime")
        );

        List<ReplacementInfo> replacementInfoList = replacementPage.getContent().stream()
                .map(ReplacementInfo::new).toList();

        return new GetReplacementList(replacementInfoList, replacementPage.getNumber(), replacementPage.hasNext());
    }

    /** 해당 스케줄의 직원 변경 (대타) */
    @Transactional
    public void changeWorker(User newUser, String replacementIdString) {
        Replacement replacement = replacementRepository.findWithPessimisticLock(
                Long.parseLong(replacementIdString)
        ).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        // 대타 모집중이 아닐 때
        if (replacement.getHasFound()) {
            throw new CustomException(ErrorType.FORBIDDEN);
        }

        Fixed fixed = replacement.getFixed();

        // 대타 모집중이 아닐 때
        if (!fixed.getIsFindingReplacement()) {
            throw new CustomException(ErrorType.FORBIDDEN);
        }

        // 마감
        replacement.findReplacement();

        // 직원 변경
        fixed.changeUser(newUser);
    }

    /** 대타 요청하기 (저장) */
    @Transactional
    public void findReplacementWorker(User user, String fixedIdString) {
        Fixed fixed = fixedRepository.findById(
                Long.parseLong(fixedIdString)
        ).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
        Week week = fixed.getWorkTime().getDate().getWeek();

        // 본인 스케줄이 아닐 때
        if (!fixed.getUser().equals(user)) {
            throw new CustomException(ErrorType.FORBIDDEN);
        }

        // 대타 모집 중 상태로 변경
        fixed.findReplacement();

        // entity 생성, 저장
        Replacement newReplacement = Replacement.builder()
                .fixed(fixed)
                .expirationTime(startTime(fixed.getWorkTime()))
                .week(week)
                .build();
        replacementRepository.save(newReplacement);
        newReplacement.addReplacementToWeek();
    }

    /** 날짜 + 시간 = LocalDateTime */
    private LocalDateTime startTime(WorkTime workTime) {
        LocalDate date = workTime.getDate().getLocalDate();
        return workTime.getStartTime().atDate(date);
    }
}
