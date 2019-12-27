package br.com.hbsis.pedido.itens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
 interface ItensRepository  extends JpaRepository<Itens, Long> {
    Optional<Itens> findByPedido_IdIs(Long idPedido);
    List<Itens> findAllByPedido_IdIs(Long idPedido);
    List<Itens> findAll();
}
