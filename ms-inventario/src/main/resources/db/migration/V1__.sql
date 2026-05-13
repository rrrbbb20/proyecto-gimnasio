CREATE TABLE inventario
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    nombre         VARCHAR(255) NULL,
    descripcion    VARCHAR(255) NULL,
    precio DOUBLE NULL,
    fecha_registro date NULL,
    CONSTRAINT pk_inventario PRIMARY KEY (id)
);