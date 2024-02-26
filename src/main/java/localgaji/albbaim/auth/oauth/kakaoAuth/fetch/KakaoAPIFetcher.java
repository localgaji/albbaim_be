package localgaji.albbaim.auth.oauth.kakaoAuth.fetch;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@Component
public class KakaoAPIFetcher {
    @Value("${kakaoAuth.client_id}")
    private String client_id;

    @Value("${kakaoAuth.redirect_uri}")
    private String redirect_uri;

    @Value("${kakaoAuth.token_url}")
    private String token_url;

    @Value("${kakaoAuth.user_info_url}")
    private String user_info_url;


    public Long codeToKakaoId(String code) {
        String token = codeToKakaoToken(code);
        String kakaoId = tokenToKakaoId(token);
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
        String url = token_url;

        return sendRequest(request, url, HttpMethod.POST, GetTokenResponse.class).access_token();
    }

    // 2. 토큰으로 카카오 id 호출
    private String tokenToKakaoId(String accessToken) {
        // Request Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내기
        HttpEntity<String> request = new HttpEntity<>(headers);
        String url = user_info_url;

        return sendRequest(request, url, HttpMethod.GET, GetKakaoIdResponse.class).id();
    }

    // 외부 api에 post 요청 전송
    private <RequestDTO, ResponseDTO> ResponseDTO sendRequest(HttpEntity<RequestDTO> request,
                                                              String requestUrl,
                                                              HttpMethod method,
                                                              Class<ResponseDTO> responseDTO) {
        RestTemplate rt = new RestTemplate();
        ResponseEntity<ResponseDTO> response = rt.exchange(
                requestUrl,
                method,
                request,
                responseDTO
        );
        return response.getBody();
    }
}
