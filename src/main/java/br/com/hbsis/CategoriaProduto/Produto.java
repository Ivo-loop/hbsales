package br.com.hbsis.CategoriaProduto;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "seg_produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "nomeProduto", unique = true, nullable = false, length = 100)
    private String nomeProduto;
    @Column(name = "cod_produto", unique=true, nullable = false, length = 100)
    private String codProduto;
    @ManyToOne
    @JoinColumn(name="id_produto_fornecedor", referencedColumnName="id")
    private Fornecedor id_produto_fornecedor;



    public Produto(){}

    public Produto(long id, String nomeProduto, long id_fornecedor){
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

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getCodProduto() {
        return codProduto;
    }

    public void setCodProduto(String cod) {
        this.codProduto = cod;
    }

    public Fornecedor getId_produto_fornecedor() {
        return id_produto_fornecedor;
    }

    public void setId_produto_fornecedor(Fornecedor id_produto_fornecedor) {
        this.id_produto_fornecedor = id_produto_fornecedor;
    }

    @Override
    public String toString(){
        return "seg_produtos{" +
                "id_produto = "+id+
                "nome_produto = '"+nomeProduto+'\''+
                "cod_produto = "+ codProduto +
                "id_produto_fornecedor = "+id_produto_fornecedor+'}';
    }



}
