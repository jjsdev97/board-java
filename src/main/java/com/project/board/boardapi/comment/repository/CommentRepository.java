package com.project.board.boardapi.comment.repository;

import com.project.board.boardapi.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
