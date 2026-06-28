package br.ufs.engenhariadados.dto;

import br.ufs.engenhariadados.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public record UsuarioResponseDTO(
        BigDecimal cpf,
        String nome,
        LocalDate dataNascimento,
        List<String> email,
        List<String> telefone,
        String login
) {

    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getCpf(),
                usuario.getNome(),
                usuario.getDataNascimento(),
                usuario.getEmail() == null ? List.of() : Arrays.asList(usuario.getEmail()),
                usuario.getTelefone() == null ? List.of() : Arrays.asList(usuario.getTelefone()),
                usuario.getLogin()
        );
    }
}