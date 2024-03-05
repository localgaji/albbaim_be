package localgaji.albbaim.__core__.auth;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenProvider {
    private final Long tokenValidityInSeconds;
    private final Key secretKey;

    public TokenProvider(@Value("${jwt.secret}") String secret,
                         @Value("${jwt.tokenValidityInSeconds}") long tokenValidityInSeconds
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

    /** 토큰 생성 */
    public String createToken(Long userId) {
        //Header 부분 설정
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // 토큰 만료 시간 구하기
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireTime = now.plusSeconds(tokenValidityInSeconds);

        // 토큰 생성
        String jwt = Jwts.builder()
                .setHeader(headers)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expireTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return "Bearer " + jwt;
    }

    public Long tokenToId(String authorization) {
        Claims claims = parseToken(authorization);
        String sub = claims.getSubject();
        if (sub == null) {
            throw new CustomException(ErrorType.INVALID_TOKEN);
        }
        return Long.parseLong(sub);
    }

    /** 토큰에서 claim set 뽑기 (+ 유효성 검증, 만료 검증) */
    private Claims parseToken(String authorization) {
        String token = authorization.substring("Bearer ".length());

        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean checkExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        Date now = new Date();
        return now.before(expiration);
    }
}