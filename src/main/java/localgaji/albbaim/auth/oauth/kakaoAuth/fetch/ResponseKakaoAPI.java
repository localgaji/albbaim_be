package localgaji.albbaim.auth.oauth.kakaoAuth.fetch;

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
