package br.com.hbsis.fornecedor;


public class FornecedoresDTO {
    private Long   id;
    private String razao;
    private String cnpj;
    private String nomeFan;
    private String endereco;
    private Long telefone;
    private String email;

    //- deixa viver para mandar json
    public FornecedoresDTO() {
    }

    public FornecedoresDTO(Long id, String razao, String cnpj, String nomeFan, String endereco, Long telefone, String email) {
        this.id = id;
        this.razao = razao;
        this.cnpj = cnpj;
        this.nomeFan = nomeFan;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public static FornecedoresDTO of(Fornecedor fornecedor) {
        return new FornecedoresDTO(
                fornecedor.getId(),
                fornecedor.getRazao(),
                fornecedor.getCnpj(),
                fornecedor.getNomefan(),
                fornecedor.getEndereco(),
                fornecedor.getTelefone(),
                fornecedor.getEmail()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazao() {
        return razao;
    }

    public void setRazao(String razao) {
        this.razao = razao;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFan() {
        return nomeFan;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFan = nomeFantasia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Long getTelefone() {
        return telefone;
    }

    public void setTelefone(Long telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {

        return "seg_fornecedores{" +
                "id=" + id +
                ", razao ='"    +razao      + '\'' +
                ", cnpj='"      +cnpj       + '\'' +
                ", nomefan='"   +nomeFan    + '\'' +
                ", endereco='"  +endereco   + '\'' +
                ", telefone='"  +telefone   + '\'' +
                ", email='"     +email      + '\'' +
                '}';
    }

}