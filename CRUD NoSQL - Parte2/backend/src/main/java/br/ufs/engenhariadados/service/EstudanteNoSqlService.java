package br.ufs.engenhariadados.service;

import br.ufs.engenhariadados.dto.CursoVinculoDTO;
import br.ufs.engenhariadados.dto.EstudanteRequestDTO;
import br.ufs.engenhariadados.dto.EstudanteResponseDTO;
import br.ufs.engenhariadados.dto.VinculoDTO;
import br.ufs.engenhariadados.exception.ResourceNotFoundException;
import br.ufs.engenhariadados.model.nosql.CursoVinculo;
import br.ufs.engenhariadados.model.nosql.EstudanteDocument;
import br.ufs.engenhariadados.model.nosql.Vinculo;
import br.ufs.engenhariadados.repository.EstudanteNoSqlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudanteNoSqlService {

    private final EstudanteNoSqlRepository repository;

    public EstudanteResponseDTO criar(EstudanteRequestDTO request) {
        // Verifica se já existe CPF ou matrícula
        if (repository.findByCpf(request.getCpf()).isPresent()) {
            throw new DuplicateKeyException("CPF já cadastrado: " + request.getCpf());
        }
        if (repository.findByMatricula(request.getMatricula()).isPresent()) {
            throw new DuplicateKeyException("Matrícula já cadastrada: " + request.getMatricula());
        }

        EstudanteDocument document = toDocument(request);
        document = repository.save(document);
        return toResponse(document);
    }

    public List<EstudanteResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public EstudanteResponseDTO buscarPorCpf(String cpf) {
        EstudanteDocument document = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Estudante com CPF " + cpf + " não encontrado"));
        return toResponse(document);
    }

    public EstudanteResponseDTO atualizar(String cpf, EstudanteRequestDTO request) {
        // Verifica se o estudante existe
        EstudanteDocument existente = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Estudante com CPF " + cpf + " não encontrado"));

        // Se a matrícula for alterada, verifica se a nova já existe (se for diferente)
        if (!existente.getMatricula().equals(request.getMatricula())) {
            if (repository.findByMatricula(request.getMatricula()).isPresent()) {
                throw new DuplicateKeyException("Matrícula já cadastrada: " + request.getMatricula());
            }
        }

        // Atualiza todos os campos (substituição total)
        existente.setCpf(request.getCpf());          // não deve mudar, mas caso mude, o índice único vai reclamar
        existente.setNome(request.getNome());
        existente.setDataNascimento(request.getDataNascimento());
        existente.setEmail(request.getEmail());
        existente.setTelefone(request.getTelefone());
        existente.setLogin(request.getLogin());
        existente.setSenha(request.getSenha());
        existente.setMatricula(request.getMatricula());
        existente.setMc(request.getMc());
        existente.setAnoIngresso(request.getAnoIngresso());

        // Atualiza a lista de cursos (substitui totalmente)
        List<CursoVinculo> cursos = request.getCursos().stream()
                .map(this::toCursoVinculo)
                .collect(Collectors.toList());
        existente.setCursos(cursos);

        EstudanteDocument updated = repository.save(existente);
        return toResponse(updated);
    }

    public void deletar(String cpf) {
        EstudanteDocument existente = repository.findByCpf(cpf)
                .orElseThrow(() -> new ResourceNotFoundException("Estudante com CPF " + cpf + " não encontrado"));
        repository.delete(existente);
    }

    // --- Métodos de conversão ---

    private EstudanteDocument toDocument(EstudanteRequestDTO dto) {
        EstudanteDocument doc = new EstudanteDocument();
        doc.setCpf(dto.getCpf());
        doc.setNome(dto.getNome());
        doc.setDataNascimento(dto.getDataNascimento());
        doc.setEmail(dto.getEmail());
        doc.setTelefone(dto.getTelefone());
        doc.setLogin(dto.getLogin());
        doc.setSenha(dto.getSenha());
        doc.setMatricula(dto.getMatricula());
        doc.setMc(dto.getMc());
        doc.setAnoIngresso(dto.getAnoIngresso());
        if (dto.getCursos() != null) {
            List<CursoVinculo> cursos = dto.getCursos().stream()
                    .map(this::toCursoVinculo)
                    .collect(Collectors.toList());
            doc.setCursos(cursos);
        }
        return doc;
    }

    private CursoVinculo toCursoVinculo(CursoVinculoDTO dto) {
        CursoVinculo cv = new CursoVinculo();
        cv.setCursoId(dto.getCursoId());
        cv.setNome(dto.getNome());
        cv.setGrau(dto.getGrau());
        cv.setTurno(dto.getTurno());
        cv.setCampus(dto.getCampus());
        cv.setNivel(dto.getNivel());
        if (dto.getVinculo() != null) {
            Vinculo v = new Vinculo();
            v.setDataEntrada(dto.getVinculo().getDataEntrada());
            v.setStatus(dto.getVinculo().getStatus());
            v.setDataSaida(dto.getVinculo().getDataSaida());
            cv.setVinculo(v);
        }
        return cv;
    }

    private EstudanteResponseDTO toResponse(EstudanteDocument doc) {
        EstudanteResponseDTO dto = new EstudanteResponseDTO();
        dto.setId(doc.getId());
        dto.setCpf(doc.getCpf());
        dto.setNome(doc.getNome());
        dto.setDataNascimento(doc.getDataNascimento());
        dto.setEmail(doc.getEmail());
        dto.setTelefone(doc.getTelefone());
        dto.setLogin(doc.getLogin());
        dto.setMatricula(doc.getMatricula());
        dto.setMc(doc.getMc());
        dto.setAnoIngresso(doc.getAnoIngresso());
        if (doc.getCursos() != null) {
            List<CursoVinculoDTO> cursosDto = doc.getCursos().stream()
                    .map(this::toCursoVinculoDTO)
                    .collect(Collectors.toList());
            dto.setCursos(cursosDto);
        }
        return dto;
    }

    private CursoVinculoDTO toCursoVinculoDTO(CursoVinculo cv) {
        CursoVinculoDTO dto = new CursoVinculoDTO();
        dto.setCursoId(cv.getCursoId());
        dto.setNome(cv.getNome());
        dto.setGrau(cv.getGrau());
        dto.setTurno(cv.getTurno());
        dto.setCampus(cv.getCampus());
        dto.setNivel(cv.getNivel());
        if (cv.getVinculo() != null) {
            VinculoDTO vDto = new VinculoDTO();
            vDto.setDataEntrada(cv.getVinculo().getDataEntrada());
            vDto.setStatus(cv.getVinculo().getStatus());
            vDto.setDataSaida(cv.getVinculo().getDataSaida());
            dto.setVinculo(vDto);
        }
        return dto;
    }
}