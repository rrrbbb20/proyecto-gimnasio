CREATE TABLE clase (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   nombre_clase VARCHAR(100) NOT NULL,
   descripcion VARCHAR(200),
   nivel_de_clase VARCHAR(100) NOT NULL,
   fecha_realizacion DATE NOT NULL,
   hora_realizacion TIME NOT NULL,
   cupos INT NOT NULL,
   estado BOOLEAN NOT NULL ,
   id_entrenador BIGINT NOT NULL
);

