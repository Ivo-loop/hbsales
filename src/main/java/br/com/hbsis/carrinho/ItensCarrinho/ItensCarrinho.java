package br.com.hbsis.carrinho.ItensCarrinho;

import br.com.hbsis.carrinho.Carrinho;
import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.produtos.Produtos;

import javax.persistence.*;

@Entity
@Table(name="seg_itens_Carrinho")
public class ItensCarrinho {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_itens_carrinho", referencedColumnName = "id")
    private Carrinho carrinho;

    @ManyToOne
    @JoinColumn(name = "id_itens_produto", referencedColumnName = "id")
    private Produtos produtos;

    @Column(name = "amount_itens",nullable = false, length = 10)
    private Long amount;

    public ItensCarrinho(Long id, Carrinho carrinho, Produtos produtos, Long amount) {
        this.id = id;
        this.carrinho = carrinho;
        this.produtos = produtos;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ItensCarrinho{" +
                "id=" + id +
                ", carrinho=" + carrinho +
                ", produtos=" + produtos +
                ", amount=" + amount +
                '}';
    }

    public ItensCarrinho() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrinho getCarrinho() {
        return carrinho;
    }

    public void setCarrinho(Carrinho carrinho) {
        this.carrinho = carrinho;
    }

    public Produtos getProdutos() {
        return produtos;
    }

    public void setProdutos(Produtos produtos) {
        this.produtos = produtos;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
