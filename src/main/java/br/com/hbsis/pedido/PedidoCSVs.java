package br.com.hbsis.pedido;

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
    private final ItensService itensService;
    private final VendasService vendasService;

    public PedidoCSVs(IPedidoRepository iPedidoRepository, FornecedorService fornecedorService, ItensService itensService, VendasService vendasService) {
        this.iPedidoRepository = iPedidoRepository;
        this.fornecedorService = fornecedorService;
        this.itensService = itensService;
        this.vendasService = vendasService;
    }

    public void exportCSV(HttpServletResponse response, Long id) throws IOException, ParseException {

        //seta o nome do arq
        String categoriaCSV = "categoria.csv";

        //seta o tipo do arq da resposta
        response.setContentType("text/csv");

        //config do header
        String headerKey = "Content-Disposition";

        //como é aberto em anexo
        String headerValue = String.format("attachment; filename=\"%s\"", categoriaCSV);

        response.setHeader(headerKey, headerValue);

        //instancia Print e seta como escritor
        PrintWriter printWriter = response.getWriter();

        //seta cabeça do cvs
        String header = "Periodo ; nome_produto ; quantidade ; fornecedor ";

        // escreve o cabeçario
        printWriter.println(header);

        String nome = "";
        Long cont = 0L;

        for (Vendas vendas : vendasService.findByIdFornecedor(id)) {

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
}
