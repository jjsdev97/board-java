package com.project.board.boardapi.member.controller;

import com.project.board.boardapi.member.dto.MemberCreateRequest;
import com.project.board.boardapi.member.dto.MemberCreateResponse;
import com.project.board.boardapi.member.dto.MemberResponse;
import com.project.board.boardapi.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;
    public MemberController(MemberService memberService) { this.memberService = memberService; }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getById(id));
    }

    @PostMapping
    public ResponseEntity<MemberCreateResponse> create(@Valid @RequestBody MemberCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.create(request));
    }
}
