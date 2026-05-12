CREATE TABLE pagos
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    tipo_pago       VARCHAR(255) NULL,
    numero_tarjeta  INT NULL,
    fecha_caducidad VARCHAR(255) NULL,
    cvc             INT NULL,
    CONSTRAINT pk_pagos PRIMARY KEY (id)
);

CREATE TABLE planes
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    nombre_plan VARCHAR(255) NULL,
    precio_plan INT NULL,
    id_pago     BIGINT NULL,
    CONSTRAINT pk_planes PRIMARY KEY (id)
);

ALTER TABLE planes
    ADD CONSTRAINT uc_planes_id_pago UNIQUE (id_pago);

ALTER TABLE planes
    ADD CONSTRAINT FK_PLANES_ON_ID_PAGO FOREIGN KEY (id_pago) REFERENCES pagos (id);