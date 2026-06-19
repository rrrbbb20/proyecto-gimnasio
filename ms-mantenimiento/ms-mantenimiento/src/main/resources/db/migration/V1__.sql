CREATE TABLE mantenimiento
(
    id                        BIGINT AUTO_INCREMENT NOT NULL,
    empresa                   VARCHAR(255) NULL,
    descripcion_mantenimiento VARCHAR(255) NULL,
    fecha_mantenimiento       date NULL,
    precio DOUBLE NULL,
    CONSTRAINT pk_mantenimiento PRIMARY KEY (id)
);