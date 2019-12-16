package br.com.hbsis.fornecedor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface IFornecedoresRepository extends JpaRepository<Fornecedor, Long> {
    @Override
    boolean existsById(Long id);
    Optional<Fornecedor> findByCnpj(String cnpj);
}