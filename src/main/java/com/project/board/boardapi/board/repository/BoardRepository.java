package com.project.board.boardapi.board.repository;

import com.project.board.boardapi.board.domain.Board;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndIsDeletedFalse(Long id);
    Page<Board> findAllByIsDeletedFalse(Pageable pageable);
    Page<Board> findByTitleContainingAndIsDeletedFalse(String keyword, Pageable pageable);
}
