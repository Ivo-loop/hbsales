package br.com.hbsis.categorias;

import br.com.hbsis.fornecedor.Fornecedor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlterCod {
    private final ICategoriaRepository iCategoriaRepository;
    private static final String CAT = "IVO";

    public AlterCod(ICategoriaRepository iCategoriaRepository) {
        this.iCategoriaRepository = iCategoriaRepository;
    }

    public void alterCODCat(Fornecedor fornecedor) {
        List<Categoria> buscaCategorias = iCategoriaRepository.findAllByFornecedor_IdIs(fornecedor.getId());
        for (Categoria categorai : buscaCategorias) {

            categorai.setNomeCategoria(categorai.getNomeCategoria());
            categorai.setFornecedor(fornecedor);
            //TODO: 18/12/2019 os códigos da categoria deverão ter IVO ao invés de CAT
            categorai.setCodCategoria(this.codCategoria(fornecedor,categorai));

            this.iCategoriaRepository.save(categorai);
        }
    }

     private String codCategoria(Fornecedor fornecedor, Categoria categoria){
        String cnpj = fornecedor.getCnpj();
         return (CAT + cnpj.substring(10, 14) + categoria.getCodCategoria().substring(7, 10));
    }

    String codCategoria(Categoria categoria, String number) {
        String cnpj = categoria.getFornecedor().getCnpj();
        return (CAT + cnpj.substring(10, 14) + number);
    }

    String number(CategoriaDTO categoriaDTO){
        StringBuilder cont = new StringBuilder(String.valueOf(categoriaDTO.getCodigo()));
        while(cont.length() < 3) { cont.insert(0, "0"); }
        return cont.toString();
    }
}
