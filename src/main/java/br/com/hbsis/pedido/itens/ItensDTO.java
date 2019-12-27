package br.com.hbsis.pedido.itens;

public class ItensDTO {

    private Long id;
    private Long idProduto;
    private Long idPedido;
    private Long amount;

    public ItensDTO(Long id, Long idProduto, Long idPedido, Long amount) {
        this.id = id;
        this.idProduto = idProduto;
        this.idPedido = idPedido;
        this.amount = amount;
    }

    public ItensDTO() {
    }

    public static ItensDTO of(Itens itens){
        return new ItensDTO(
                itens.getId(),
                itens.getProdutos().getId(),
                itens.getPedido().getId(),
                itens.getAmount()
        );
    }

    @Override
    public String toString() {
        return "ItensDTO{" +
                "id=" + id +
                ", idProduto=" + idProduto +
                ", idPedido=" + idPedido +
                ", amount=" + amount +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
