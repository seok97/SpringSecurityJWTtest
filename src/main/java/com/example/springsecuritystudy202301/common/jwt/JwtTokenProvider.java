package com.example.springsecuritystudy202301.common.jwt;

import com.example.springsecuritystudy202301.sample.dto.req.UsrReqDto;
import com.example.springsecuritystudy202301.sample.dto.res.UsrResDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final String secret;
    private final long tokenValidityInMilliseconds;

    private Key key;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds,
            @Value("${jwt.access-token-expire-time}") long accessTime,
            @Value("${jwt.refresh-token-expire-time}") long refreshTime
            ) {
        this.secret = secret;
        this.tokenValidityInMilliseconds = tokenValidityInSeconds;
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;
    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//        byte[] keyBytes = Decoders.BASE64.decode(secret);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }

    private Map<String, Object> createClaim(UsrReqDto usrInfo){
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", usrInfo.getUsrId());
//        claims.put("name", usrInfo.getUserId());
        // 권한명 리스트 값을 모두 이어 붙인 문자열로 만들어 put
        claims.put(AUTHORITIES_KEY, usrInfo.getRoleList()
                .stream()
                .map(roleId -> String.valueOf(roleId))
                .collect(Collectors.joining(",")));
        return claims;
    }

    // 토큰 생성
    public String createToken(UsrReqDto usrInfo, long tokenValid) {
        /**
            3. claim은 payload 부분에 들어갈 정보 조각들이다.
            4. claim은 Registered claim / Public claim / Private claim으로 나눠진다.
            4.1. Registered claim
            1) Registered claim은 Token에 대한 정보를 담기 위해 이미 정해진 claim이다.
            2) Registered claim은 필수 값이 아닌 선택 값이다.
            3) Registered claim에는 Issuer claim, Subject claim, Audience claim, Expiration Time claim, Not Before
            claim, Issued At claim, JWT ID claim이 있다.
            Subject(제목)과 Issued At(발행일), Expiration Time(만료일)
         */
        long now = (new Date()).getTime();
        return Jwts.builder()
                .setSubject(usrInfo.getUsrId()) // 제목
                .setClaims(createClaim(usrInfo)) // 토큰 발행 유저 정보
                .signWith(key, SignatureAlgorithm.HS512) // 키와 알고리즘 설정
                .setExpiration(new Date(now + tokenValid)) // 토큰 유효시간
                .compact();
    }

    /** 액세스 토큰 생성 */
    public String createAccessToken(UsrReqDto usrInfo) {
        return this.createToken(usrInfo, ACCESS_TOKEN_EXPIRE_TIME);
    }

    /** 리프레시 토큰 생성 */
    public String createRefreshToken(UsrReqDto usrInfo) {
        return this.createToken(usrInfo, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**token 유효성 검증 */
    public boolean validateToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            return false;
        } catch (Exception e) {
            log.info("잘못된 토큰입니다.");
            return false;
        }
    }

    /**인증 정보 조회 */
    public Authentication getAuthentication(String token) {

        // 토큰으로 claims 생성
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // claims에서 권한정보만 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 권한 정보를 user 객체에 담아
        User principal = new User(claims.getSubject(), "", authorities);

        // 세가지 정보를 모두 담아 authentication 객체 리턴
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

}
