package br.com.hbsis.linhas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILinhasRepository extends JpaRepository<Linhas, Long> {
}
