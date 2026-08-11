package com.project.board.boardapi.member.service;

import com.project.board.boardapi.member.domain.Member;
import com.project.board.boardapi.member.dto.MemberCreateRequest;
import com.project.board.boardapi.member.dto.MemberCreateResponse;
import com.project.board.boardapi.member.dto.MemberResponse;
import com.project.board.boardapi.member.exception.DuplicateEmailException;
import com.project.board.boardapi.member.exception.MemberNotFoundException;
import com.project.board.boardapi.member.repository.MemberRepository;
import java.util.Objects;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberCreateResponse create(MemberCreateRequest request) {
        if (memberRepository.existsByEmail(request.email())) throw new DuplicateEmailException(request.email());
        String encodedPassword = Objects.requireNonNull(
                passwordEncoder.encode(request.password()), "비밀번호 인코딩에 실패했습니다.");
        Member saved = memberRepository.save(
                new Member(request.email(), encodedPassword, request.name(), request.age()));
        return new MemberCreateResponse(saved.getId());
    }

    public MemberResponse getById(Long id) {
        Member member = getMember(id);
        return new MemberResponse(member.getId(), member.getEmail(), member.getName(), member.getAge());
    }

    private Member getMember(Long id) {
        return memberRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
}
