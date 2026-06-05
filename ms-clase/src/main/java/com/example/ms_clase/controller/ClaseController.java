package com.example.ms_clase.controller;


import com.example.ms_clase.dto.ApiResponse;
import com.example.ms_clase.dto.ClaseRequest;
import com.example.ms_clase.dto.ClaseResponse;
import com.example.ms_clase.service.ClaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
@Tag(
        name = "Clases",
        description = "Endpoints para gestion de Entrenadores"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clases")
public class ClaseController {

    private final ClaseService service;

    @Operation(
            summary = "anadir clase",
            description = "Recibe los datos de la clase, se valida y realiza la creación."
    )
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClaseResponse>> add(
            @Valid @RequestBody ClaseRequest c,
            @Parameter(hidden = true)@RequestHeader("Authorization") String token) {

        return ResponseEntity.status(201).body(
                ApiResponse.<ClaseResponse>builder().success(true)
                        .message("Clase anadida")
                        .data(service.add(c,token)).build()
        );
    }
    @Operation(
            summary = "encontrar clase proporcionando su id ",
            description = "permite retornar la clase buscada si existe "
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<ClaseResponse>> findById(
            @Parameter(description = "id de la clase a buscar", example = "1", required = true)@PathVariable Long id,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){
        return ResponseEntity.ok(ApiResponse.<ClaseResponse>builder().success(true)
                .message("Clase Encontrada")
                .data(service.findById(id,token))
                .build()
        );
    }
    @Operation(
            summary = "obtener a todas las clases registrados ",
            description = "permite retornar una lista de todos las clases que se encuentran registradas "
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<ClaseResponse>>> getAll(
            @Parameter(hidden = true)@RequestHeader("Authorization")
            String token){

        return ResponseEntity.ok(ApiResponse.<List<ClaseResponse>>builder()
                .success(true)
                .data(service.getAll(token))
                .build()
        );
    }
    @Operation(
            summary = "actualizar datos de una clase",
            description = "permite actualizar datos de una clase en particular mediante su id "
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ClaseResponse>> update(
            @Parameter(description = "id de la clase que se desea modificar", example = "1", required = true)@PathVariable Long id,
            @Valid @RequestBody ClaseRequest c,
            @Parameter(hidden = true)@RequestHeader("Authorization")String token){

        return ResponseEntity.ok(ApiResponse.<ClaseResponse>builder()
                .success(true)
                .message("Clase Actualizada")
                .data(service.update(id,c,token))
                .build()
        );
    }
    @Operation(
            summary = "eliminar una clase registrada",
            description = "permite eliminar una clase mediante su id "
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "id de la clase a eliminar", example = "1", required = true)
            @PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Clase eliminada")
                .build()
        );
    }
    @Operation(
            summary = "restar el cupo de una clase mediante su id",
            description = "si se inscribe una persona a la clase se procedera a restarle un cupo a ella"
    )
    @PatchMapping("/restar-cupo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> personaInscrita(
            @Parameter(description = "id de la clase a la que se necesita restar cupo", example = "1", required = true)
            @PathVariable Long id){
        service.personaInscrita(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder().message("Se Resta cupo").success(true)

                        .build()

        );

    }
    @Operation(
            summary = "sumar el cupo a una clase mediante su id",
            description = "si una persona retira su iscripcion a la clase se procedera sumarle un cupo a ella"
    )
    @PatchMapping("/sumar-cupo/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> removerInscripcion(
            @Parameter(description = "id de la clase a la que se necesita sumar cupo", example = "1", required = true)
            @PathVariable Long id){
        service.removerInscripcion(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder().message("Se suma cupo").success(true)

                        .build()

        );

    }
    @Operation(
            summary = "retornar una lista de clases proporcionando su nombre",
            description = "si la clase buscada existe retornara una lista de las clases encontradas por ese nombre "
    )
    @GetMapping("/buscar-por-nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<ApiResponse<List<ClaseResponse>>> buscarPorNombre(
            @Parameter(description = "nombre de la clase a buscar", example = "tenis", required = true)
            @PathVariable String nombre,
            @Parameter(hidden = true)@RequestHeader("Authorization")
            String token){

        return ResponseEntity.ok(ApiResponse.<List<ClaseResponse>>builder()
                .success(true)
                .data(service.buscarPorNombre(nombre,token))
                .build()
        );
    }
}
