package br.com.hbsis.pedido;


import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.funcionario.Funcionario;

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
    @Column(name = "status_pedido", nullable = false, length = 10)
    private String status;

    @ManyToOne
    @JoinColumn(name = "id_pedido_fornecedo", referencedColumnName = "id")
    private Fornecedor fornecedor;
    @ManyToOne
    @JoinColumn(name = "id_pedido_funcionario", referencedColumnName = "id")
    private Funcionario funcionario;

    @Column(name = "dia_create_pedido", nullable = false)
    private LocalDateTime dia;

    public Pedido(Long id, String codPedido, String status, Fornecedor fornecedor, Funcionario funcionario, LocalDateTime dia) {
        this.id = id;
        this.codPedido = codPedido;
        this.status = status;
        this.fornecedor = fornecedor;
        this.funcionario = funcionario;
        this.dia = dia;
    }

    public Pedido() {
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", codPedido='" + codPedido + '\'' +
                ", status='" + status + '\'' +
                ", fornecedorPedido=" + fornecedor +
                ", funcionario=" + funcionario +
                ", dia=" + dia +
                '}';
    }

    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario;}

    public Long getId() {return id; }
    public void setId(Long id) {this.id = id; }

    public String getCodPedido() {return codPedido; }
    public void setCodPedido(String codPedido) {this.codPedido = codPedido; }

    public String getStatus() {return status; }
    public void setStatus(String status) {this.status = status;}

    public Fornecedor getFornecedor() {return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) {this.fornecedor = fornecedor; }

    public LocalDateTime getDia() {return dia; }
    public void setDia(LocalDateTime dia) {this.dia = dia; }

}
