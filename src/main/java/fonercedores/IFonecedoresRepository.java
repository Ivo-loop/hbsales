package fonercedores;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFonecedoresRepository extends JpaRepository<Fornecedor, Long> {
}
