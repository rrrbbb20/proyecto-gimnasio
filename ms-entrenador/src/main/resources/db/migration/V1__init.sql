CREATE TABLE entrenador (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
    run VARCHAR(12) NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    fecha_nacimiento DATE NOT NULL
);