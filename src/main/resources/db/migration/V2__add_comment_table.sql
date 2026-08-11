CREATE TABLE comment (
                        id BIGINT NOT NULL AUTO_INCREMENT,

                        content TEXT NOT NULL,

                        board_id BIGINT NOT NULL,
                        member_id BIGINT NOT NULL,

                        created_at DATETIME(6) NOT NULL,
                        created_by BIGINT NULL,
                        updated_at DATETIME(6) NOT NULL,
                        updated_by BIGINT NULL,

                        is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                        deleted_at DATETIME(6) NULL,
                        deleted_by BIGINT NULL,

                        PRIMARY KEY (id),

                        CONSTRAINT fk_comment_member
                            FOREIGN KEY (member_id)
                                REFERENCES member(id),

                        CONSTRAINT fk_comment_board
                            FOREIGN KEY (board_id)
                                REFERENCES board(id)
);