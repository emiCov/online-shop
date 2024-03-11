CREATE SEQUENCE IF NOT EXISTS product_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS technical_details_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS roles_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS users_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS inventories_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS cart_items_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS orders_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE IF NOT EXISTS order_details_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS product
(
    id bigint NOT NULL,
	name character varying(255),
	code character varying(255) NOT NULL UNIQUE,
	description character varying(255),
	price double precision,
    CONSTRAINT product_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS technical_details
(
    id bigint NOT NULL,
    product_id bigint,
    name character varying(255),
    value character varying(255),
    CONSTRAINT technical_details_pkey PRIMARY KEY (id),
    CONSTRAINT fk_product_id FOREIGN KEY (product_id)
        REFERENCES product (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS users
(
    id bigint NOT NULL,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255),
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS roles
(
    id bigint NOT NULL,
    name character varying(255),
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users_roles
(
    role_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT users_roles_pkey PRIMARY KEY (role_id, user_id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_role_id FOREIGN KEY (role_id)
        REFERENCES roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS inventories
(
    id bigint NOT NULL,
    code character varying(255) NOT NULL UNIQUE,
    quantity bigint,
    CONSTRAINT inventories_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS cart_items
(
    id bigint NOT NULL,
	quantity smallint NOT NULL,
    product_id bigint,
    user_id bigint,
    CONSTRAINT cart_items_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_product_id FOREIGN KEY (product_id)
        REFERENCES product (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS orders
(
    id bigint NOT NULL,
	local_date date,
    total real NOT NULL,
    order_status character varying(255),
    user_id bigint,
    CONSTRAINT orders_pkey PRIMARY KEY (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS order_details
(
    id bigint NOT NULL,
	quantity smallint NOT NULL,
	unit_price double precision NOT NULL,
    subtotal double precision NOT NULL,
    order_id bigint,
    product_id bigint,
    CONSTRAINT order_details_pkey PRIMARY KEY (id),
    CONSTRAINT fk_product_id FOREIGN KEY (product_id)
        REFERENCES product (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_order_id FOREIGN KEY (order_id)
        REFERENCES orders (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);