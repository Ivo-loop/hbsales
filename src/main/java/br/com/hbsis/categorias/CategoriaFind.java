package br.com.hbsis.categorias;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoriaFind {
    private final ICategoriaRepository iCategoriaRepository;

    public CategoriaFind(ICategoriaRepository iCategoriaRepository) {
        this.iCategoriaRepository = iCategoriaRepository;
    }


    //busca a Categoria pelo Id
    public Categoria findById(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (categoriaOptional.isPresent()) {
            return categoriaOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //busca a Categoria pelo Id, retorna ele como DTO
    public CategoriaDTO findByIdDTO(Long id) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findById(id);

        if (categoriaOpcional.isPresent()) {
            return CategoriaDTO.of(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaDTO findByIdPost(Long id) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findById(id);

        if (categoriaOpcional.isPresent()) {
            return CategoriaDTO.of(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //Busca pelo cod da categoria
    public Optional<Categoria> findByCodCategoriaOptinal(String cod) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findByCodCategoria(cod);

        if (categoriaOpcional.isPresent()) {
            return categoriaOpcional;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", cod));
    }

    //Busca pelo cod da categoria
    public CategoriaDTO findByCodCategoria(String cod) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findByCodCategoria(cod);

        if (categoriaOpcional.isPresent()) {
            return CategoriaDTO.of(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", cod));
    }

    //busca toda categoria
    public List<Categoria> findAll() {
        return iCategoriaRepository.findAll();
    }
}
