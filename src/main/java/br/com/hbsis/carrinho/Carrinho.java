package br.com.hbsis.carrinho;

import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.funcionario.Funcionario;

import javax.persistence.*;

@Entity
@Table(name = "seg_carrinho")
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cod_pedido", nullable = false, length = 10)
    private String codPedido;
    @ManyToOne
    @JoinColumn(name = "id_pedido_fornecedo", referencedColumnName = "id")
    private Fornecedor fornecedor;
    @ManyToOne
    @JoinColumn(name = "id_pedido_funcionario", referencedColumnName = "id")
    private Funcionario funcionario;

    public Carrinho() {
    }

    public Carrinho(Long id, String codPedido, Fornecedor fornecedor, Funcionario funcionario) {
        this.id = id;
        this.codPedido = codPedido;
        this.fornecedor = fornecedor;
        this.funcionario = funcionario;
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", codPedido='" + codPedido + '\'' +
                ", fornecedor=" + fornecedor +
                ", funcionario=" + funcionario +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
