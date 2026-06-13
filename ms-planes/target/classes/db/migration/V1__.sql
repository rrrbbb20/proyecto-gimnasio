CREATE TABLE pagos
(
    pago_id         BIGINT AUTO_INCREMENT NOT NULL,
    tipo_pago       VARCHAR(255) NULL,
    numero_tarjeta  INT NULL,
    fecha_caducidad VARCHAR(255) NULL,
    cvc             INT NULL,
    CONSTRAINT pk_pagos PRIMARY KEY (pago_id)
);

CREATE TABLE planes
(
    planes_id   BIGINT AUTO_INCREMENT NOT NULL,
    nombre_plan VARCHAR(255) NULL,
    precio_plan INT NULL,
    pago_id     BIGINT NULL,
    CONSTRAINT pk_planes PRIMARY KEY (planes_id)
);

ALTER TABLE planes
    ADD CONSTRAINT uc_planes_pago UNIQUE (pago_id);

ALTER TABLE planes
    ADD CONSTRAINT FK_PLANES_ON_PAGO FOREIGN KEY (pago_id) REFERENCES pagos (pago_id);