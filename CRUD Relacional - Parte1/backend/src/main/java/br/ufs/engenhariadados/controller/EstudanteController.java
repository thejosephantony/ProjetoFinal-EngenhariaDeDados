package br.ufs.engenhariadados.controller;

import br.ufs.engenhariadados.dto.EstudanteRequestDTO;
import br.ufs.engenhariadados.dto.EstudanteResponseDTO;
import br.ufs.engenhariadados.service.EstudanteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudantes")
public class EstudanteController {

    private final EstudanteService estudanteService;

    public EstudanteController(EstudanteService estudanteService) {
        this.estudanteService = estudanteService;
    }

    @PostMapping
    public ResponseEntity<EstudanteResponseDTO> criar(@Valid @RequestBody EstudanteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudanteService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<EstudanteResponseDTO>> listar() {
        return ResponseEntity.ok(estudanteService.listar());
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<EstudanteResponseDTO> buscarPorMatricula(@PathVariable String matricula) {
        return ResponseEntity.ok(estudanteService.buscarPorMatricula(matricula));
    }

    @PutMapping("/{matricula}")
    public ResponseEntity<EstudanteResponseDTO> atualizar(
            @PathVariable String matricula,
            @Valid @RequestBody EstudanteRequestDTO dto
    ) {
        return ResponseEntity.ok(estudanteService.atualizar(matricula, dto));
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> remover(@PathVariable String matricula) {
        estudanteService.remover(matricula);
        return ResponseEntity.noContent().build();
    }
}