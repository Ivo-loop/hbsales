package br.com.hbsis.categorias;

public class CategoriaDTO {
    private Long id;
    private String nomeCategoria;
    private String codCategoria;
    private Long idCategoriaFornecedor;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Long id, String nomeCategoria, String codCategoria, Long idCategoriaFornecedor) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
        this.codCategoria = codCategoria;
        this.idCategoriaFornecedor = idCategoriaFornecedor;
    }

    public static CategoriaDTO of(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getCodCategoria(),
                categoria.getFornecedor().getId()
        );
    }

    public long getIdCategoriaFornecedor() {
        return idCategoriaFornecedor;
    }

    public void setIdCategoriaFornecedor(Long idCategoriaFornecedor) {
        this.idCategoriaFornecedor = idCategoriaFornecedor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nome) {
        this.nomeCategoria = nome;
    }

    public String getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(String cod) {
        this.codCategoria = cod;
    }

    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                ", codCategoria='" + codCategoria + '\'' +
                ", idCategoriaFornecedor=" + idCategoriaFornecedor +
                '}';
    }
}
