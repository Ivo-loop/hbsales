package br.com.hbsis.categorias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByCodCategoria(String cod);
    Optional<Categoria> findByFornecedor_IdIs(Long idCategoriaFornecedor);
    List<Categoria> findAllByFornecedor_IdIs(Long idFornecedor);
    boolean existsById(Long id);
}
