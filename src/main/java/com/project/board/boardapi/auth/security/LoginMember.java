package com.project.board.boardapi.auth.security;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record LoginMember(Long memberId, String email, String memberName) implements UserDetails {
    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return ""; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }
}
