package br.com.hbsis.vendas;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seg_vendas")
public class Vendas {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "descricao_vendas", length = 50)
    private String descricaoVendas;
    @Column(name = "dia_inicial_vendas", nullable = false)
    private LocalDateTime diaInicial;
    @Column(name = "dia_final_vendas", nullable = false)
    private LocalDateTime diaFinal;
    @Column(name = "dia_retirada_vendas", nullable = false)
    private LocalDateTime diaRetirada;
    @ManyToOne
    @JoinColumn(name = "id_vendas_fornecedor", referencedColumnName = "id")
    private Fornecedor fornecedorVenda;

    @Override
    public String toString() {
        return "Vendas{" +
                "id=" + id +
                ", descricaoVendas='" + descricaoVendas + '\'' +
                ", diaInicial=" + diaInicial +
                ", diaFinal=" + diaFinal +
                ", diaRetirada=" + diaRetirada +
                ", fornecedorVenda=" + fornecedorVenda +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricaoVendas() {
        return descricaoVendas;
    }

    public void setDescricaoVendas(String descricaoVendas) {
        this.descricaoVendas = descricaoVendas;
    }

    public LocalDateTime getDiaInicial() {
        return diaInicial;
    }

    public void setDiaInicial(LocalDateTime diaInicial) {
        this.diaInicial = diaInicial;
    }

    public LocalDateTime getDiaFinal() {
        return diaFinal;
    }

    public void setDiaFinal(LocalDateTime diaFinal) {
        this.diaFinal = diaFinal;
    }

    public LocalDateTime getDiaRetirada() {
        return diaRetirada;
    }

    public void setDiaRetirada(LocalDateTime diaRetirada) {
        this.diaRetirada = diaRetirada;
    }

    public Fornecedor getFornecedorVenda() {
        return fornecedorVenda;
    }

    public void setFornecedorVenda(Fornecedor fornecedorVenda) {
        this.fornecedorVenda = fornecedorVenda;
    }
}
