ALTER TABLE comment
    ADD COLUMN parent_id BIGINT NULL AFTER content,
    ADD COLUMN depth TINYINT NOT NULL DEFAULT 0 AFTER parent_id,

    ADD CONSTRAINT chk_comment_depth
        CHECK (depth IN (0, 1)),

    ADD CONSTRAINT fk_comment_parent
        FOREIGN KEY (parent_id) REFERENCES comment(id);