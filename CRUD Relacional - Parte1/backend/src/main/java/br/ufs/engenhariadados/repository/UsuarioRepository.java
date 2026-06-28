package br.ufs.engenhariadados.repository;

import br.ufs.engenhariadados.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, BigDecimal> {

    boolean existsByLogin(String login);

    Optional<Usuario> findByLogin(String login);
}