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

    public String codCategoria(Fornecedor fornecedor, Categoria categoria){
        String cnpj = fornecedor.getCnpj();
        String cod =(CAT + cnpj.substring(10, 14) + categoria.getCodCategoria().substring(7, 10));
        return cod;
    }

    public String codCategoria(Categoria categoria, String number) {
        String cnpj = categoria.getFornecedor().getCnpj();
        String cod =(CAT + cnpj.substring(10, 14) + number);
        return cod;
    }

    public String number(CategoriaDTO categoriaDTO){
        String cont = String.valueOf(categoriaDTO.getCodigo());
        while(cont.length() < 3) { cont = "0" + cont; }
        return cont;
    }
}
