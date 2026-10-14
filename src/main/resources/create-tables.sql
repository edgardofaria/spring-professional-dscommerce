CREATE TABLE tb_category (
    id   BIGSERIAL    PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE tb_product (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL,
    description TEXT,
    price       NUMERIC(38, 2)  NOT NULL,
    img_url     VARCHAR(255)
);

CREATE TABLE tb_product_category (
    product_id  BIGINT NOT NULL REFERENCES tb_product  (id),
    category_id BIGINT NOT NULL REFERENCES tb_category (id),
    PRIMARY KEY (product_id, category_id)
);

CREATE TABLE tb_user (
    id         BIGSERIAL    PRIMARY KEY,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted_at TIMESTAMP WITH TIME ZONE,
    name       VARCHAR(255),
    email      VARCHAR(255) UNIQUE,
    phone      VARCHAR(255),
    birth_date DATE,
    password   VARCHAR(255)
);

CREATE TABLE tb_role (
    id        BIGSERIAL    PRIMARY KEY,
    authority VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE tb_user_role (
    user_id BIGINT NOT NULL REFERENCES tb_user (id),
    role_id BIGINT NOT NULL REFERENCES tb_role (id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE tb_order (
    id        BIGSERIAL                   PRIMARY KEY,
    moment    TIMESTAMP WITHOUT TIME ZONE,
    status    INTEGER                     NOT NULL,
    client_id BIGINT                      NOT NULL REFERENCES tb_user (id)
);

CREATE TABLE tb_order_item (
    order_id   BIGINT         NOT NULL REFERENCES tb_order   (id),
    product_id BIGINT         NOT NULL REFERENCES tb_product (id),
    quantity   INTEGER        NOT NULL,
    price      NUMERIC(38, 2) NOT NULL,
    PRIMARY KEY (order_id, product_id)
);

CREATE TABLE tb_payment (
    id     BIGINT                      PRIMARY KEY REFERENCES tb_order (id),
    moment TIMESTAMP WITHOUT TIME ZONE
);
