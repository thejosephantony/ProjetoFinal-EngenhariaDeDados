package br.ufs.engenhariadados.controller;

import br.ufs.engenhariadados.dto.CursoRequestDTO;
import br.ufs.engenhariadados.dto.CursoResponseDTO;
import br.ufs.engenhariadados.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<CursoResponseDTO> criar(@Valid @RequestBody CursoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(cursoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CursoRequestDTO dto
    ) {
        return ResponseEntity.ok(cursoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        cursoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}