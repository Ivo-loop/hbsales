package br.com.hbsis.pedido;


import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.produtos.Produtos;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seg_pedido")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_pedido", nullable = false, length = 10)
    private String codPedido;
    @Column(name = "status_pedido",nullable = false, length = 10)
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_pedido_fornecedo", referencedColumnName = "id")
    private Fornecedor fornecedorPedido;
    @ManyToOne
    @JoinColumn(name = "id_pedido_produtos", referencedColumnName = "id")
    private Produtos produtosPedido;

    @Column(name = "dia_create_pedido", nullable = false)
    private LocalDateTime dia;
    @Column(name = "amount_pedido", nullable = false)
    private Long amount;

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", codPedido='" + codPedido + '\'' +
                ", status='" + status + '\'' +
                ", fornecedorPedido=" + fornecedorPedido +
                ", produtosPedido=" + produtosPedido +
                ", dia=" + dia +
                ", amount=" + amount +
                '}';
    }

    public Long getId() {return id; }
    public void setId(Long id) {this.id = id; }

    public String getCodPedido() {return codPedido; }
    public void setCodPedido(String codPedido) {this.codPedido = codPedido; }

    public String getStatus() {return status; }
    public void setStatus(String status) {this.status = status;}

    public Fornecedor getFornecedorPedido() {return fornecedorPedido; }
    public void setFornecedorPedido(Fornecedor fornecedorPedido) {this.fornecedorPedido = fornecedorPedido; }

    public Produtos getProdutosPedido() {return produtosPedido; }
    public void setProdutosPedido(Produtos produtosPedido) {this.produtosPedido = produtosPedido; }

    public LocalDateTime getDia() {return dia; }
    public void setDia(LocalDateTime dia) {this.dia = dia; }

    public Long getAmount() {return amount; }
    public void setAmount(Long amount) {this.amount = amount; }
}
