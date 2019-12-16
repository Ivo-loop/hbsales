package br.com.hbsis.linhas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface ILinhasRepository extends JpaRepository<Linhas, Long> {
    Optional<Linhas> findByCodLinhas(String cod);
}
