package com.devoops.rentalbrain.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 Authorization Token 추출
        String authorizationHeader = request.getHeader("Authorization");
        // 토큰 존재하는지 검사
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
//            log.info("토큰 인증 실패");
            filterChain.doFilter(request,response);
            return;
        }

        log.info("Authorization 헤더 : {}", authorizationHeader);
        String token = authorizationHeader.substring(7);
        log.info("토큰 : {}", token);

        // 토큰 유효성 검사
        try{
            jwtUtil.validateToken(token);
            // 토큰에 있는 권한 추출
            Authentication authentication = jwtUtil.getAuthentication(token);
            log.info("authentication 내용 : {}",authentication.toString());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("유효하지 않은 JWT Token(값이 없음)");
        } catch(ExpiredJwtException e){
            log.info("세션 만료");
            // refresh token validate api()
        } catch(UnsupportedJwtException e){
            log.info("지원하지 않는 JWT Token");
        } catch(IllegalArgumentException e){
            log.info("토큰의 클레임이 없음");
        }


        filterChain.doFilter(request,response);
    }
}
