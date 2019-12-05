package br.com.hbsis.produtos;

import br.com.hbsis.linhas.Linhas;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "seg_produtos")
public class Produtos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_produto", unique = true, nullable = false, length = 100)
    private String nomeProduto;
    @Column(name = "cod_produto", unique = true, nullable = false, length = 100)
    private String codProdutos;
    @Column(name = "preco_produto", nullable = false)
    private Float preco;

    @ManyToOne
    @JoinColumn(name = "id_produtos_linhas", referencedColumnName = "id")
    private Linhas linhas;

    @Column(name = "unipercax_produto", nullable = false)
    private Float uniPerCax;
    @Column(name = "pesoperuni_produto", nullable = false)
    private Float pesoPerUni;
    @Column(name = "validade_produto", nullable = false)
    private LocalDateTime validade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getCodProdutos() {
        return codProdutos;
    }

    public void setCodProdutos(String codProdutos) {
        this.codProdutos = codProdutos;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }

    public Linhas getLinhas() {
        return linhas;
    }

    public void setLinhas(Linhas linhas) {
        this.linhas = linhas;
    }

    public Float getUniPerCax() {
        return uniPerCax;
    }

    public void setUniPerCax(Float uniPerCax) {
        this.uniPerCax = uniPerCax;
    }

    public Float getPesoPerUni() {
        return pesoPerUni;
    }

    public void setPesoPerUni(Float pesoPerUni) {
        this.pesoPerUni = pesoPerUni;
    }

    public LocalDateTime getValidade() {
        return validade;
    }

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }

    @Override
    public String toString() {
        return "seg_categoria{" +
                "id = " + id +
                "nome_Produto = '"       + nomeProduto + '\'' +
                "cod_Produto = "         + codProdutos +
                "preco_Produto = "       + preco +
                "id_Produto_Linhas = "   + linhas +
                "uniPerCax_Produto = "   + uniPerCax +
                "pesoPerUni_Produto = "  + pesoPerUni +
                "validade_Produto = "    + validade + '}';
    }
}
