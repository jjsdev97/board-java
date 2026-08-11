CREATE TABLE member (
                        id BIGINT NOT NULL AUTO_INCREMENT,

                        email VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        age INT NOT NULL,

                        created_at DATETIME(6) NOT NULL,
                        created_by BIGINT NULL,
                        updated_at DATETIME(6) NOT NULL,
                        updated_by BIGINT NULL,

                        is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                        deleted_at DATETIME(6) NULL,
                        deleted_by BIGINT NULL,

                        PRIMARY KEY (id),
                        UNIQUE KEY uk_member_email (email)
);

CREATE TABLE board (
                       id BIGINT NOT NULL AUTO_INCREMENT,

                       member_id BIGINT NOT NULL,
                       title VARCHAR(100) NOT NULL,
                       content TEXT NOT NULL,

                       created_at DATETIME(6) NOT NULL,
                       created_by BIGINT NULL,
                       updated_at DATETIME(6) NOT NULL,
                       updated_by BIGINT NULL,

                       is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                       deleted_at DATETIME(6) NULL,
                       deleted_by BIGINT NULL,

                       PRIMARY KEY (id),

                       CONSTRAINT fk_board_member
                           FOREIGN KEY (member_id)
                               REFERENCES member(id)
);