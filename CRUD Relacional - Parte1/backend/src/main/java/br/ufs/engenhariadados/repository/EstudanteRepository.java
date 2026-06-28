package br.ufs.engenhariadados.repository;

import br.ufs.engenhariadados.model.Estudante;
import br.ufs.engenhariadados.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstudanteRepository extends JpaRepository<Estudante, String> {

    boolean existsByUsuario(Usuario usuario);

    Optional<Estudante> findByUsuario(Usuario usuario);
}