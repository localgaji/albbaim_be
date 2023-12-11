package localgaji.albbaim.oauth.kakaoAuth.kakaoAuthDTO;

import lombok.Builder;
import lombok.Getter;

public class RequestKakaoAPI {
    @Getter @Builder
    public static class GetTokenRequest {
         private final String grant_type = "authorization_code";
         private final String client_id;
         private final String redirect_uri;
         private final String code;
    }
}