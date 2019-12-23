package br.com.hbsis.vendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
interface IVendasRepository extends JpaRepository<Vendas, Long> {
    Optional<Vendas> findById(Long id);

    List<Vendas> findAllFornecedorById(Long idFornecedor);
}