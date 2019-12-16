package br.com.hbsis.produtos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface IProdutosRepository extends JpaRepository<Produtos, Long> {
    boolean existsById(Long id);
    Optional<Produtos> findById(Long id);
    Optional<Produtos> findByCodProdutos(String cod);
}
