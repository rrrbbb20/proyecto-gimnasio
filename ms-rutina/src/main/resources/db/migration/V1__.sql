CREATE TABLE ejercicios
(
    id_ejercicio     BIGINT AUTO_INCREMENT NOT NULL,
    nombre_ejercicio VARCHAR(255) NULL,
    tipo_ejercicio   VARCHAR(255) NULL,
    zona_ejercitada  VARCHAR(255) NULL,
    repeticiones     INT NULL,
    CONSTRAINT pk_ejercicios PRIMARY KEY (id_ejercicio)
);

CREATE TABLE rutina
(
    id_rutina       BIGINT AUTO_INCREMENT NOT NULL,
    nombre_rutina   VARCHAR(255) NULL,
    duracion        VARCHAR(255) NULL,
    tiempo_descanso INT NULL,
    CONSTRAINT pk_rutina PRIMARY KEY (id_rutina)
);

CREATE TABLE rutina_ejercicio
(
    id_ejercicio BIGINT NOT NULL,
    id_rutina    BIGINT NOT NULL,
    CONSTRAINT pk_rutina_ejercicio PRIMARY KEY (id_ejercicio, id_rutina)
);

ALTER TABLE rutina_ejercicio
    ADD CONSTRAINT fk_ruteje_on_ejercicio FOREIGN KEY (id_ejercicio) REFERENCES ejercicios (id_ejercicio);

ALTER TABLE rutina_ejercicio
    ADD CONSTRAINT fk_ruteje_on_rutina FOREIGN KEY (id_rutina) REFERENCES rutina (id_rutina);