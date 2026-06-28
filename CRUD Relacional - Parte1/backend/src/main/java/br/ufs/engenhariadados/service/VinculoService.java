package br.ufs.engenhariadados.service;

import br.ufs.engenhariadados.dto.VinculoRequestDTO;
import br.ufs.engenhariadados.dto.VinculoResponseDTO;
import br.ufs.engenhariadados.exception.RecursoNaoEncontradoException;
import br.ufs.engenhariadados.exception.RegraNegocioException;
import br.ufs.engenhariadados.model.Curso;
import br.ufs.engenhariadados.model.Estudante;
import br.ufs.engenhariadados.model.Vinculo;
import br.ufs.engenhariadados.repository.CursoRepository;
import br.ufs.engenhariadados.repository.EstudanteRepository;
import br.ufs.engenhariadados.repository.VinculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class VinculoService {

    private static final Set<String> STATUS_VALIDOS = Set.of("Ativo", "Cancelada", "Formando", "Graduado");

    private final VinculoRepository vinculoRepository;
    private final EstudanteRepository estudanteRepository;
    private final CursoRepository cursoRepository;

    public VinculoService(
            VinculoRepository vinculoRepository,
            EstudanteRepository estudanteRepository,
            CursoRepository cursoRepository
    ) {
        this.vinculoRepository = vinculoRepository;
        this.estudanteRepository = estudanteRepository;
        this.cursoRepository = cursoRepository;
    }

    public VinculoResponseDTO criar(VinculoRequestDTO dto) {
        validarStatus(dto.status());

        Estudante estudante = buscarEstudante(dto.matriculaEstudante());
        Curso curso = buscarCurso(dto.cursoId());

        Vinculo vinculo = new Vinculo();
        vinculo.setEstudante(estudante);
        vinculo.setCurso(curso);
        vinculo.setDataEntrada(dto.dataEntrada());
        vinculo.setStatus(dto.status());
        vinculo.setDataSaida(dto.dataSaida());

        return VinculoResponseDTO.fromEntity(vinculoRepository.save(vinculo));
    }

    public List<VinculoResponseDTO> listar() {
        return vinculoRepository.findAll()
                .stream()
                .map(VinculoResponseDTO::fromEntity)
                .toList();
    }

    public VinculoResponseDTO buscarPorId(Integer id) {
        return VinculoResponseDTO.fromEntity(buscarEntidadePorId(id));
    }

    public VinculoResponseDTO atualizar(Integer id, VinculoRequestDTO dto) {
        validarStatus(dto.status());

        Vinculo vinculo = buscarEntidadePorId(id);
        Estudante estudante = buscarEstudante(dto.matriculaEstudante());
        Curso curso = buscarCurso(dto.cursoId());

        vinculo.setEstudante(estudante);
        vinculo.setCurso(curso);
        vinculo.setDataEntrada(dto.dataEntrada());
        vinculo.setStatus(dto.status());
        vinculo.setDataSaida(dto.dataSaida());

        return VinculoResponseDTO.fromEntity(vinculoRepository.save(vinculo));
    }

    public void remover(Integer id) {
        Vinculo vinculo = buscarEntidadePorId(id);
        vinculoRepository.delete(vinculo);
    }

    private Vinculo buscarEntidadePorId(Integer id) {
        return vinculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Vínculo com ID " + id + " não encontrado."
                ));
    }

    private Estudante buscarEstudante(String matricula) {
        return estudanteRepository.findById(matricula)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estudante com matrícula " + matricula + " não encontrado."
                ));
    }

    private Curso buscarCurso(Integer cursoId) {
        return cursoRepository.findById(cursoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Curso com ID " + cursoId + " não encontrado."
                ));
    }

    private void validarStatus(String status) {
        if (!STATUS_VALIDOS.contains(status)) {
            throw new RegraNegocioException("Status inválido. Valores válidos: " + STATUS_VALIDOS);
        }
    }
}