package com.project.board.boardapi.auth.service;

import com.project.board.boardapi.auth.dto.LoginRequest;
import com.project.board.boardapi.auth.dto.LoginResponse;
import com.project.board.boardapi.auth.exception.LoginFailedException;
import com.project.board.boardapi.auth.jwt.JwtProvider;
import com.project.board.boardapi.member.domain.Member;
import com.project.board.boardapi.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmailAndIsDeletedFalse(request.email())
                .orElseThrow(LoginFailedException::new);
        if (!passwordEncoder.matches(request.password(), member.getPassword())) throw new LoginFailedException();
        return new LoginResponse(jwtProvider.createAccessToken(member.getId()));
    }
}
