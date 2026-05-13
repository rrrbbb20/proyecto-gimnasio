CREATE TABLE sede
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    nombre        VARCHAR(255) NULL,
    direccion     VARCHAR(255) NULL,
    hora_apertura INT NULL,
    hora_cierre   INT NULL,
    CONSTRAINT pk_sede PRIMARY KEY (id)
);