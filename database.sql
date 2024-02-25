CREATE TABLE IF NOT EXISTS menus
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(256) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);
CREATE TABLE IF NOT EXISTS menu_sections
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    menu_id    INT          NOT NULL,
    name       VARCHAR(256) NOT NULL,
    ordering   TINYINT,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    CONSTRAINT FK_menu_id FOREIGN KEY (menu_id) REFERENCES menus (id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS menu_items
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    menu_id         INT          NOT NULL,
    menu_section_id INT,
    name            VARCHAR(256) NOT NULL,
    description     TEXT,
    ingredients     TEXT,
    price           FLOAT        NOT NULL,
    picture         VARCHAR(256),
    created_at      TIMESTAMP    NOT NULL,
    updated_at      TIMESTAMP    NOT NULL,
    CONSTRAINT FOREIGN KEY (menu_id) REFERENCES menus (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (menu_section_id) REFERENCES menu_sections (id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS restaurant_tables
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    available_seats TINYINT   NOT NULL,
    joinable        BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP NOT NULL
);
CREATE TABLE IF NOT EXISTS customers
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    menu_id      INT,
    name         VARCHAR(256),
    finalized_at TIMESTAMP DEFAULT NULL,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL,
    CONSTRAINT FOREIGN KEY (menu_id) REFERENCES menus (id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS customer_restaurant_tables
(
    customer_id         INT NOT NULL,
    restaurant_table_id INT,
    CONSTRAINT FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (restaurant_table_id) REFERENCES restaurant_tables (id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS orders
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    customer_id  INT       NOT NULL,
    menu_item_id INT,
    quantity     INT       NOT NULL,
    ready        BOOLEAN DEFAULT FALSE,
    received     BOOLEAN DEFAULT FALSE,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL,
    CONSTRAINT FOREIGN KEY (customer_id) REFERENCES customers (id) ON DELETE CASCADE,
    CONSTRAINT FOREIGN KEY (menu_item_id) REFERENCES menu_items (id) ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS users
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    username   VARCHAR(256),
    password   VARCHAR(256),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX unique_username ON users (username);
CREATE TABLE IF NOT EXISTS persistent_tokens
(
    series      VARCHAR(256) NOT NULL PRIMARY KEY,
    username    VARCHAR(256) NOT NULL,
    token_value VARCHAR(256) NOT NULL,
    date        TIMESTAMP    NOT NULL
);
