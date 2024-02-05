package localgaji.albbaim.__utils__;

import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache.KakaoIdCache;
import localgaji.albbaim.schedule.application.Application;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.workTime.DTO.WorkTimeHeadCountDTO;
import localgaji.albbaim.schedule.workTime.DTO.WorkTimeRequest;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import localgaji.albbaim.workplace.invitation.Invitation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeRequest.*;

public class Samples {
    public static User someUser() {
        return User.builder()
                .userName("라이언")
                .isAdmin(true)
                .build();
    }
    public static Workplace someWorkplace() {
        return Workplace.builder()
                .workplaceId(1L)
                .workplaceName("라이언 월드")
                .workplaceNumber("1111111111")
                .mainAddress("서울시 성동구 성수대로")
                .detailAddress("1번지")
                .build();
    }

    public static Invitation someInvitation(Workplace workplace) {
        return Invitation.builder()
                .invitationKey("abc")
                .workplace(workplace)
                .keyUpdatedDate(LocalDateTime.now())
                .build();
    }

    public static Invitation expiredInvitation(Workplace workplace) {
        return Invitation.builder()
                .invitationKey("def")
                .workplace(workplace)
                .keyUpdatedDate(LocalDateTime.of(2000, 10, 10, 1, 1))
                .build();
    }
    public static KakaoAuth someKakaoAuth(User user) {
        return KakaoAuth.builder()
                .kakaoId(123L)
                .user(user)
                .build();
    }

    public static KakaoIdCache someKakaoIdCache() {
        return KakaoIdCache.builder()
                .kakaoId(someKakaoId)
                .code(someCode)
                .build();
    }

    public static String someCode = "CODE";
    public static Long someKakaoId = 123L;

    public static Week someWeek(Workplace workplace) {
        return Week.builder()
                .weekId(1L)
                .workplace(workplace)
                .startWeekDate(LocalDate.of(2030,4,1))
                .build();
    }

    public static Date someDate(Week week) {
        return Date.builder()
                .dateId(1L)
                .week(week)
                .localDate(LocalDate.of(2030,4,1))
                .build();
    }

    public static WorkTime someWorkTime(Date date) {
        return WorkTime.builder()
                .workTimeId(1L)
                .date(date)
                .workTimeName("샘플")
                .startTime(LocalTime.of(12, 0))
                .endTime(LocalTime.of(22, 0))
                .build();
    }

    public static PostOpenRequest postOpenRequest() {
        List<List<WorkTimeHeadCountDTO>> template = IntStream.range(0, 7).mapToObj(d ->
                IntStream.range(0, 3).mapToObj(w -> new WorkTimeHeadCountDTO(
                        Character.toString((char) w + 64),
                        (w * 5 + 10) + ":00",
                        (w * 5 + 14) + ":00",
                        10
                )).collect(Collectors.toList())
        ).collect(Collectors.toList());
        return new PostOpenRequest("2030-04-01", template);
    }

    public static Application someApplication(User user, WorkTime workTime) {
        return Application.builder()
                .workTime(workTime)
                .user(user)
                .build();
    }
}
