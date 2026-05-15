CREATE TABLE inscripcion_clase (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   id_clase BIGINT NOT NULL,
   id_cliente BIGINT NOT NULL,
   fecha_inscripcion DATE NOT NULL,
   hora_inscripcion TIME NOT NULL
);