package localgaji.albbaim.workplace.invitation;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.workplace.Workplace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;

    public Invitation openInvitation(String invitationKey) {
        Invitation invitation = invitationRepository.findByInvitationKey(invitationKey)
                .orElseThrow(() -> new CustomException(ErrorType.INVALID_INVITATION));

        if (invitation.isExpired()) {
            throw new CustomException(ErrorType.INVALID_INVITATION);
        }

        return invitation;
    }

    @Transactional
    public Invitation getMyInvitation(Workplace workplace) {
        Invitation invitation = invitationRepository.findByWorkplace(workplace)
                .orElseGet(() -> createInvitationKey(workplace));

        if (invitation.isExpired()) {
            recreateInvitationKey(invitation);
        }

        return invitation;
    }

    private Invitation createInvitationKey(Workplace workplace) {
        LocalDateTime now = LocalDateTime.now();
        String newKey = workplace.getWorkplaceId().toString() + now;

        Invitation newInvitation = Invitation.builder()
                .invitationKey(newKey)
                .keyUpdatedDate(now)
                .workplace(workplace)
                .build();

        invitationRepository.save(newInvitation);

        return newInvitation;
    }

    private void recreateInvitationKey(Invitation invitation) {
        LocalDateTime now = LocalDateTime.now();
        String newKey = invitation.getWorkplace().getWorkplaceId().toString() + now;

        invitation.updateInvitation(newKey, now);
        invitationRepository.save(invitation);
    }
}
