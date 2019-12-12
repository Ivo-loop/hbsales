package br.com.hbsis.categorias;

public class CategoriaDTO {
    private Long id;
    private String nomeCategoria;
    private Long idCategoriaFornecedor;
    private String codigo;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Long id, String nomeCategoria, Long idCategoriaFornecedor, String codigo) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
        this.idCategoriaFornecedor = idCategoriaFornecedor;
        this.codigo = codigo;
    }

    public static CategoriaDTO of(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getFornecedor().getId(),
                categoria.getCodCategoria().substring(8,10)
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getIdCategoriaFornecedor() {
        return idCategoriaFornecedor;
    }

    public void setIdCategoriaFornecedor(Long idCategoriaFornecedor) {
        this.idCategoriaFornecedor = idCategoriaFornecedor;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nome) {
        this.nomeCategoria = nome;
    }

    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                ", idCategoriaFornecedor=" + idCategoriaFornecedor +
                ", Number=" + codigo +
                '}';
    }
}
