package br.ufs.engenhariadados.repository;

import br.ufs.engenhariadados.model.nosql.EstudanteDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudanteNoSqlRepository extends MongoRepository<EstudanteDocument, String> {
    Optional<EstudanteDocument> findByCpf(String cpf);
    Optional<EstudanteDocument> findByMatricula(String matricula);
    void deleteByCpf(String cpf);
}