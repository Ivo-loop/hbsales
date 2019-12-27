package br.com.hbsis.api.invoice;

import br.com.hbsis.pedido.itens.Itens;

public class InvoiceItemDTO {
    private Long amount;
    private String itemName;

    public InvoiceItemDTO(Long amount, String itemName) {
        this.itemName = itemName;
        this.amount = amount;
    }

    public InvoiceItemDTO() {
    }

    public static InvoiceItemDTO of(Itens itens) {
        return new InvoiceItemDTO(
                itens.getAmount(),
                itens.getProdutos().getNomeProduto()
        );
    }



    @Override
    public String toString() {
        return "InvoiceItemDTO{" +
                "amount=" + amount +
                ", itemName='" + itemName + '\'' +
                '}';
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
