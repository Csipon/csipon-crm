DROP SCHEMA IF EXISTS public CASCADE;
COMMIT;
CREATE SCHEMA public;
COMMIT;


CREATE TABLE address
(
  id                BIGSERIAL        NOT NULL,
  latitude          DOUBLE PRECISION NOT NULL,
  longitude         DOUBLE PRECISION NOT NULL,
  formatted_address VARCHAR(300)     NOT NULL,
  region_id         INTEGER          NOT NULL,
  details           VARCHAR(500)
);


CREATE INDEX address__IDX
  ON address
  (
    latitude ASC,
    longitude ASC
  );

ALTER TABLE address
  ADD CONSTRAINT address_PK PRIMARY KEY (id);


CREATE TABLE complaint
(
  id          BIGSERIAL    NOT NULL,
  title       VARCHAR(50)  NOT NULL,
  message     VARCHAR(400) NOT NULL,
  status_id   INTEGER      NOT NULL,
  "date"      TIMESTAMP(0) NOT NULL,
  customer_id INTEGER      NOT NULL,
  pmg_id      INTEGER,
  order_id    INTEGER      NOT NULL
);


ALTER TABLE complaint
  ADD CONSTRAINT complaint_PK PRIMARY KEY (id);


CREATE TABLE discount
(
  id          BIGSERIAL        NOT NULL,
  title       VARCHAR(50)      NOT NULL,
  percentage  DOUBLE PRECISION NOT NULL,
  description VARCHAR(400)     NOT NULL,
  active      BOOLEAN          NOT NULL
);


CREATE INDEX discount__IDX
  ON discount
  (
    title ASC
  );

ALTER TABLE discount
  ADD CONSTRAINT discount_PK PRIMARY KEY (id);


CREATE TABLE groups
(
  id          BIGSERIAL   NOT NULL,
  name        VARCHAR(50) NOT NULL,
  discount_id INTEGER
);


ALTER TABLE groups
  ADD CONSTRAINT groups_PK PRIMARY KEY (id);

-- ALTER TABLE groups
--   ADD CONSTRAINT groups__UN UNIQUE (discount_id);


CREATE TABLE history
(
  id                 BIGSERIAL    NOT NULL,
  new_status_id      INTEGER      NOT NULL,
  date_change_status TIMESTAMP(0) NOT NULL,
  desc_change_status VARCHAR(100) NOT NULL,
  order_id           INTEGER,
  complaint_id       INTEGER,
  product_id         INTEGER
);


ALTER TABLE history
  ADD CONSTRAINT history_PK PRIMARY KEY (id);


CREATE TABLE "orders"
(
  id             BIGSERIAL NOT NULL,
  date_finish    TIMESTAMP(0),
  preferred_date TIMESTAMP(0),
  status_id      INTEGER   NOT NULL,
  customer_id    INTEGER   NOT NULL,
  product_id     INTEGER   NOT NULL,
  csr_id         INTEGER
);


ALTER TABLE "orders"
  ADD CONSTRAINT orders_PK PRIMARY KEY (id);


CREATE TABLE organization
(
  id   BIGSERIAL   NOT NULL,
  name VARCHAR(50) NOT NULL
);


ALTER TABLE organization
  ADD CONSTRAINT organization_PK PRIMARY KEY (id);

ALTER TABLE organization
  ADD CONSTRAINT organization__UN UNIQUE (name);


CREATE TABLE persistent_logins
(
  series    CHARACTER VARYING(64)       NOT NULL,
  username  CHARACTER VARYING(64)       NOT NULL,
  token     CHARACTER VARYING(64)       NOT NULL,
  last_used TIMESTAMP WITHOUT TIME ZONE NOT NULL
);


ALTER TABLE persistent_logins
  ADD CONSTRAINT persistent_logins_PK PRIMARY KEY (series);


CREATE TABLE product
(
  id            BIGSERIAL   NOT NULL,
  title         VARCHAR(50) NOT NULL,
  default_price DOUBLE PRECISION,
  status_id     INTEGER     NOT NULL,
  description   VARCHAR(400),
  discount_id   INTEGER,
  group_id      INTEGER
);


