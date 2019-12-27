package br.com.hbsis.pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
 interface IPedidoRepository extends JpaRepository<Pedido, Long> {

 List<Pedido> findByFornecedor_Id(Long id);

 List<Pedido> findByfuncionario_Id(Long id);
}
