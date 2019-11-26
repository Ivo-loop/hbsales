package br.com.hbsis.CategoriaProduto;

import br.com.hbsis.fornecedor.Fornecedor;

public class ProdutoDTO {
    private long id;
    private String nomeProduto;
    private String codProduto;
    private Fornecedor id_produto_fornecedor;


    public ProdutoDTO(){
    }

    public ProdutoDTO(long id, String nome, String codProduto, Fornecedor id_produto_fornecedor){
        this.id = id;
        this.nomeProduto = nome;
        this.codProduto = codProduto;
        this.id_produto_fornecedor = id_produto_fornecedor;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nome) {
        this.nomeProduto = nome;
    }

    public String getCodProduto() { return codProduto; }

    public void setCodProduto(String cod) { this.codProduto = cod; }

    public static ProdutoDTO of(Produto produto){
        return new ProdutoDTO(
                produto.getId(),
                produto.getNomeProduto(),
                produto.getCodProduto(),
                produto.getId_produto_fornecedor()
        );
    }

    @Override
    public String toString(){
        return "seg_produtos{" +
                "id_produto = "+id+
                "nome_produto = '"+nomeProduto+'\''+
                "cod_produto = "+codProduto+
                "id_produto_fornecedor = "+id_produto_fornecedor+'}';
    }
}
