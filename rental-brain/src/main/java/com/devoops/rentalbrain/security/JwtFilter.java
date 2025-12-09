package com.devoops.rentalbrain.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String,String> redisTemplate;

    public JwtFilter(JwtUtil jwtUtil,
                     RedisTemplate<String,String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 Authorization Token 추출
        String authorizationHeader = request.getHeader("Authorization");

        // 토큰 존재하는지 검사
        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            log.info("토큰 인증 실패");
            filterChain.doFilter(request,response);
            return;
        }

        String accessToken = authorizationHeader.substring(7);

        // 토큰 유효성 검사
        try{
            jwtUtil.validateToken(accessToken);

            // 토큰에 있는 권한 추출
            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            log.info("authentication 내용 : {}",authentication.toString());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch(ExpiredJwtException e){
            log.info("세션 만료");
            // 프론트로 엑세스 토큰 만료 메세지 전송

            // refresh token 검증
            String refreshToken = null;
            refreshToken = request.getHeader("Refresh-Token");

            if (refreshToken == null || refreshToken.isBlank()) {
                log.info("Refresh Token 없음");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"message\":\"access token expired\"}");
                return;
            }
            log.info("리프레쉬 토큰 : {}",refreshToken);


        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("유효하지 않은 JWT Token(값이 없음)");

        }  catch(UnsupportedJwtException e){
            log.info("지원하지 않는 JWT Token");

        } catch(IllegalArgumentException e){
            log.info("토큰의 클레임이 없음");

        }


        filterChain.doFilter(request,response);
    }

    private void refreshTokenValidate(String accessToken, String refreshToken, HttpServletRequest request, HttpServletResponse response, String authorizationHeader) throws IOException {
        String userId = null;

        // 1. Refresh Token 검증
        try {
            jwtUtil.validateRefreshToken(refreshToken);   // 만료되거나 위조되면 예외 발생
            userId = jwtUtil.getUserId(refreshToken);
            log.info("Refresh Token 유효. userId={}", userId);

        } catch (ExpiredJwtException e) {
            log.info("Refresh Token 만료됨 → 재로그인 필요");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"refresh token expired\"}");
            return;
        } catch (Exception e) {
            log.info("Refresh Token 위조 또는 잘못됨");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"invalid refresh token\"}");
            return;
        }


        // 2. Redis에 저장된 Refresh Token과 동일한지 비교
        String redisKey = "RT:" + userId;
        String redisStored = (String) redisTemplate.opsForValue().get(redisKey);

        if (redisStored == null) {
            log.info("Redis에 Refresh Token이 없음 → 이미 로그아웃된 사용자");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"no refresh token stored\"}");
            return;
        }

        if (!redisStored.equals(refreshToken)) {
            log.info("Refresh Token 불일치 → 탈취 가능성 있음");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"refresh token mismatch\"}");
            return;
        }

        // 3. Access Token이 유효한지 확인 -> 여기서 만료만 허용, 다른 오류는 거부
        try {
            jwtUtil.validateToken(accessToken);

            // Access Token 아직 유효하면 재발급 필요 없음 → 그냥 진행
            log.info("Access Token 아직 유효 → authentication 설정");
            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return;

        } catch (ExpiredJwtException e) {
            log.info("Access Token 만료됨 → refresh token 기반으로 재발급 진행");

        } catch (Exception e) {
            log.info("Access Token 위조 또는 잘못됨 → 거부");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\": \"invalid access token\"}");
            return;
        }


        // 4. Access Token 재발급
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        log.info("새 Access Token 발급: {}", newAccessToken);

        // 응답 헤더에 새 토큰 전달
        response.setHeader("access-Token", newAccessToken);

        // Authentication 설정 (새로운 access token 기준)
        Authentication authentication = jwtUtil.getAuthentication(newAccessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void accessTokenValidate(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) throws IOException, ServletException {


    }

}
