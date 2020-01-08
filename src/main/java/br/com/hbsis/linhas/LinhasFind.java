package br.com.hbsis.linhas;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LinhasFind {
    private final ILinhasRepository iLinhasRepository;

    public LinhasFind(ILinhasRepository iLinhasRepository) {
        this.iLinhasRepository = iLinhasRepository;
    }

    //Busca tudo
    List<Linhas> findAll() {
        return iLinhasRepository.findAll();
    }

    //puxa o linhas pelo Id dela
    Linhas findById(Long id) {
        Optional<Linhas> linhaOptional = this.iLinhasRepository.findById(id);

        if (linhaOptional.isPresent()) {
            return linhaOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa a linha pelo Id dele, retorna como DTO
    LinhasDTO findByIdDTO(Long id) {
        Optional<Linhas> linhaOptional = this.iLinhasRepository.findById(id);

        if (linhaOptional.isPresent()) {
            return LinhasDTO.of(linhaOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa as linhas pelo cod dela
    Optional<Linhas> findByCodLinhasOptional(String cod) {
        Optional<Linhas> linhaOptional = this.iLinhasRepository.findByCodLinhas(cod);

        if (linhaOptional.isPresent()) {
            return linhaOptional;
        }
        throw new IllegalArgumentException(String.format("Cod %s não existe", cod));
    }

    //puxa as linhas pelo cod dela
    LinhasDTO findByCodLinhasDTO(String cod) {
        Optional<Linhas> linhaOptional = this.iLinhasRepository.findByCodLinhas(cod);

        if (linhaOptional.isPresent()) {
            return LinhasDTO.of(linhaOptional.get());
        }
        throw new IllegalArgumentException(String.format("Cod %s não existe", cod));
    }
}
