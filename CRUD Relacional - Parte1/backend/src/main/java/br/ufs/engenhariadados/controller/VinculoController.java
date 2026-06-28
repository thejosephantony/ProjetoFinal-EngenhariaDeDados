package br.ufs.engenhariadados.controller;

import br.ufs.engenhariadados.dto.VinculoRequestDTO;
import br.ufs.engenhariadados.dto.VinculoResponseDTO;
import br.ufs.engenhariadados.service.VinculoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vinculos")
public class VinculoController {

    private final VinculoService vinculoService;

    public VinculoController(VinculoService vinculoService) {
        this.vinculoService = vinculoService;
    }

    @PostMapping
    public ResponseEntity<VinculoResponseDTO> criar(@Valid @RequestBody VinculoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vinculoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<VinculoResponseDTO>> listar() {
        return ResponseEntity.ok(vinculoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VinculoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(vinculoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VinculoResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody VinculoRequestDTO dto
    ) {
        return ResponseEntity.ok(vinculoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        vinculoService.remover(id);
        return ResponseEntity.noContent().build();
    }
}