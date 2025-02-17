package com.example.elice_3rd.member.service;

import com.example.elice_3rd.member.dto.MemberRequestDto;
import com.example.elice_3rd.member.dto.MemberUpdateDto;
import com.example.elice_3rd.member.entity.Member;
import com.example.elice_3rd.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberManagementService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    Member toEntity(MemberRequestDto requestDto) {
        return Member.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .contact(requestDto.getContact())
                .build();
    }

    void register(MemberRequestDto requestDto){
        if(memberRepository.existsByEmail(requestDto.getEmail()))
            throw new IllegalArgumentException("회원가입 실패: 중복되는 이메일을 사용하는 회원이 존재합니다.");
        memberRepository.save(toEntity(requestDto));
    }

    // TODO Entity 없을 때 예외 처리
    @Transactional
    void updatePassword(String email, String password){
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("비밀번호 변경 실패: 이메일과 일치하는 회원이 존재하지 않습니다.")
        );
        member.updatePassword(passwordEncoder.encode(password));
    }

    @Transactional
    void updateInfo(String email, MemberUpdateDto updateDto){
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("회원 정보 수정 실패: 이메일과 일치하는 회원이 존재하지 않습니다.")
        );
        member.updateInfo(updateDto);
    }

    @Transactional
    void quit(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("회원 탈퇴 실패: 이메일과 일치하는 회원이 존재하지 않습니다.")
        );
        member.quit();
    }

    @Transactional
    void updateRole(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("회원 권한 변경 실패: 이메일과 일치하는 회원이 없습니다.")
        );
        member.updateRoleDoctor();
    }
}
