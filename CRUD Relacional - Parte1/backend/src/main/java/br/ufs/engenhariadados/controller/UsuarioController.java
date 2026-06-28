package br.ufs.engenhariadados.controller;

import br.ufs.engenhariadados.dto.UsuarioRequestDTO;
import br.ufs.engenhariadados.dto.UsuarioResponseDTO;
import br.ufs.engenhariadados.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorCpf(@PathVariable BigDecimal cpf) {
        return ResponseEntity.ok(usuarioService.buscarPorCpf(cpf));
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(
            @PathVariable BigDecimal cpf,
            @Valid @RequestBody UsuarioRequestDTO dto
    ) {
        return ResponseEntity.ok(usuarioService.atualizar(cpf, dto));
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> remover(@PathVariable BigDecimal cpf) {
        usuarioService.remover(cpf);
        return ResponseEntity.noContent().build();
    }
}