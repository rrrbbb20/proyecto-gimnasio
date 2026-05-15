CREATE TABLE encargado
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    nombre_completo  VARCHAR(255) NULL,
    run              VARCHAR(255) NULL,
    direccion        VARCHAR(255) NULL,
    fecha_nacimiento date NULL,
    CONSTRAINT pk_encargado PRIMARY KEY (id)
);