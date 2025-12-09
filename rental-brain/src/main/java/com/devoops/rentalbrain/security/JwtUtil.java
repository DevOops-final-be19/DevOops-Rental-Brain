package com.devoops.rentalbrain.security;

import com.kjandgo.securitydemo.member.command.service.MemberCommandService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final MemberCommandService memberCommandService;

    public JwtUtil(@Value("${token.secret}")String key,
                   MemberCommandService memberCommandService){
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.memberCommandService = memberCommandService;
    }

    public void validateToken(String token) throws io.jsonwebtoken.security.SecurityException,
            MalformedJwtException,
            ExpiredJwtException,
            UnsupportedJwtException,
            IllegalArgumentException
    {

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Authentication getAuthentication(String token) {
        // 토큰에 있는 claims 추출
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        // 토큰에 들어있던 member_id로 유효성 검증
        UserDetails userDetails = memberCommandService.loadUserByUsername(claims.getSubject());
//        log.info("userDetails: {}",userDetails.getUsername());
//        log.info("userDetails: {}",userDetails.getAuthorities());

        // 토큰에 있는 권한 추출



        //

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
