CREATE TABLE ejercicios
(
    id_ejercicio     BIGINT NOT NULL,
    nombre_ejercicio VARCHAR(255) NULL,
    tipo_ejercicio   VARCHAR(255) NULL,
    zona_ejercitada  VARCHAR(255) NULL,
    repeticiones     INT NULL,
    CONSTRAINT pk_ejercicios PRIMARY KEY (id_ejercicio)
);

CREATE TABLE rutina
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    nombre_rutina   VARCHAR(255) NULL,
    duracion        VARCHAR(255) NULL,
    tiempo_descanso INT NULL,
    id_ejercicio    BIGINT NULL,
    CONSTRAINT pk_rutina PRIMARY KEY (id)
);

ALTER TABLE rutina
    ADD CONSTRAINT FK_RUTINA_ON_ID_EJERCICIO FOREIGN KEY (id_ejercicio) REFERENCES ejercicios (id_ejercicio);