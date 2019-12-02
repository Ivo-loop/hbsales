package br.com.hbsis.LinhasCategorias;

public class LinhasDTO {

    private Long id;
    private String nomeLinhas;
    private String codLinhas;
    private Long idLinhasCategoria;

    public LinhasDTO() {
    }

    public LinhasDTO(Long id, String nomeLinhas, String codLinhas, Long idLinhasCategoria) {
        this.id = id;
        this.nomeLinhas = nomeLinhas;
        this.codLinhas = codLinhas;
        this.idLinhasCategoria = idLinhasCategoria;
    }

    public Long getidLinhasCategoria() {
        return idLinhasCategoria;
    }

    public void setidLinhasCategoria(Long idLinhasCategoria) {
        this.idLinhasCategoria = idLinhasCategoria;
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

    public static LinhasDTO OF(Linhas linhas) {
        return new LinhasDTO(
                linhas.getId(),
                linhas.getNomeLinhas(),
                linhas.getCodLinhas(),
                linhas.getCategoria().getId()
        );
    }

    @Override
    public String toString() {
        return "seg_categoria{" +
                "id = " + id +
                "nome_Linhas = '" + nomeLinhas + '\'' +
                "cod_Linhas = " + codLinhas +
                "id_Linhas_Categoria = " + idLinhasCategoria + '}';
    }


}

