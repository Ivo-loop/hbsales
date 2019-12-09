package br.com.hbsis.categorias;

public class CategoriaDTO {
    private Long id;
    private String nomeCategoria;
    private Long idCategoriaFornecedor;
    private Long Number;

    public CategoriaDTO() {
    }

    public CategoriaDTO(Long id, String nomeCategoria, String codCategoria, Long idCategoriaFornecedor) {
        this.id = id;
        this.nomeCategoria = nomeCategoria;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumber() {
        return Number;
    }

    public void setNumber(Long number) {
        Number = number;
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
                ", Number=" + Number +
                '}';
    }
}
