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

import static localgaji.albbaim.__utils__.Samples.*;
import static localgaji.albbaim.workplace.workplaceDTO.RequestWorkplace.*;
import static localgaji.albbaim.workplace.workplaceDTO.ResponseWorkplace.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkplaceServiceTest {
    @InjectMocks
    private WorkplaceService workplaceService;
    @Mock
    private InvitationService invitationService;
    @Mock
    private WorkplaceRepository workplaceRepository;
    private final User user = someUser();
    private final Workplace workplace = someWorkplace();

    @DisplayName("매장 생성 (가입된 매장 없을 때)")
    @Test
    void addNewWorkplace() {
        // given
        PostAddGroupRequest request = PostAddGroupRequest.builder()
                .workplaceName(workplace.getWorkplaceName())
                .workplaceNumber(workplace.getWorkplaceNumber())
                .mainAddress(workplace.getMainAddress())
                .detailAddress(workplace.getDetailAddress())
                .build();

        // when
        workplaceService.addNewWorkplace(user, request);

        // then
        verify(workplaceRepository).save(any(Workplace.class));
        assertThat(user.getWorkplace()).isNotNull();
    }

    @DisplayName("내 매장 정보 조회")
    @Test
    void findGroupInfo() {
        // given
        User anotherUser = someUser();
        Workplace workplace = someWorkplace();
        user.updateWorkplace(workplace);
        anotherUser.updateWorkplace(workplace);

        // when
        GetMyWorkplaceResponse response = workplaceService.findGroupInfo(user);

        // then
        assertThat(response.workplaceName()).isEqualTo(workplace.getWorkplaceName());
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
        assertThat(response.getWorkplaceName()).isEqualTo(workplace.getWorkplaceName());
    }

    @DisplayName("(매니저) 초대 키 발급")
    @Test
    void getInvitationKey() {
        // given
        Workplace workplace = someWorkplace();
        Invitation invitation = someInvitation(workplace);
        user.updateWorkplace(workplace);

        when(invitationService.getMyWorkplaceInvitation(any(Workplace.class)))
                .thenReturn(invitation);

        // when
        GetInvitationKeyResponse response = workplaceService.getInvitationKey(user);

        // then
        assertThat(response.invitationKey()).isEqualTo(invitation.getInvitationKey());
    }

}