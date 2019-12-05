package br.com.hbsis;

import br.com.hbsis.categorias.Categoria;
import br.com.hbsis.categorias.CategoriaDTO;
import br.com.hbsis.categorias.CategoriaService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.FornecedoresDTO;
import br.com.hbsis.linhas.Linhas;
import br.com.hbsis.linhas.LinhasDTO;
import br.com.hbsis.linhas.LinhasService;
import br.com.hbsis.produtos.ProdutoService;
import br.com.hbsis.produtos.Produtos;
import br.com.hbsis.produtos.ProdutosDTO;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PopuladorComponent {

    //- Essa classe faz a populaçao do Database, por favor nao mexer
    // porfavor conferir se as primary foram resetadas

    //caso o banco esteja vazio, tenho que popular
    private final FornecedorService fornecedorService;
    private final CategoriaService categoriaService;
    private final LinhasService linhasService;
    private final ProdutoService produtoService;

    public PopuladorComponent(FornecedorService fornecedorService, CategoriaService categoriaService, LinhasService linhasService, ProdutoService produtoService) {
        this.fornecedorService = fornecedorService;
        this.categoriaService = categoriaService;
        this.linhasService = linhasService;
        this.produtoService = produtoService;
    }

    @PostConstruct
    public void posConstruct() {
        List<Fornecedor> buscaFornecedor = fornecedorService.findAll();
        List<Categoria> conferiCategoria = categoriaService.findAll();
        List<Linhas> conferilinhas = linhasService.findAll();
        List<Produtos> conferiProdutos = produtoService.findAll();

        if (buscaFornecedor.isEmpty()) {
            //fornecedor
            String[][] fornecedor = {{"rosberto", "tioberto", "voberto"},
                    {"11111111111111", "11111111111112", "11111111111123"},
                    {"rosberto", "tioberto", "voberto"},
                    {"1", "12", "123"}, {"1", "12", "123"},
                    {"rosberto", "tioberto", "voberto"}
            };
            for (int a = 0; a <= 2; ) {
                FornecedoresDTO fonecedoresDTO = new FornecedoresDTO();
                fonecedoresDTO.setRazao(fornecedor[0][a]);
                fonecedoresDTO.setCnpj(fornecedor[1][a]);
                fonecedoresDTO.setNomeFantasia(fornecedor[2][a]);
                fonecedoresDTO.setEndereco(fornecedor[3][a]);
                fonecedoresDTO.setTelefone(fornecedor[4][a]);
                fonecedoresDTO.setEmail(fornecedor[5][a]);

                fornecedorService.save(fonecedoresDTO);
                a++;
            }
        }
        if (!buscaFornecedor.isEmpty() && conferiCategoria.isEmpty()) {
            String[][] categorias = {{"cerveja", "refri", "suco"},
                    {"1", "2", "3"}, {"1", "2", "3"}};
            for (int a = 0; a <= 2; ) {
                CategoriaDTO categoriaDTO = new CategoriaDTO();
                categoriaDTO.setNomeCategoria(categorias[0][a]);
                categoriaDTO.setCodCategoria(categorias[1][a]);
                categoriaDTO.setIdCategoriaFornecedor(Long.parseLong(categorias[2][a]));

                categoriaService.save(categoriaDTO);
                a++;
            }
        }
        List<Categoria> buscaCategoria = categoriaService.findAll();
        if (!buscaCategoria.isEmpty() && conferilinhas.isEmpty()) {
            String[][] linhas = {{"antarctica", "brahma", "skol", "soda", "citrus", "do bem"},
                    {"1", "2", "3", "4", "5", "6"},
                    {"1", "1", "1", "2", "2", "3"}};
            for (int a = 0; a <= 5; ) {
                LinhasDTO linhasDTO = new LinhasDTO();
                linhasDTO.setNomeLinhas(linhas[0][a]);
                linhasDTO.setCodLinhas(linhas[1][a]);
                linhasDTO.setidLinhasCategoria(Long.parseLong(linhas[2][a]));

                linhasService.save(linhasDTO);
                a++;
            }
        }
        List<Linhas> buscaLinhas = linhasService.findAll();
        if (!buscaLinhas.isEmpty() && conferiProdutos.isEmpty()) {
            String[][] produtos = {
                    {"antarctica", "1", "2.95", "1", "12", "7", "2019-02-03T00:00:00"},
                    {"brahma", "2", "2.45", "1", "12", "6", "2019-02-03T00:00:00"},
                    {"skol", "3", "2.65", "1", "12", "5", "2019-02-03T00:00:00"},
                    {"soda", "4", "4.00", "2", "9", "4", "2019-02-03T00:00:00"},
                    {"citrus", "5", "5.50", "2", "6", "3", "2019-02-03T00:00:00"},
                    {"do bem", "6", "7.89", "3", "8", "2", "2019-02-03T00:00:00"},
            };
            for (String[] produto : produtos) {
                ProdutosDTO produtosDTO = new ProdutosDTO(
                        null,
                        produto[0],
                        produto[1],
                        Float.parseFloat(produto[2]),
                        Long.parseLong(produto[3]),
                        Float.parseFloat(produto[4]),
                        Float.parseFloat(produto[5]),
                        LocalDateTime.parse(produto[6])
                );
                produtoService.save(produtosDTO);
                System.out.println("deu bom kk");
            }
        }
    }
}

