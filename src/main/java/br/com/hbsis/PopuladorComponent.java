package br.com.hbsis;

import br.com.hbsis.categorias.Categoria;
import br.com.hbsis.categorias.CategoriaDTO;
import br.com.hbsis.categorias.CategoriaService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.FornecedoresDTO;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioDTO;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.linhas.Linhas;
import br.com.hbsis.linhas.LinhasDTO;
import br.com.hbsis.linhas.LinhasService;
import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.pedido.PedidoService;
import br.com.hbsis.pedido.itens.ItensService;
import br.com.hbsis.produtos.ProdutoService;
import br.com.hbsis.produtos.Produtos;
import br.com.hbsis.produtos.ProdutosDTO;
import br.com.hbsis.vendas.Vendas;
import br.com.hbsis.vendas.VendasDTO;
import br.com.hbsis.vendas.VendasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class PopuladorComponent {

    //- Essa classe faz a populaçao do Database, por favor nao mexer
    // Error:
    // 1-porfavor conferir se as primary foram resetadas

    private static final Logger LOGGER = LoggerFactory.getLogger(PopuladorComponent.class);
    private final FornecedorService fornecedorService;
    private final CategoriaService categoriaService;
    private final LinhasService linhasService;
    private final ProdutoService produtoService;
    private final FuncionarioService funcionarioService;
    private final VendasService vendasService;
    private final PedidoService pedidoService;
    private final ItensService itensService;

    public PopuladorComponent(FornecedorService fornecedorService, CategoriaService categoriaService, LinhasService linhasService, ProdutoService produtoService, FuncionarioService funcionarioService, VendasService vendasService, PedidoService pedidoService, ItensService itensService) {
        this.fornecedorService = fornecedorService;
        this.categoriaService = categoriaService;
        this.linhasService = linhasService;
        this.produtoService = produtoService;
        this.funcionarioService = funcionarioService;
        this.vendasService = vendasService;
        this.pedidoService = pedidoService;
        this.itensService = itensService;
    }

    @PostConstruct
    public void posConstruct() {
        List<Fornecedor> buscaFornecedor = fornecedorService.findAll();
        List<Categoria> conferiCategoria = categoriaService.findAll();
        List<Linhas> conferilinhas = linhasService.findAll();
        List<Produtos> conferiProdutos = produtoService.findAll();
        List<Funcionario> conferiFuncionarios = funcionarioService.findAll();
        List<Vendas> conferiVendas = vendasService.findAll();
        List<Pedido> conferiPedido = pedidoService.findAll();

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
                        {"antarctica", "antar1507", "2.95", "1", "12", "7.700", "kg", "2019-02-03T00:00:00"},
                        {"brahma", "brahm1508", "2.45", "2", "12", "6.20", "kg", "2019-02-03T00:00:00"},
                        {"skol", "skol1508", "2.65", "3", "12", "5.1", "kg", "2019-02-03T00:00:00"},
                        {"soda", "soda1509", "4.00", "4", "9", "4", "kg", "2019-02-03T00:00:00"},
                        {"citrus", "citru1510", "5.50", "5", "6", "3.596", "kg", "2019-02-03T00:00:00"},
                        {"do bem", "dobem1511", "7.89", "6", "8", "2.756", "kg", "2019-02-03T00:00:00"},
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
            }

            if (conferiFuncionarios.isEmpty()) {
                String[][] funcionarios = {
                        {"roberto", "roberto@hbsis.com.br", "euid"},
                        {"rosberto", "rosberto@hbsis.com.br", "euid"},
                        {"tioberto", "tioberto@hbsis.com.br", "euid"},
                        {"voberto", "voberto@hbsis.com.br", "euid"},
                        {"netoberto", "netoberto@hbsis.com.br", "euid"},
                        {"paiberto", "paiberto@hbsis.com.br", "euid"},
                };
                for (String[] funcionario : funcionarios) {
                    FuncionarioDTO funcionarioDTO = new FuncionarioDTO(
                            null,
                            funcionario[0],
                            funcionario[1],
                            funcionario[2]
                    );
                    funcionarioService.save(funcionarioDTO);
                }
            }

            if (conferiVendas.isEmpty()) {
                String[][] periodos = {
                        {"primeiro periodo", "1"},
                        {"segunda periodo", "2"},
                        {"terceiro periodo", "3"},
                };
                for (String[] periodo : periodos) {
                    VendasDTO funcionarioDTO = new VendasDTO(
                            null,
                            periodo[0],
                            LocalDateTime.now().plusDays(1),
                            LocalDateTime.now().plusWeeks(1),
                            LocalDateTime.now().plusMonths(1),
                            Long.parseLong(periodo[1])
                    );
                    vendasService.save(funcionarioDTO);
                }
            }

            if(conferiPedido.isEmpty()){

            }
            LOGGER.info("deu bom ao Repopular.");
        }
    }
}

