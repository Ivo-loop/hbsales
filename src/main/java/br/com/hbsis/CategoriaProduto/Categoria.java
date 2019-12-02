package br.com.hbsis.CategoriaProduto;

import br.com.hbsis.fornecedor.Fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "seg_categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_Categoria", unique = true, nullable = false, length = 100)
    private String nomeCategoria;
    @Column(name = "cod_Categoria", unique = true, nullable = false, length = 100)
    private String codCategoria;
    @ManyToOne
    @JoinColumn(name = "id_Categoria_Fornecedor", referencedColumnName = "id")
    private Fornecedor fornecedor;

    public Categoria() {
    }

    public Categoria(Long id, String nomeCategoria, Long id_fornecedor) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String cod) {
        this.codCategoria = cod;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    @Override
    public String toString() {
        return "seg_categoria{" +
                "id = " + id +
                "nome_categoria = '" + nomeCategoria + '\'' +
                "cod_categoria = " + codCategoria +
                "id_categoria_fornecedor = " + fornecedor + '}';
    }


}
