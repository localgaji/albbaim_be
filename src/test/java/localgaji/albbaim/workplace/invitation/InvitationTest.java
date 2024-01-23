package localgaji.albbaim.workplace.invitation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static localgaji.albbaim.utils.Samples.*;
import static org.assertj.core.api.Assertions.assertThat;

class InvitationTest {
    @DisplayName("만료기간 기본값")
    @Test
    void duration() {
        // given
        Invitation invitation = someInvitation(someWorkplace());

        // then
        assertThat(invitation.getDurationHours()).isEqualTo(720);
    }

    @DisplayName("초대장 만료 (안됨)")
    @Test
    void isNotExpired() {
        // given
        Invitation invitation = someInvitation(someWorkplace());
        invitation.updateInvitation("123", LocalDateTime.now().minusHours(9));
        invitation.updateDurationHours(10);

        // when
        boolean isExpired = invitation.isExpired();

        // then
        assertThat(isExpired).isFalse();
    }

    @DisplayName("초대장 만료 (됨)")
    @Test
    void isExpired() {
        // given
        Invitation invitation = someInvitation(someWorkplace());
        invitation.updateInvitation("123", LocalDateTime.now().minusHours(11));
        invitation.updateDurationHours(10);

        // when
        boolean isExpired = invitation.isExpired();

        // then
        assertThat(isExpired).isTrue();
    }
}