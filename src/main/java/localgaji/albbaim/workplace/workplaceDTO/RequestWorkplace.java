package localgaji.albbaim.workplace.workplaceDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class RequestWorkplace {
    @Schema(description = "그룹 가입")
    public record PostJoinGroupRequest(
        @Schema(description = "초대키")
        String invitationKey
    ) {
    }

    @Schema(description = "그룹 생성")
    public record PostAddGroupRequest (
        @Schema(description = "매장 이름")
        String marketName,

        @Schema(description = "사업자 번호")
        String marketNumber,

        @Schema(description = "주소1")
        String mainAddress,

        @Schema(description = "주소2")
        String detailAddress
    ) {
    }

    @Schema(description = "초대장으로 그룹 정보 조회")
    public record GetGroupInfoRequest(
        @Schema(description = "초대키")
        String invitationKey
    ) {
    }
}
