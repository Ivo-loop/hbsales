package br.com.hbsis.pedido;

import br.com.hbsis.Tools.ExportImport.ExportCSV;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.pedido.itens.Itens;
import br.com.hbsis.pedido.itens.ItensService;
import br.com.hbsis.vendas.Vendas;
import br.com.hbsis.vendas.VendasService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

@Component
public class PedidoCSVs {
    private final IPedidoRepository iPedidoRepository;
    private final FornecedorService fornecedorService;
    private final VendasService vendasService;
    private final ItensService itensService;
    private final ExportCSV exportCSV;

    public PedidoCSVs(IPedidoRepository iPedidoRepository, FornecedorService fornecedorService, ItensService itensService, VendasService vendasService, ExportCSV exportCSV) {
        this.iPedidoRepository = iPedidoRepository;
        this.fornecedorService = fornecedorService;
        this.itensService = itensService;
        this.vendasService = vendasService;
        this.exportCSV = exportCSV;
    }

    void exportCSV(HttpServletResponse response, Long id) throws IOException, ParseException {

        //seta cabeça do cvs
        String header = "Periodo ; nome_produto ; quantidade ; fornecedor ";

        exportCSV.writerHeader(response, header, "Pedido");
        PrintWriter printWriter = response.getWriter();

        String nome = "";

        for (Vendas vendas : vendasService.findByAllIdFornecedor(id)) {

            for (Pedido pedido : iPedidoRepository.findByFornecedor_Id(id)) {

                if (!vendasService.validaCompra(pedido.getDia(), pedido.getFornecedor().getId())) {

                    for (Itens itens : itensService.findByIdspedido(pedido.getId())) {

                        if (!nome.equals(itens.getProdutos().getNomeProduto())) {

                            String dia = String.valueOf(vendas.getDiaInicial()).substring(0, 10) + " até " + String.valueOf(vendas.getDiaFinal()).substring(0, 10);

                            nome = itens.getProdutos().getNomeProduto();
                            String quat = "" + itensService.soma(pedido.getId(), nome);

                            String cnpj = pedido.getFornecedor().getRazao() + " - " + fornecedorService.getCnpjMask(pedido.getFornecedor().getCnpj());
                            //escreve os dados
                            printWriter.println(dia + ";" + quat + ";" + nome + ";" + cnpj);
                        }
                    }
                }
            }
        }
        printWriter.close();
    }

    void exportCSVFuncionario(HttpServletResponse response, Long id) throws IOException, ParseException {

        //seta cabeça do cvs
        String header = " nome_funcionario ; Periodo ; nome_produto ; quantidade ; fornecedor ";

        exportCSV.writerHeader(response, header, "Pedido");
        PrintWriter printWriter = response.getWriter();

        String nome = "";

        for (Pedido pedido : iPedidoRepository.findByfuncionario_Id(id)) {

            for (Vendas vendas : vendasService.findByAllIdFornecedor(pedido.getFornecedor().getId())) {

                if (!vendasService.validaCompra(pedido.getDia(), pedido.getFornecedor().getId())) {

                    for (Itens itens : itensService.findByIdspedido(pedido.getId())) {

                        if (!nome.equals(itens.getProdutos().getNomeProduto())) {

                            String dia = String.valueOf(vendas.getDiaInicial()).substring(0, 10) + " até " + String.valueOf(vendas.getDiaFinal()).substring(0, 10);

                            nome = itens.getProdutos().getNomeProduto();
                            String quat = "" + itensService.soma(pedido.getId(), nome);

                            String cnpj = pedido.getFornecedor().getRazao() + " - " + fornecedorService.getCnpjMask(pedido.getFornecedor().getCnpj());
                            //escreve os dados
                            printWriter.println(dia + ";" + quat + ";" + nome + ";" + cnpj);
                        }
                    }
                }
            }
        }
        printWriter.close();
    }
}
