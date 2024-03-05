package localgaji.albbaim.__core__.auth;

import io.jsonwebtoken.ExpiredJwtException;
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
    @Value("${jwt.tokenValidityInSeconds}")
    private Long tokenValidityInSeconds;
    private final Key secretKey;
    private final String tokenTypeKeyword = "Bearer ";

    public TokenProvider(@Value("${jwt.secret}") String secret
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
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

        return tokenTypeKeyword + jwt;
    }

    /** JWT 토큰 검증하고 userId 얻기 */
    public Long getUserIdByToken(String authorization) {
        Claims claims = parseToken(authorization);

        // claim set 에서 subject claim (== userId) 뽑기
        String sub = claims.getSubject();
        if (sub == null) {
            throw new CustomException(ErrorType.INVALID_TOKEN);
        }
        return Long.parseLong(sub);
    }

    /** 토큰에서 claim set 뽑기 (+ 유효성 검증, 만료 검증) */
    private Claims parseToken(String authorization) {
        // 토큰 시작 키워드 검증
        if (!authorization.startsWith(tokenTypeKeyword)) {
            throw new CustomException(ErrorType.INVALID_TOKEN);
        }

        // 시작 키워드 제거
        String token = authorization.substring(tokenTypeKeyword.length());

        // 토큰에서 claime set 뽑기 + 예외 처리
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorType.EXPIRED_TOKEN);
        } catch (Exception e) {
            throw new CustomException(ErrorType.INVALID_INVITATION);
        }
    }
}