package br.ufs.engenhariadados.service;

import br.ufs.engenhariadados.dto.UsuarioRequestDTO;
import br.ufs.engenhariadados.dto.UsuarioResponseDTO;
import br.ufs.engenhariadados.exception.RecursoNaoEncontradoException;
import br.ufs.engenhariadados.exception.RegraNegocioException;
import br.ufs.engenhariadados.model.Usuario;
import br.ufs.engenhariadados.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsById(dto.cpf())) {
            throw new RegraNegocioException("Já existe um usuário com este CPF.");
        }

        if (temTexto(dto.login()) && usuarioRepository.existsByLogin(dto.login())) {
            throw new RegraNegocioException("Já existe um usuário com este login.");
        }

        Usuario usuario = new Usuario();
        preencherUsuario(usuario, dto);

        return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::fromEntity)
                .toList();
    }

    public UsuarioResponseDTO buscarPorCpf(BigDecimal cpf) {
        return UsuarioResponseDTO.fromEntity(buscarEntidadePorCpf(cpf));
    }

    public UsuarioResponseDTO atualizar(BigDecimal cpf, UsuarioRequestDTO dto) {
        Usuario usuario = buscarEntidadePorCpf(cpf);

        if (!cpf.equals(dto.cpf())) {
            throw new RegraNegocioException("Não é permitido alterar o CPF do usuário.");
        }

        boolean loginAlterado = temTexto(dto.login())
                && usuario.getLogin() != null
                && !usuario.getLogin().equalsIgnoreCase(dto.login());

        boolean loginNovo = temTexto(dto.login()) && usuario.getLogin() == null;

        if ((loginAlterado || loginNovo) && usuarioRepository.existsByLogin(dto.login())) {
            throw new RegraNegocioException("Já existe outro usuário com este login.");
        }

        preencherUsuario(usuario, dto);

        return UsuarioResponseDTO.fromEntity(usuarioRepository.save(usuario));
    }

    public void remover(BigDecimal cpf) {
        Usuario usuario = buscarEntidadePorCpf(cpf);
        usuarioRepository.delete(usuario);
    }

    private Usuario buscarEntidadePorCpf(BigDecimal cpf) {
        return usuarioRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com CPF " + cpf + " não encontrado."
                ));
    }

    private void preencherUsuario(Usuario usuario, UsuarioRequestDTO dto) {
        usuario.setCpf(dto.cpf());
        usuario.setNome(dto.nome());
        usuario.setDataNascimento(dto.dataNascimento());
        usuario.setEmail(dto.email() == null ? null : dto.email().toArray(String[]::new));
        usuario.setTelefone(dto.telefone() == null ? null : dto.telefone().toArray(String[]::new));
        usuario.setLogin(dto.login());
        usuario.setSenha(dto.senha());
    }

    private boolean temTexto(String valor) {
        return valor != null && !valor.isBlank();
    }
}