CREATE INDEX product__IDX
  ON product
  (
    title ASC
  );

ALTER TABLE product
  ADD CONSTRAINT product_PK PRIMARY KEY (id);

-- ALTER TABLE product
--     ADD CONSTRAINT product__UN UNIQUE ( discount_id ) ;

CREATE TABLE \
    ( 
     id BIGSERIAL  NOT NULL , 
     param_name VARCHAR (50)  NOT NULL , 
     value VARCHAR (50)  NOT NULL , 
     product_id INTEGER
    ) 
;


CREATE INDEX product_param__IDX ON product_param 
    ( 
     param_name ASC 
    ) 
;

ALTER TABLE product_param 
    ADD CONSTRAINT product_param_PK PRIMARY KEY ( id ) ;

ALTER TABLE product_param 
    ADD CONSTRAINT product_param__UN UNIQUE ( product_id , param_name ) ;

ALTER TABLE product_param 
    ADD CONSTRAINT product_param_product_FK FOREIGN KEY 
    ( 
     product_id
    ) 
    REFERENCES product 
    ( 
     id
    ) 
    ON DELETE CASCADE 
;


CREATE TABLE region
(
  id          BIGSERIAL   NOT NULL,
  name        VARCHAR(50) NOT NULL
);


ALTER TABLE region
  ADD CONSTRAINT region_PK PRIMARY KEY (id);


CREATE TABLE region_groups
(
  id        BIGSERIAL NOT NULL,
  region_id INTEGER   NOT NULL,
  group_id  INTEGER   NOT NULL
);


ALTER TABLE region_groups
  ADD CONSTRAINT region_groups_PK PRIMARY KEY (id);

ALTER TABLE region_groups
  ADD CONSTRAINT region_groups__UN UNIQUE (region_id, group_id);


CREATE TABLE statuses
(
  id   BIGSERIAL   NOT NULL,
  name VARCHAR(50) NOT NULL
);


ALTER TABLE statuses
  ADD CONSTRAINT statuses_PK PRIMARY KEY (id);


CREATE TABLE user_attempts
(
  id            BIGSERIAL                NOT NULL,
  attempts      INTEGER                  NOT NULL,
  last_modified TIMESTAMP WITH TIME ZONE NOT NULL,
  user_id       INTEGER                  NOT NULL
);


ALTER TABLE user_attempts
  ADD CONSTRAINT user_attempts_PK PRIMARY KEY (id);


CREATE TABLE user_roles
(
  id   BIGSERIAL   NOT NULL,
  name VARCHAR(30) NOT NULL
);


ALTER TABLE user_roles
  ADD CONSTRAINT group_user_PK PRIMARY KEY (id);


CREATE TABLE users
(
  id                 BIGSERIAL    NOT NULL,
  password           VARCHAR(100) NOT NULL,
  first_name         VARCHAR(50)  NOT NULL,
  middle_name        VARCHAR(50)  NOT NULL,
  last_name          VARCHAR(50),
  phone              VARCHAR(20),
  email              VARCHAR(320) NOT NULL,
  enable             BOOLEAN      NOT NULL,
  account_non_locked BOOLEAN      NOT NULL,
  contact_person     BOOLEAN      NOT NULL,
  address_id         INTEGER,
  user_role_id       INTEGER      NOT NULL,
  org_id             INTEGER
);


CREATE INDEX users__IDX
  ON users
  (
    first_name ASC,
    middle_name ASC,
    last_name ASC
  );

ALTER TABLE users
  ADD CONSTRAINT users_PK PRIMARY KEY (id);

ALTER TABLE users
  ADD CONSTRAINT users__UN UNIQUE (email);


ALTER TABLE address
  ADD CONSTRAINT address_region_FK FOREIGN KEY
  (
    region_id
  )
REFERENCES region
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE complaint
  ADD CONSTRAINT complaint_orders_FK FOREIGN KEY
  (
    order_id
  )
