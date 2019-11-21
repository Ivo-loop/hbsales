package br.com.hbsis.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Classe responsável pela comunciação com o banco de dados
 */
@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
}
