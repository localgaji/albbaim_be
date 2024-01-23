package localgaji.albbaim.workplace.invitation;

import localgaji.albbaim.workplace.Workplace;
import localgaji.albbaim.workplace.WorkplaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static localgaji.albbaim.utils.Samples.*;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class InvitationRepositoryTest {
    @Autowired
    InvitationRepository invitationRepository;
    @Autowired
    WorkplaceRepository workplaceRepository;

    @DisplayName("초대키로 초대장 조회 성공")
    @Test
    void findByInvitationKey() {
        // given
        Workplace workplace = someWorkplace();
        workplaceRepository.save(workplace);
        Invitation invitation = someInvitation(workplace);
        invitationRepository.save(invitation);

        // when
        Optional<Invitation> opt = invitationRepository.findByInvitationKey(invitation.getInvitationKey());

        // then
        assertThat(opt.orElseThrow()).isEqualTo(invitation);
    }

    @DisplayName("초대키로 초대장 조회 실패")
    @Test
    void findByInvitationKeyFail() {
        // given
        String key = "fail";

        // when
        Optional<Invitation> opt = invitationRepository.findByInvitationKey(key);

        // then
        assertThat(opt.isEmpty()).isTrue();
    }

    @DisplayName("매장으로 초대장 조회 성공")
    @Test
    void findByWorkplace() {
        // given
        Workplace workplace = someWorkplace();
        workplaceRepository.save(workplace);
        Invitation invitation = someInvitation(workplace);
        invitationRepository.save(invitation);

        // when
        Optional<Invitation> opt = invitationRepository.findByWorkplace(workplace);

        // then
        assertThat(opt.orElseThrow()).isEqualTo(invitation);
    }

    @DisplayName("만료기간 기본값")
    @Test
    void duration() {
        // given
        Workplace workplace = someWorkplace();
        workplaceRepository.save(workplace);
        Invitation invitation = someInvitation(workplace);
        invitationRepository.save(invitation);

        // when
        Optional<Invitation> foundInvitation = invitationRepository.findById(invitation.getInvitationId());

        // then
        assertThat(foundInvitation.orElseThrow().getDurationHours()).isEqualTo(720);
    }
}