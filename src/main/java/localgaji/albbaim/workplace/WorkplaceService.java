package localgaji.albbaim.workplace;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.workplace.invitation.Invitation;
import localgaji.albbaim.workplace.invitation.InvitationService;
import localgaji.albbaim.user.User;
import localgaji.albbaim.user.UserService;
import localgaji.albbaim.workplace.workplaceDTO.ResponseWorkplace.GetMyWorkplaceResponse.UserListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static localgaji.albbaim.workplace.workplaceDTO.RequestWorkplace.*;
import static localgaji.albbaim.workplace.workplaceDTO.ResponseWorkplace.*;

@Service @Slf4j
@RequiredArgsConstructor
public class WorkplaceService {

    private final WorkplaceRepository workplaceRepository;
    private final UserService userService;
    private final InvitationService invitationService;

    // 매장 생성
    @Transactional
    public void addNewWorkplace(User user, PostAddGroupRequest requestBody) {
        // 이미 매장 있을때 : 예외 처리
        if (user.getWorkplace() != null) {
            throw new CustomException(ErrorType.ALREADY_HAVE);
        }
        // 요청 정보로 매장 엔티티 생성
        Workplace newWorkplace = requestBody.toEntity();
        // 매장 엔티티 저장
        workplaceRepository.save(newWorkplace);
        // 유저 엔티티에 매장 정보 업데이트
        userService.addWorkplace(user, newWorkplace);
    }

    // 매장에 유저 가입 (초대 페이지 승인하기)
    @Transactional
    public void joinWorkplace(User user, String invitationKey) {
        // 이미 매장 있을때 : 예외 처리
        if (user.getWorkplace() != null) {
            throw new CustomException(ErrorType.ALREADY_HAVE);
        }
        // 초대키로 초대장 조회
        Invitation invitation = invitationService.readInvitation(invitationKey);
        // 초대장으로 매장 조회 -> 유저 엔티티에 매장 정보 업데이트
        userService.addWorkplace(user, invitation.getWorkplace());
    }

    // 내 매장 정보 조회
    public GetMyWorkplaceResponse findGroupInfo(User user)  {
        // 유저가 소속된 매장 엔티티 조회
        Workplace workplace = Optional.ofNullable(user.getWorkplace())
                .orElseThrow(() -> new CustomException(ErrorType.FORBIDDEN));
        // 해당 매장의 직원 리스트 가공
        List<UserListDTO> members = workplace.getUserList()
                    .stream()
                .map(UserListDTO::new)
                .collect(Collectors.toList());
        return GetMyWorkplaceResponse.builder()
                .groupName(groupName)
                .members(members)
                .build();
    }

    // 초대키로 매장 조회 (초대장 열기)
    public GetInvitationInfoResponse findWorkplaceByInvitationKey(String invitationKey) {
        // 초대키로 초대장 조회
        Invitation invitation = invitationService.readInvitation(invitationKey);
        // 초대장으로 매장 조회
        Workplace workplace = invitation.getWorkplace();
        return new GetInvitationInfoResponse(workplace);
    }

    // (매니저) 초대 키 발급
    @Transactional
    public GetInvitationKeyResponse getInvitationKey(User user) {
        // 매니저가 운영 중인 매장을 조회
        Workplace workplace = Optional.ofNullable(user.getWorkplace())
                .orElseThrow(() -> new CustomException(ErrorType.FORBIDDEN));
        // 해당 그룹의 초대장 발급
        Invitation invitation = invitationService.issueMyWorkplaceInvitation(workplace);
        return new GetInvitationKeyResponse(invitation.getInvitationKey());
    }
}
