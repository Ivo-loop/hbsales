package br.com.hbsis.pedido.itens;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.produtos.Produtos;

import javax.persistence.*;

@Entity
@Table(name="seg_items")
public class Itens {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_itens_pedido", referencedColumnName = "id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_itens_produtos", referencedColumnName = "id")
    private Produtos produtos;

    @Column(name = "amount_itens",nullable = false, length = 10)
    private Long amount;

    public Itens(Long id, Pedido pedido, Produtos produtos, Long amount) {
        this.id = id;
        this.pedido = pedido;
        this.produtos = produtos;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Itens{" +
                "id=" + id +
                ", pedido=" + pedido +
                ", produtos=" + produtos +
                ", amount=" + amount +
                '}';
    }

    public Itens() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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
