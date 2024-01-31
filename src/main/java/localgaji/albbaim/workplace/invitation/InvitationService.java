package localgaji.albbaim.workplace.invitation;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.workplace.Workplace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;

    // 초대장 열기
    public Invitation readInvitation(String invitationKey) {
        // 초대키로 초대장 조회 : 없으면 에러
        Invitation invitation = invitationRepository.findByInvitationKey(invitationKey)
                .orElseThrow(() -> new CustomException(ErrorType.INVALID_INVITATION));

        // 만료되었으면 에러
        if (invitation.isExpired()) {
            throw new CustomException(ErrorType.INVALID_INVITATION);
        }
        return invitation;
    }

    // 초대장 조회
    public Invitation getMyWorkplaceInvitation(Workplace workplace) {
        // 없으면 생성, 있으면 가져오기
        Invitation invitation = invitationRepository.findByWorkplace(workplace)
                .orElseGet(() -> createInvitationKey(workplace));

        // 만료됐으면 재발급
        if (invitation.isExpired()) {
            recreateInvitationKey(invitation);
        }
        return invitation;
    }

    // 신규 초대장 생성
    private Invitation createInvitationKey(Workplace workplace) {
        // 임시 초대키 생성 로직
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

    // 초대장 재발급
    private void recreateInvitationKey(Invitation invitation) {
        // 임시 초대키 생성 로직
        LocalDateTime now = LocalDateTime.now();
        String newKey = invitation.getWorkplace().getWorkplaceId().toString() + now;

        invitation.updateInvitation(newKey, now);
        invitationRepository.save(invitation);
    }
}