REFERENCES orders
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE complaint
  ADD CONSTRAINT complaint_statuses_FK FOREIGN KEY
  (
    status_id
  )
REFERENCES statuses
  (
    id
  );


ALTER TABLE complaint
  ADD CONSTRAINT complaint_user_FK FOREIGN KEY
  (
    customer_id
  )
REFERENCES users
  (
    id
  );


ALTER TABLE complaint
  ADD CONSTRAINT complaint_user_FKv1 FOREIGN KEY
  (
    pmg_id
  )
REFERENCES users
  (
    id
  );


ALTER TABLE groups
  ADD CONSTRAINT groups_discount_FK FOREIGN KEY
  (
    discount_id
  )
REFERENCES discount
  (
    id
  );


ALTER TABLE history
  ADD CONSTRAINT history_complaint_FK FOREIGN KEY
  (
    complaint_id
  )
REFERENCES complaint
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE history
  ADD CONSTRAINT history_orders_FK FOREIGN KEY
  (
    order_id
  )
REFERENCES orders
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE history
  ADD CONSTRAINT history_product_FK FOREIGN KEY
  (
    product_id
  )
REFERENCES product
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE history
  ADD CONSTRAINT history_statuses_FK FOREIGN KEY
  (
    new_status_id
  )
REFERENCES statuses
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE orders
  ADD CONSTRAINT orders_product_FK FOREIGN KEY
  (
    product_id
  )
REFERENCES product
  (
    id
  );


ALTER TABLE orders
  ADD CONSTRAINT orders_statuses_FK FOREIGN KEY
  (
    status_id
  )
REFERENCES statuses
  (
    id
  );


ALTER TABLE orders
  ADD CONSTRAINT orders_user_FK FOREIGN KEY
  (
    customer_id
  )
REFERENCES users
  (
    id
  );


ALTER TABLE orders
  ADD CONSTRAINT orders_user_FKv1 FOREIGN KEY
  (
    csr_id
  )
REFERENCES users
  (
    id
  );


ALTER TABLE product
  ADD CONSTRAINT product_discount_FK FOREIGN KEY
  (
    discount_id
  )
REFERENCES discount
  (
    id
  );


ALTER TABLE product
  ADD CONSTRAINT product_group_FK FOREIGN KEY
  (
    group_id
  )
REFERENCES groups
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE product
  ADD CONSTRAINT product_statuses_FK FOREIGN KEY
  (
    status_id
  )
REFERENCES statuses
  (
    id
  );


ALTER TABLE region_groups
  ADD CONSTRAINT region_groups_group_FK FOREIGN KEY
  (
    group_id
  )
REFERENCES groups
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE region_groups
  ADD CONSTRAINT region_groups_region_FK FOREIGN KEY
  (
    region_id
  )
REFERENCES region
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE user_attempts
  ADD CONSTRAINT user_attempts_user_FK FOREIGN KEY
  (
    user_id
  )
REFERENCES users
  (
    id
  )
ON DELETE CASCADE;


ALTER TABLE users
  ADD CONSTRAINT users_address_FK FOREIGN KEY
  (
    address_id
  )
REFERENCES address
  (
    id
  );


ALTER TABLE users
  ADD CONSTRAINT users_organization_FK FOREIGN KEY
  (
    org_id
  )
REFERENCES organization
  (
    id
  );


ALTER TABLE users
  ADD CONSTRAINT users_user_roles_FK FOREIGN KEY
  (
    user_role_id
  )
REFERENCES user_roles
  (
    id
  );

CREATE TABLE user_register_token
(
  id        BIGSERIAL    NOT NULL,
  user_id   INTEGER      NOT NULL,
  token     VARCHAR(60)  NOT NULL,
  date_send TIMESTAMP(0) NOT NULL,
  used      BOOLEAN      NOT NULL
);

ALTER TABLE user_register_token
  ADD CONSTRAINT user_register_token_PK PRIMARY KEY (id);

