package com.project.board.boardapi.comment.domain;

import com.project.board.boardapi.board.domain.Board;
import com.project.board.boardapi.common.entity.BaseEntity;
import com.project.board.boardapi.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 200, message = "200자 이하만 가능")
    @NotBlank(message = "내용을 입력하셔야합니다.")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Comment(){}

    public Comment(String content, Board board, Member member){
        this.content = content;
        this.board = board;
        this.member = member;
    }

    public Long getId(){
        return id;
    }

    public String getContent() {
        return this.content;
    }


    public Member getMember() {
        return this.member;
    }
}
