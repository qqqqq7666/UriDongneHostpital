package com.example.elice_3rd.security;

import com.example.elice_3rd.common.exception.NoSuchDataException;
import com.example.elice_3rd.member.entity.Member;
import com.example.elice_3rd.member.repository.MemberRepository;
import com.example.elice_3rd.security.jwt.JwtUtil;
import com.example.elice_3rd.security.service.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // obtainUsername 대신 parameter email 사용함
        String email = request.getParameter("email");
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails memberDetails = (CustomUserDetails) authentication.getPrincipal();
        if(memberDetails.isDeleted()){
            log.error("로그인 실패: 탈퇴한 회원 정보");
            throw new BadRequestException("탈퇴한 회원입니다.");
        }

        String email = memberDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        Member member = memberRepository.findByEmail(email).orElseThrow(() ->
                new NoSuchDataException("로그인 실패: 이메일과 일치하는 회원 정보가 없습니다.")
        );
        String name = member.getName();
        Boolean isOauth = member.getIsOauth();


        String accessToken = jwtUtil.createAccessToken(email, role, name, isOauth);
        String refreshToken = jwtUtil.createRefreshToken(email, role, name, isOauth);

        jwtUtil.addRefreshToken(email, refreshToken);

        response.addCookie(jwtUtil.createCookie("access", accessToken));
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("/");
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
    }
}