ALTER TABLE user_register_token
  ADD CONSTRAINT user_register_token_users_FK FOREIGN KEY
  (
    user_id
  )
REFERENCES users
  (
    id
  )
ON DELETE CASCADE;


INSERT INTO user_roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO user_roles (id, name) VALUES (2, 'ROLE_CUSTOMER');
INSERT INTO user_roles (id, name) VALUES (3, 'ROLE_CSR');
INSERT INTO user_roles (id, name) VALUES (4, 'ROLE_PMG');

-- password - 123123
INSERT INTO "users" (
  password, first_name, middle_name, last_name, phone, email, enable, account_non_locked,
  contact_person, address_id, user_role_id, org_id)
VALUES ('$2a$10$mJfq5rmvQR66o1xBN2xMzeptwYaxogOToWzvbVUeEHol.pe/jABia', 'John', 'Doe', 'Doevich',
                                                                        '0000000000', 'admin@gmail.com', TRUE, TRUE,
                                                                        FALSE, NULL, 1, NULL);
INSERT INTO public.statuses (id, name) VALUES (1, 'OPEN');
INSERT INTO public.statuses (id, name) VALUES (2, 'SOLVING');
INSERT INTO public.statuses (id, name) VALUES (3, 'CLOSED');
INSERT INTO public.statuses (id, name) VALUES (4, 'NEW');
INSERT INTO public.statuses (id, name) VALUES (5, 'PROCESSING');
INSERT INTO public.statuses (id, name) VALUES (6, 'ACTIVE');
INSERT INTO public.statuses (id, name) VALUES (7, 'DISABLED');
INSERT INTO public.statuses (id, name) VALUES (8, 'PAUSED');
INSERT INTO public.statuses (id, name) VALUES (9, 'REQUEST_TO_RESUME');
INSERT INTO public.statuses (id, name) VALUES (10, 'REQUEST_TO_PAUSE');
INSERT INTO public.statuses (id, name) VALUES (11, 'REQUEST_TO_DISABLE');
INSERT INTO public.statuses (id, name) VALUES (12, 'PLANNED');
INSERT INTO public.statuses (id, name) VALUES (13, 'ACTUAL');
INSERT INTO public.statuses (id, name) VALUES (14, 'OUTDATED');

CREATE OR REPLACE FUNCTION update_product(ids               BIGINT [], new_discount_id BIGINT,
                                          new_group_id      BIGINT,
                                          new_default_price DOUBLE PRECISION, new_description TEXT)
  RETURNS INT AS
'
DECLARE rows_updated INT;
BEGIN
  UPDATE product
  SET
    group_id      = COALESCE(new_group_id, group_id),
    description   = COALESCE(new_description, description),
    discount_id   = COALESCE(new_discount_id, discount_id),
    default_price = COALESCE(new_default_price, default_price)
  WHERE id IN (SELECT *
               FROM unnest(ids));
  GET DIAGNOSTICS rows_updated = ROW_COUNT;
  RETURN rows_updated;
END
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_discount(ids            BIGINT [], new_active BOOLEAN,
                                           new_percentage DOUBLE PRECISION, new_description TEXT)
  RETURNS INT AS
'
DECLARE rows_updated INT;
BEGIN
  UPDATE discount
  SET
    active      = COALESCE(new_active, active),
    description = COALESCE(new_description, description),
    percentage  = COALESCE(new_percentage, percentage)
  WHERE id IN (SELECT *
               FROM unnest(ids));
  GET DIAGNOSTICS rows_updated = ROW_COUNT;
  RETURN rows_updated;
END
'
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION update_groups(ids            BIGINT [], new_discount_id BIGINT)
  RETURNS INT AS
'
DECLARE rows_updated INT;
BEGIN
  UPDATE groups
  SET
    discount_id   = COALESCE(new_discount_id, discount_id)
  WHERE id IN (SELECT *
               FROM unnest(ids));
  GET DIAGNOSTICS rows_updated = ROW_COUNT;
  RETURN rows_updated;
END
'
LANGUAGE plpgsql;

COMMIT;
