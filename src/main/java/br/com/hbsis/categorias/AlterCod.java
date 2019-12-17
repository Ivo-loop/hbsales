package br.com.hbsis.categorias;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlterCod {
    private final ICategoriaRepository iCategoriaRepository;

    public AlterCod(ICategoriaRepository iCategoriaRepository) {
        this.iCategoriaRepository = iCategoriaRepository;
    }

    public void AlterCODCat(Fornecedor fornecedor) {
        List<Categoria> buscaCategorias = iCategoriaRepository.findAllByFornecedor_IdIs(fornecedor.getId());
        for (Categoria categorai : buscaCategorias) {

            categorai.setNomeCategoria(categorai.getNomeCategoria());
            categorai.setFornecedor(fornecedor);
            categorai.setCodCategoria("CAT" + fornecedor.getCnpj().substring(10, 14) + categorai.getCodCategoria().substring(7, 10));

            this.iCategoriaRepository.save(categorai);
        }
    }
}
