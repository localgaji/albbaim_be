package localgaji.albbaim.oauth.kakaoAuth.kakaoAuthDTO;

public class ResponseKakaoAPI {
    public record GetTokenResponse(
            String access_token
    ) {
    }

    public record GetKakaoIdResponse(
            String id
    ) {
    }
}
