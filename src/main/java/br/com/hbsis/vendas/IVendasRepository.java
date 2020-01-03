package br.com.hbsis.vendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Repository
interface IVendasRepository extends JpaRepository<Vendas, Long> {
    Optional<Vendas> findById(Long id);

    List<Vendas> findAllFornecedorById(Long idFornecedor);

    @Query("SELECT venda From Vendas venda " +
            "WHERE dia_inicial_vendas <= GETDATE() " +
            "AND id_vendas_fornecedor = :idFornecedor "+
            "ORDER BY dia_inicial_vendas DESC ")
    Optional<Vendas> findByIdFornecedor(@Param("idFornecedor")Long idFornecedor);

}