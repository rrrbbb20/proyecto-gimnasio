CREATE TABLE cliente
(
    id                      BIGINT AUTO_INCREMENT NOT NULL,
    nombre_completo_cliente VARCHAR(255) NULL,
    run                     VARCHAR(255) NULL,
    fecha_nac_cliente       date NULL,
    tipo_plan               VARCHAR(255) NULL,
    CONSTRAINT pk_cliente PRIMARY KEY (id)
);