package br.ufs.engenhariadados.service;

import br.ufs.engenhariadados.dto.CursoRequestDTO;
import br.ufs.engenhariadados.dto.CursoResponseDTO;
import br.ufs.engenhariadados.exception.RecursoNaoEncontradoException;
import br.ufs.engenhariadados.exception.RegraNegocioException;
import br.ufs.engenhariadados.model.Curso;
import br.ufs.engenhariadados.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CursoService {

    private static final Set<String> GRAUS_VALIDOS = Set.of("Bacharelado", "Licenciatura Plena");
    private static final Set<String> TURNOS_VALIDOS = Set.of("Matutino", "Vespertino", "Noturno", "Turno Indefinido");
    private static final Set<String> NIVEIS_VALIDOS = Set.of("Graduação", "Mestrado", "Doutorado", "Lato");

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public CursoResponseDTO criar(CursoRequestDTO dto) {
        validarEnums(dto);

        Curso curso = new Curso();
        preencherCurso(curso, dto);

        return CursoResponseDTO.fromEntity(cursoRepository.save(curso));
    }

    public List<CursoResponseDTO> listar() {
        return cursoRepository.findAll()
                .stream()
                .map(CursoResponseDTO::fromEntity)
                .toList();
    }

    public CursoResponseDTO buscarPorId(Integer id) {
        return CursoResponseDTO.fromEntity(buscarEntidadePorId(id));
    }

    public CursoResponseDTO atualizar(Integer id, CursoRequestDTO dto) {
        validarEnums(dto);

        Curso curso = buscarEntidadePorId(id);
        preencherCurso(curso, dto);

        return CursoResponseDTO.fromEntity(cursoRepository.save(curso));
    }

    public void remover(Integer id) {
        Curso curso = buscarEntidadePorId(id);
        cursoRepository.delete(curso);
    }

    private Curso buscarEntidadePorId(Integer id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Curso com ID " + id + " não encontrado."
                ));
    }

    private void preencherCurso(Curso curso, CursoRequestDTO dto) {
        curso.setNome(dto.nome());
        curso.setGrau(dto.grau());
        curso.setTurno(dto.turno());
        curso.setCampus(dto.campus());
        curso.setNivel(dto.nivel());
    }

    private void validarEnums(CursoRequestDTO dto) {
        if (dto.grau() != null && !GRAUS_VALIDOS.contains(dto.grau())) {
            throw new RegraNegocioException("Grau inválido. Valores válidos: " + GRAUS_VALIDOS);
        }

        if (!TURNOS_VALIDOS.contains(dto.turno())) {
            throw new RegraNegocioException("Turno inválido. Valores válidos: " + TURNOS_VALIDOS);
        }

        if (dto.nivel() != null && !NIVEIS_VALIDOS.contains(dto.nivel())) {
            throw new RegraNegocioException("Nível inválido. Valores válidos: " + NIVEIS_VALIDOS);
        }
    }
}