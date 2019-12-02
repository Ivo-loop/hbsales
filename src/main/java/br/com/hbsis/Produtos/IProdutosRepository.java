package br.com.hbsis.Produtos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IProdutosRepository extends JpaRepository<Produtos, Long> {
}
