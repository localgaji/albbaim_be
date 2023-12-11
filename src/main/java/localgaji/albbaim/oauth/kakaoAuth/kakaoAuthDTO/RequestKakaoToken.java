package localgaji.albbaim.oauth.kakaoAuth.kakaoAuthDTO;


import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class RequestKakaoToken {
    public final String grant_type = "authorization_code";

    public String client_id;

    public String redirect_uri;

    public String code;

}