package com.devoops.rentalbrain.security;

import com.devoops.rentalbrain.employee.command.service.EmployeeCommandService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.List;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final Key refreshKey;
    private final EmployeeCommandService employeeCommandService;

    public JwtUtil(@Value("${token.access_secret}")String key,
                   @Value("${token.refresh_secret}")String refreshToken,
                   EmployeeCommandService employeeCommandService){
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.refreshKey = Keys.hmacShaKeyFor(keyBytes);
        this.employeeCommandService = employeeCommandService;
    }

    public void validateToken(String token) throws io.jsonwebtoken.security.SecurityException, MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Authentication getAuthentication(String token) {
        // 토큰에 있는 claims 추출
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        // 토큰에 들어있던 emp_id로 유효성 검증
        UserDetails userDetails = employeeCommandService.loadUserByUsername(claims.getSubject());
//        log.info("userDetails: {}",userDetails.getUsername());
//        log.info("userDetails: {}",userDetails.getAuthorities());


        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public void validateRefreshToken(String refreshToken) throws ExpiredJwtException,Exception{
        Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(refreshToken);
    }

    public String getUserId(String refreshToken) {
        Claims claims = Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(refreshToken).getBody();
        return claims.getSubject();
    }

    public String generateAccessToken(String userId) {

        return null;
    }
}
