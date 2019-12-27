package br.com.hbsis.pedido;

import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.pedido.itens.ItensService;
import br.com.hbsis.vendas.Vendas;
import br.com.hbsis.vendas.VendasService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

public class pedidoCSVs {
    private final IPedidoRepository iPedidoRepository;
    private final FornecedorService fornecedorService;
    private final ItensService itensService;
    private final VendasService vendasService;

    public pedidoCSVs(IPedidoRepository iPedidoRepository, FornecedorService fornecedorService, ItensService itensService, VendasService vendasService) {
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

        for (Pedido pedido : iPedidoRepository.findByFornecedor_Id(id)) {
            for (Vendas vendas : vendasService.findByIdFornecedor(id)) {
                if (vendasService.validaCompra(pedido.getDia(), pedido.getFornecedor().getId())) {

                    String dia = vendas.getDiaInicial() + " até " + vendas.getDiaFinal();
                    String nome = itensService.findByIdpedido(pedido.getId()).getProdutos().getNomeProduto();
                    String cod = "" + itensService.soma(pedido.getId() , nome);

                    String cnpj = pedido.getFornecedor().getRazao() + " - " + fornecedorService.getCnpjMask(pedido.getFornecedor().getCnpj());
                    //escreve os dados
                    printWriter.println(cod + ";" + nome + ";" + cnpj);
                }
            }
        }
        printWriter.close();
    }
}
