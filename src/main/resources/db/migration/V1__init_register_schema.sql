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

-- Insume (inventory)
CREATE TABLE IF NOT EXISTS tb_insume (
    id_tb_insume UUID PRIMARY KEY,
    name         VARCHAR(150)   NOT NULL,
    brand        VARCHAR(100),
    sku_id       VARCHAR(50),
    quantity     INTEGER        NOT NULL DEFAULT 0,
    price        DECIMAL(10,2)  NOT NULL,
    unity_price  DECIMAL(10,2)  NOT NULL
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

-- Dados Iniciais de Insumos para Demonstração
INSERT INTO tb_insume (id_tb_insume, name, brand, sku_id, quantity, price, unity_price)
VALUES 
    (gen_random_uuid(), 'Óleo de Motor 5W30 Sintético', 'Motul', 'OIL-5W30', 50, 45.00, 45.00),
    (gen_random_uuid(), 'Filtro de Óleo', 'Bosch', 'FLT-OIL-01', 30, 28.50, 28.50),
    (gen_random_uuid(), 'Pastilha de Freio Dianteira', 'Cobreq', 'BRK-PAD-01', 20, 120.00, 120.00),
    (gen_random_uuid(), 'Filtro de Ar Condicionado', 'Fram', 'FLT-AIR-02', 15, 35.00, 35.00)
ON CONFLICT DO NOTHING;
