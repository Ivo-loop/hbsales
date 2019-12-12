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
                    {"12345678901011", "12345678901112", "12345678901213"},
                    {"rosberto", "tioberto", "voberto"},
                    {"1", "12", "123"},
                    {"5547123456789", "5547123456789", "5547123456789"},
                    {"rosberto", "tioberto", "voberto"}
            };
            for (int a = 0; a <= 2; ) {
                FornecedoresDTO fonecedoresDTO = new FornecedoresDTO();
                fonecedoresDTO.setRazao(fornecedor[0][a]);
                fonecedoresDTO.setCnpj(fornecedor[1][a]);
                fonecedoresDTO.setNomeFantasia(fornecedor[2][a]);
                fonecedoresDTO.setEndereco(fornecedor[3][a]);
                fonecedoresDTO.setTelefone(Long.parseLong(fornecedor[4][a]));
                fonecedoresDTO.setEmail(fornecedor[5][a]);

                fornecedorService.save(fonecedoresDTO);
                a++;
            }

            List<Fornecedor> termino = fornecedorService.findAll();
            if (!termino.isEmpty() && conferiCategoria.isEmpty()) {
                String[][] categorias = {{"cerveja", "refri", "suco"},
                        {"1", "2", "3"}, {"123", "12", "1"}};
                for (int a = 0; a <= 2; ) {
                    CategoriaDTO categoriaDTO = new CategoriaDTO();
                    categoriaDTO.setNomeCategoria(categorias[0][a]);
                    categoriaDTO.setIdCategoriaFornecedor(Long.parseLong(categorias[1][a]));
                    categoriaDTO.setCodigo(categorias[2][a]);

                    categoriaService.save(categoriaDTO);
                    a++;
                }
            }
            List<Categoria> buscaCategoria = categoriaService.findAll();
            if (!buscaCategoria.isEmpty() && conferilinhas.isEmpty()) {
                String[][] linhas = {{"antarctica", "brahma", "skol", "soda", "citrus", "do bem"},
                        {"antar1507", "brahm1508", "skol1508", "soda1509", "citru1510", "dobem1511"},
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
                        {"antarctica", "antar1507", "2.95", "1", "12", "7.700","kg", "2019-02-03T00:00:00"},
                        {"brahma", "brahm1508", "2.45", "1", "12", "6.20","kg", "2019-02-03T00:00:00"},
                        {"skol", "skol1508", "2.65", "1", "12", "5.1","kg", "2019-02-03T00:00:00"},
                        {"soda", "soda1509", "4.00", "2", "9", "4","kg", "2019-02-03T00:00:00"},
                        {"citrus", "citru1510", "5.50", "2", "6", "3.596","kg", "2019-02-03T00:00:00"},
                        {"do bem", "dobem1511", "7.89", "3", "8", "2.756","kg", "2019-02-03T00:00:00"},
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
                            produto[6],
                            LocalDateTime.parse(produto[7])
                    );
                    produtoService.save(produtosDTO);
                }
                System.out.println("deu bom kk");
            }
        }
    }
}

