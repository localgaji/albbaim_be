package localgaji.albbaim.workplace;

import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.invitation.Invitation;
import localgaji.albbaim.workplace.invitation.InvitationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static localgaji.albbaim.utils.Samples.*;
import static localgaji.albbaim.workplace.workplaceDTO.ResponseWorkplace.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkplaceServiceTest {
    @InjectMocks
    private WorkplaceService workplaceService;
    @Mock
    private InvitationService invitationService;

    @DisplayName("내 매장 정보 조회")
    @Test
    void findGroupInfo() {
        // given
        User user1 = someUser();
        User user2 = someUser();
        Workplace workplace = someWorkplace();
        user1.updateWorkplace(workplace);
        user2.updateWorkplace(workplace);

        // when
        GetMyWorkplaceResponse response = workplaceService.findGroupInfo(user1);

        // then
        assertThat(response.groupName()).isEqualTo(workplace.getMarketName());
        assertThat(response.members().size()).isEqualTo(2);
    }

    @DisplayName("초대키로 매장 조회 (초대장 열기)")
    @Test
    void findWorkplaceByInvitationKey() {
        // given
        Workplace workplace = someWorkplace();
        Invitation invitation = someInvitation(workplace);
        when(invitationService.readInvitation(any())).thenReturn(invitation);

        // when
        GetInvitationInfoResponse response = workplaceService.findWorkplaceByInvitationKey(invitation.getInvitationKey());

        // then
        assertThat(response.getMarketName()).isEqualTo(workplace.getMarketName());
    }

    @DisplayName("(매니저) 초대 키 발급")
    @Test
    void getInvitationKey() {
        // given
        Workplace workplace = someWorkplace();
        Invitation invitation = someInvitation(workplace);
        User user = someUser();
        user.updateWorkplace(workplace);

        when(invitationService.issueMyWorkplaceInvitation(any(Workplace.class)))
                .thenReturn(invitation);

        // when
        GetInvitationKeyResponse response = workplaceService.getInvitationKey(user);

        // then
        assertThat(response.invitationKey()).isEqualTo(invitation.getInvitationKey());
    }

}