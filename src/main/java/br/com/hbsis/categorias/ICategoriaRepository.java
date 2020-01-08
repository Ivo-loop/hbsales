package br.com.hbsis.categorias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ICategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByCodCategoria(String cod);
    List<Categoria> findAllByFornecedor_IdIs(Long idFornecedor);
}
