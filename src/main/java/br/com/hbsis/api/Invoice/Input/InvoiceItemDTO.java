package br.com.hbsis.api.Invoice.Input;

import br.com.hbsis.pedido.PedidoDTO;

public class InvoiceItemDTO {
    private Long amount;
    private String itemName;

    public InvoiceItemDTO(Long amount, String itemName) {
        this.amount = amount;
        this.itemName = itemName;
    }

    public static InvoiceItemDTO of(PedidoDTO pedidoDTO, String itemName) {
        return new InvoiceItemDTO(
                pedidoDTO.getAmount(),
                itemName
        );
    }

    @Override
    public String toString() {
        return "InvoiceItemDTO{" +
                "amount=" + amount +
                ", itemName='" + itemName + '\'' +
                '}';
    }
}
