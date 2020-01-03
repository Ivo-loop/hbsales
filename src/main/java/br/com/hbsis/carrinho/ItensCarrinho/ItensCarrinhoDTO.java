package br.com.hbsis.carrinho.ItensCarrinho;

public class ItensCarrinhoDTO {

    private Long id;
    private Long idProduto;
    private Long idCarrinho;
    private Long amount;

    public ItensCarrinhoDTO(Long id, Long idProduto, Long idCarrinho, Long amount) {
        this.id = id;
        this.idProduto = idProduto;
        this.idCarrinho = idCarrinho;
        this.amount = amount;
    }

    public ItensCarrinhoDTO() {
    }

    public static ItensCarrinhoDTO of(ItensCarrinho itensCarrinho){
        return new ItensCarrinhoDTO(
                itensCarrinho.getId(),
                itensCarrinho.getProdutos().getId(),
                itensCarrinho.getCarrinho().getId(),
                itensCarrinho.getAmount()
        );
    }

    @Override
    public String toString() {
        return "ItensCarrinhoDTO{" +
                "id=" + id +
                ", idProduto=" + idProduto +
                ", idCarrinho=" + idCarrinho +
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

    public Long getIdCarrinho() {
        return idCarrinho;
    }

    public void setIdCarrinho(Long idPedido) {
        this.idCarrinho = idPedido;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
