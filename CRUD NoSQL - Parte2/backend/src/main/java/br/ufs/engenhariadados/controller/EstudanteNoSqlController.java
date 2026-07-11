package br.ufs.engenhariadados.controller;

import br.ufs.engenhariadados.dto.EstudanteRequestDTO;
import br.ufs.engenhariadados.dto.EstudanteResponseDTO;
import br.ufs.engenhariadados.service.EstudanteNoSqlService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nosql/estudantes")
@RequiredArgsConstructor
public class EstudanteNoSqlController {

    private final EstudanteNoSqlService service;

    @PostMapping
    public ResponseEntity<EstudanteResponseDTO> criar(@RequestBody @Valid EstudanteRequestDTO request) {
        EstudanteResponseDTO response = service.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EstudanteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<EstudanteResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarPorCpf(cpf));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<EstudanteResponseDTO> atualizar(
            @PathVariable String cpf,
            @RequestBody @Valid EstudanteRequestDTO request) {
        return ResponseEntity.ok(service.atualizar(cpf, request));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> deletar(@PathVariable String cpf) {
        service.deletar(cpf);
        return ResponseEntity.noContent().build();
    }
}