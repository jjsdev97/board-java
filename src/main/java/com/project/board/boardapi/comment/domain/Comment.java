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

    // 부모 댓글
    // 일반 댓글이면 null, 대댓글이면 부모 Comment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Column(nullable = false)
    private byte depth = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    protected Comment() {
    }

    // 일반 댓글
    public Comment(String content, Board board, Member member) {
        this.content = content;
        this.board = board;
        this.member = member;
        this.parent = null;
        this.depth = 0;
    }

    // 대댓글
    public Comment(
            String content,
            Board board,
            Member member,
            Comment parent
    ) {
        if (parent.getDepth() != 0) {
            throw new IllegalArgumentException(
                    "대댓글에는 답글을 작성할 수 없습니다."
            );
        }

        this.content = content;
        this.board = board;
        this.member = member;
        this.parent = parent;
        this.depth = 1;
    }

    public Long getId() {
        return id;
    }

    public int getDepth() {
        return depth;
    }

    public Member getMember() {
        return member;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public Comment getParent() {
        return parent;
    }

    public String getContent() {
        return content;
    }

    public Long getBoardId() {
        return board.getId();
    }
}
