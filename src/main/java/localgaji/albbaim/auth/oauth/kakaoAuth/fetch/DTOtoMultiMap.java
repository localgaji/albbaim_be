package localgaji.albbaim.auth.oauth.kakaoAuth.fetch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class DTOtoMultiMap {
    public static MultiValueMap<String, String> convert(ObjectMapper objectMapper, Object dto) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> map = objectMapper.convertValue(dto, new TypeReference<>() {});
        params.setAll(map);
        return params;
    }
}
