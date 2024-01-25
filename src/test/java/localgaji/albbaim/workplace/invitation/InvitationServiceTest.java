package localgaji.albbaim.workplace.invitation;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static localgaji.albbaim.__utils__.Samples.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {
    @InjectMocks
    InvitationService invitationService;
    @Mock
    InvitationRepository invitationRepository;

    @DisplayName("초대장 읽기 성공")
    @Test
    void readInvitation() {
        // given
        Invitation invitation = someInvitation(someWorkplace());
        when(invitationRepository.findByInvitationKey(any())).thenReturn(Optional.of(invitation));

        // when
        Invitation foundInvitation = invitationService.readInvitation(invitation.getInvitationKey());

        // then
        assertThat(foundInvitation).isEqualTo(invitation);
    }

    @DisplayName("초대장 읽기 실패 (만료됨)")
    @Test
    void readInvitationExpired() {
        // given
        Invitation invitation = expiredInvitation(someWorkplace());
        when(invitationRepository.findByInvitationKey(any())).thenReturn(Optional.of(invitation));

        // when, then
        Throwable exception = assertThrows(
                CustomException.class,
                () -> invitationService.readInvitation(invitation.getInvitationKey())
        );
        assertThat(exception.getMessage()).isEqualTo("유효하지 않은 초대장");
    }

    @DisplayName("초대장 발행 (유효한 초대장 있을때)")
    @Test
    void issueMyWorkplaceInvitation() {
        // given
        Workplace workplace = someWorkplace();
        Invitation invitation = someInvitation(workplace);

        when(invitationRepository.findByWorkplace(any(Workplace.class)))
                .thenReturn(Optional.of(invitation));

        // when
        Invitation newInvitation = invitationService.issueMyWorkplaceInvitation(workplace);

        // then
        assertThat(newInvitation).isEqualTo(invitation);
    }

    @DisplayName("초대장 발행 (초대장 만료일 때)")
    @Test
    void issueMyWorkplaceInvitationExpired() {
        // given
        Workplace workplace = someWorkplace();
        Invitation invitation = expiredInvitation(workplace);
        String oldKey = invitation.getInvitationKey();

        when(invitationRepository.findByWorkplace(any(Workplace.class)))
                .thenReturn(Optional.of(invitation));

        // when
        Invitation newInvitation = invitationService.issueMyWorkplaceInvitation(workplace);

        // then
        assertThat(newInvitation.getInvitationKey()).isNotEqualTo(oldKey);
    }

    @DisplayName("초대장 발행 (초대장 없을때)")
    @Test
    void issueMyWorkplaceInvitationNo() {
        // given
        Workplace workplace = someWorkplace();

        when(invitationRepository.findByWorkplace(any(Workplace.class)))
                .thenReturn(Optional.empty());

        // when
        Invitation newInvitation = invitationService.issueMyWorkplaceInvitation(workplace);

        // then
        assertThat(newInvitation).isNotNull();
    }
}