package com.project.board.boardapi.board.repository;

import com.project.board.boardapi.board.domain.Board;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByIdAndIsDeletedFalse(Long id);
    Page<Board> findAllByIsDeletedFalse(Pageable pageable);
    Page<Board> findByTitleContainingAndIsDeletedFalse(String keyword, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Board b SET b.viewCount = b.viewCount + :increment "
            + "WHERE b.id = :boardId AND b.isDeleted = false")
    int incrementViewCount(@Param("boardId") Long boardId, @Param("increment") long increment);
}
