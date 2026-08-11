package com.project.board.boardapi.member.repository;

import com.project.board.boardapi.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByIdAndIsDeletedFalse(Long id);
    boolean existsByEmail(String email);
    Optional<Member> findByEmailAndIsDeletedFalse(String email);
}
