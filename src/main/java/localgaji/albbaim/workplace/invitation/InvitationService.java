package localgaji.albbaim.workplace.invitation;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.workplace.Workplace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @Slf4j
@RequiredArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;

    public Invitation readInvitation(String invitationKey) {
        log.debug("초대장 읽기 시작");
        // 초대장 읽기 시작
        Invitation invitation = invitationRepository.findByInvitationKey(invitationKey)
                .orElseThrow(() -> new CustomException(ErrorType.INVALID_INVITATION));

        if (invitation.isExpired()) {
            log.debug("초대장 만료됨");
            throw new CustomException(ErrorType.INVALID_INVITATION);
        }
        log.debug("초대장 읽기 완료");
        return invitation;
    }

    public Invitation issueMyWorkplaceInvitation(Workplace workplace) {
        log.debug("초대장 발행 시작");
        // 없으면 생성, 만료됐으면 재발급, 멀쩡하면 그냥 가져오기
        Invitation invitation = invitationRepository.findByWorkplace(workplace)
                .orElseGet(() -> createInvitationKey(workplace));

        if (invitation.isExpired()) {
            recreateInvitationKey(invitation);
        }
        log.debug("초대장 발행 완료");
        return invitation;
    }

    private Invitation createInvitationKey(Workplace workplace) {
        log.debug("신규 초대장 생성 시작");
        LocalDateTime now = LocalDateTime.now();
        String newKey = workplace.getWorkplaceId().toString() + now;

        Invitation newInvitation = Invitation.builder()
                .invitationKey(newKey)
                .keyUpdatedDate(now)
                .workplace(workplace)
                .build();

        invitationRepository.save(newInvitation);
        log.debug("신규 초대장 생성 완료");
        return newInvitation;
    }

    private void recreateInvitationKey(Invitation invitation) {
        log.debug("초대키 재발급 시작");
        LocalDateTime now = LocalDateTime.now();
        String newKey = invitation.getWorkplace().getWorkplaceId().toString() + now;

        invitation.updateInvitation(newKey, now);
        invitationRepository.save(invitation);
        log.debug("초대키 재발급 완료");
    }
}
