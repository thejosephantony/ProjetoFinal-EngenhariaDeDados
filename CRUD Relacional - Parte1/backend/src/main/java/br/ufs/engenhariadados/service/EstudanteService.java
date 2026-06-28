package br.ufs.engenhariadados.service;

import br.ufs.engenhariadados.dto.EstudanteRequestDTO;
import br.ufs.engenhariadados.dto.EstudanteResponseDTO;
import br.ufs.engenhariadados.exception.RecursoNaoEncontradoException;
import br.ufs.engenhariadados.exception.RegraNegocioException;
import br.ufs.engenhariadados.model.Estudante;
import br.ufs.engenhariadados.model.Usuario;
import br.ufs.engenhariadados.repository.EstudanteRepository;
import br.ufs.engenhariadados.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EstudanteService {

    private final EstudanteRepository estudanteRepository;
    private final UsuarioRepository usuarioRepository;

    public EstudanteService(
            EstudanteRepository estudanteRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.estudanteRepository = estudanteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public EstudanteResponseDTO criar(EstudanteRequestDTO dto) {
        if (estudanteRepository.existsById(dto.matricula())) {
            throw new RegraNegocioException("Já existe estudante com esta matrícula.");
        }

        Usuario usuario = buscarUsuarioSeInformado(dto.cpf());

        if (usuario != null && estudanteRepository.existsByUsuario(usuario)) {
            throw new RegraNegocioException("Já existe estudante vinculado a este CPF.");
        }

        Estudante estudante = new Estudante();
        estudante.setMatricula(dto.matricula());
        estudante.setUsuario(usuario);
        estudante.setMc(dto.mc());
        estudante.setAnoIngresso(dto.anoIngresso());

        return EstudanteResponseDTO.fromEntity(estudanteRepository.save(estudante));
    }

    public List<EstudanteResponseDTO> listar() {
        return estudanteRepository.findAll()
                .stream()
                .map(EstudanteResponseDTO::fromEntity)
                .toList();
    }

    public EstudanteResponseDTO buscarPorMatricula(String matricula) {
        return EstudanteResponseDTO.fromEntity(buscarEntidadePorMatricula(matricula));
    }

    public EstudanteResponseDTO atualizar(String matricula, EstudanteRequestDTO dto) {
    Estudante estudante = buscarEntidadePorMatricula(matricula);

    if (!matricula.equals(dto.matricula())) {
        throw new RegraNegocioException("Não é permitido alterar a matrícula do estudante.");
    }

    Usuario usuario = buscarUsuarioSeInformado(dto.cpf());

    boolean cpfAlterado = usuario != null
            && (estudante.getUsuario() == null
            || !estudante.getUsuario().getCpf().equals(dto.cpf()));

    if (cpfAlterado && estudanteRepository.existsByUsuario(usuario)) {
        throw new RegraNegocioException("Já existe outro estudante vinculado a este CPF.");
    }

    estudante.setUsuario(usuario);
    estudante.setMc(dto.mc());
    estudante.setAnoIngresso(dto.anoIngresso());

    return EstudanteResponseDTO.fromEntity(estudanteRepository.save(estudante));
    }

    public void remover(String matricula) {
        Estudante estudante = buscarEntidadePorMatricula(matricula);
        estudanteRepository.delete(estudante);
    }

    private Estudante buscarEntidadePorMatricula(String matricula) {
        return estudanteRepository.findById(matricula)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estudante com matrícula " + matricula + " não encontrado."
                ));
    }

    private Usuario buscarUsuarioSeInformado(BigDecimal cpf) {
        if (cpf == null) {
            return null;
        }

        return usuarioRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário com CPF " + cpf + " não encontrado."
                ));
    }
}