package br.com.hbsis.api;

import br.com.hbsis.api.invoice.InvoiceDTO;
import br.com.hbsis.api.invoice.InvoiceItemDTO;
import br.com.hbsis.api.invoice.InvoiceService;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.pedido.itens.ItemComponent;
import br.com.hbsis.pedido.itens.Itens;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PortaAPI {

    private final FuncionarioService funcionarioService;
    private final ItemComponent itensService;

    public PortaAPI(FuncionarioService funcionarioService, ItemComponent itensService) {
        this.funcionarioService = funcionarioService;
        this.itensService = itensService;
    }

    public void validaApi(Pedido pedido) {
        Funcionario funcionario = funcionarioService.findByIdFuncionario(pedido.getFuncionario().getId());
        List<InvoiceItemDTO> invoiceItemDTO = this.listItem(pedido.getId());
        InvoiceDTO invoiceDTO = InvoiceDTO.of(pedido, invoiceItemDTO, funcionario.getUuid(), this.total(pedido.getId()));
        InvoiceService.HBInvoice(invoiceDTO);
    }

    private List<InvoiceItemDTO> listItem(Long id) {
        return listInvoice(itensService.findAllByPedido_IdIs(id));
    }

    private List<InvoiceItemDTO> listInvoice(List<Itens> itens) {
        List<InvoiceItemDTO> bata = new ArrayList<>();
        Itens item = new Itens();
        for (int i = 0; i < itens.size(); i++) {

            item.setAmount(itens.get(i).getAmount());
            item.setProdutos(itens.get(i).getProdutos());
            bata.add(InvoiceItemDTO.of(item));
        }
        return bata;
    }

    private Float total(Long id) {
        List<Itens> itens = itensService.findAllByPedido_IdIs(id);
        float bata = 0F;
        for (Itens iten : itens) {
            bata += (iten.getAmount() * iten.getProdutos().getPreco());
        }
        return bata;
    }
}
