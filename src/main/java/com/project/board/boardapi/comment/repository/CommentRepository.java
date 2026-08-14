package com.project.board.boardapi.comment.repository;

import com.project.board.boardapi.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByIdAndIsDeletedFalse(Long id);
    List<Comment> findAllByBoard_IdAndIsDeletedFalseOrderByIdAsc(Long boardId);
}
