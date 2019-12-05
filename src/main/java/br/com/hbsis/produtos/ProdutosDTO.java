package br.com.hbsis.produtos;

import java.time.LocalDateTime;

public class ProdutosDTO {
    private Long id;
    private String nomeProduto;
    private String codProdutos;
    private Float preco;
    private Long idProdutosLinhas;
    private Float uniPerCax;
    private Float pesoPerUni;
    private LocalDateTime validade;


    public ProdutosDTO(Long id, String nomeProduto, String codProdutos, Float preco, Long idProdutosLinhas, Float uniPerCax, Float pesoPerUni, LocalDateTime validade) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.codProdutos = codProdutos;
        this.preco = preco;
        this.idProdutosLinhas = idProdutosLinhas;
        this.uniPerCax = uniPerCax;
        this.pesoPerUni = pesoPerUni;
        this.validade = validade;
    }

    public ProdutosDTO() {
    }

    public static ProdutosDTO of(Produtos produtos) {
        return new ProdutosDTO(
                produtos.getId(),
                produtos.getNomeProduto(),
                produtos.getCodProdutos(),
                produtos.getPreco(),
                produtos.getLinhas().getId(),
                produtos.getUniPerCax(),
                produtos.getPesoPerUni(),
                produtos.getValidade()
        );
    }

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

    public Long getIdProdutosLinhas() {
        return idProdutosLinhas;
    }

    public void setIdProdutosLinhas(Long idProdutosLinhas) {
        this.idProdutosLinhas = idProdutosLinhas;
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
        return "ProdutosDTO{" +
                "id=" + id +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", codProdutos='" + codProdutos + '\'' +
                ", preco=" + preco +
                ", idProdutosLinhas=" + idProdutosLinhas +
                ", uniPerCax=" + uniPerCax +
                ", pesoPerUni=" + pesoPerUni +
                ", validade=" + validade +
                '}';
    }
}
