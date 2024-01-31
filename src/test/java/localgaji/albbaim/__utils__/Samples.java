package localgaji.albbaim.__utils__;

import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache.KakaoIdCache;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import localgaji.albbaim.workplace.invitation.Invitation;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                .marketName("라이언 월드")
                .marketNumber("1111111111")
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
                .localDate(LocalDate.of(2025,4,1))
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
}
