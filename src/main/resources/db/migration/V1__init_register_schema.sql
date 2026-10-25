-- ============================================================
-- Register Service — Schema Inicial (Clientes, Veículos, Insumos, Usuários)
-- ============================================================

-- Customer
CREATE TABLE IF NOT EXISTS tb_customer (
    id_tb_customer UUID PRIMARY KEY,
    name           VARCHAR(150) NOT NULL,
    document       VARCHAR(14)  NOT NULL UNIQUE,
    email          VARCHAR(255) UNIQUE,
    phone          VARCHAR(20),
    birth_date     DATE,
    created        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Vehicle
CREATE TABLE IF NOT EXISTS tb_vehicle (
    id_tb_vehicle      UUID PRIMARY KEY,
    customer_id        UUID         NOT NULL REFERENCES tb_customer(id_tb_customer) ON DELETE CASCADE,
    plate              VARCHAR(7)   NOT NULL UNIQUE,
    brand              VARCHAR(50)  NOT NULL,
    model              VARCHAR(80)  NOT NULL,
    color              VARCHAR(30),
    manufacturing_date DATE,
    last_maintenance   TIMESTAMP,
    created            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- User (com autenticação BCrypt e CPF)
CREATE TABLE IF NOT EXISTS tb_user (
    id_tb_user UUID PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    function   VARCHAR(50)  NOT NULL,
    cpf        VARCHAR(14)  NOT NULL UNIQUE,
    email      VARCHAR(255) NOT NULL UNIQUE,
    phone      VARCHAR(20),
    password   VARCHAR(255) NOT NULL
);
