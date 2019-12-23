package br.com.hbsis.api.Invoice.Input;

import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.pedido.Pedido;

public class InvoiceDTO {
    private String cnpjFornecedor;
    private String employeeUuid;
    private InvoiceItemDTO invoiceItemDTOSet;
    private Float totalValue;

    public InvoiceDTO(String cnpjFornecedor, String employeeUuid, InvoiceItemDTO invoiceItemDTOSet, Float totalValue) {
        this.cnpjFornecedor = cnpjFornecedor;
        this.employeeUuid = employeeUuid;
        this.invoiceItemDTOSet = invoiceItemDTOSet;
        this.totalValue = totalValue;
    }

    public static InvoiceDTO of(Pedido pedido, InvoiceItemDTO invoiceItemDTO, Funcionario funcionario) {
        return new InvoiceDTO(
                pedido.getFornecedorPedido().getCnpj(),
                funcionario.getUuid(),
                invoiceItemDTO,
                (pedido.getAmount() * pedido.getProdutosPedido().getPreco())
        );
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "cnpjFornecedor='" + cnpjFornecedor + '\'' +
                ", employeeUuid='" + employeeUuid + '\'' +
                ", invoiceItemDTOSet=" + invoiceItemDTOSet +
                ", totalValue=" + totalValue +
                '}';
    }
}
