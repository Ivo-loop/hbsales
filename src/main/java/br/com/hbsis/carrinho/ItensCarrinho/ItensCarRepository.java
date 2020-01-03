package br.com.hbsis.carrinho.ItensCarrinho;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
 interface ItensCarRepository extends JpaRepository<ItensCarrinho, Long> {
    Optional<ItensCarrinho> findByCarrinho_IdIs(Long idCarrinho);
    List<ItensCarrinho> findAllByCarrinho_IdIs(Long idCarrinho);
    List<ItensCarrinho> findAll();
}
