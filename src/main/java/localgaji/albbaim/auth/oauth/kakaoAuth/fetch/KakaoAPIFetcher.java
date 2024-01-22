package localgaji.albbaim.auth.oauth.kakaoAuth.fetch;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static localgaji.albbaim.auth.oauth.kakaoAuth.fetch.RequestKakaoAPI.*;
import static localgaji.albbaim.auth.oauth.kakaoAuth.fetch.ResponseKakaoAPI.*;

@Slf4j @Component
public class KakaoAPIFetcher {
    @Value("${kakaoAuth.client_id}")
    private String client_id;

    @Value("${kakaoAuth.redirect_uri}")
    private String redirect_uri;

    public Long codeToKakaoId(String code) {
        log.debug("카카오 인가 코드 {}", code);

        String token = codeToKakaoToken(code);
        log.debug("카카오 토큰 {}", token);

        String kakaoId = tokenToKakaoId(token);
        log.debug("카카오 아이디 {}", kakaoId);

        return Long.parseLong(kakaoId);
    }

    // 1. 인가 코드로 카카오 토큰 호출
    private String codeToKakaoToken(String code) {

        // Request Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        GetTokenRequest requestDTO = GetTokenRequest.builder()
                .client_id(client_id)
                .redirect_uri(redirect_uri)
                .code(code)
                .build();

        MultiValueMap<String, String> requestBody = DTOtoMultiMap.convert(new ObjectMapper(), requestDTO);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);
        String url = "https://kauth.kakao.com/oauth/token";

        return sendPostRequest(request, url, GetTokenResponse.class).access_token();
    }

    // 2. 토큰으로 카카오 id 호출
    private String tokenToKakaoId(String accessToken) {
        // Request Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내기
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = "https://kapi.kakao.com/v2/user/me";

        return sendPostRequest(request, url, GetKakaoIdResponse.class).id();
    }

    private <RequestDTO, ResponseDTO> ResponseDTO sendPostRequest(HttpEntity<RequestDTO> request,
                                                                  String requestUrl,
                                                                  Class<ResponseDTO> responseDTO) {
        RestTemplate rt = new RestTemplate();
        ResponseEntity<ResponseDTO> response = rt.exchange(
                requestUrl,
                HttpMethod.POST,
                request,
                responseDTO
        );
        return response.getBody();
    }
}
