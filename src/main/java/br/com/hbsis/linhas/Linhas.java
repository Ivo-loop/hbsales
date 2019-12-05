package br.com.hbsis.linhas;

import br.com.hbsis.categorias.Categoria;

import javax.persistence.*;

@Entity
@Table(name = "seg_Linhas")
public class Linhas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_Linhas", unique = true, nullable = false, length = 100)
    private String nomeLinhas;
    @Column(name = "cod_Linhas", unique = true, nullable = false, length = 100)
    private String codLinhas;
    @ManyToOne
    @JoinColumn(name = "id_Linhas_Categoria", referencedColumnName = "id")
    private Categoria categoria;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeLinhas() {
        return nomeLinhas;
    }

    public void setNomeLinhas(String nomeLinhas) {
        this.nomeLinhas = nomeLinhas;
    }

    public String getCodLinhas() {
        return codLinhas;
    }

    public void setCodLinhas(String codLinhas) {
        this.codLinhas = codLinhas;
    }

    @Override
    public String toString() {
        return "seg_Linhas{" +
                "id = " + id +
                "nome_Linhas = '" + nomeLinhas + '\'' +
                "cod_Linhas = " + codLinhas +
                "id_Linhas_Categoria = " + categoria + '}';
    }
}
