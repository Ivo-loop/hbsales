package fonercedores;


public class FonecedoresDTO {
    private Long id;
    private String nome;
    private String razao;
    private String cnpj;
    private String nomeFan;
    private String endereço;
    private String telefone;
    private String email;

    public FonecedoresDTO(){ }

    public FonecedoresDTO(Long id, String razao, String cnpj, String nomeFantasia, String endereço, String telefone, String email) {
        this.id = id;
        this.razao = razao;
        this.cnpj = cnpj;
        this.nomeFan = nomeFan;
        this.endereço = endereço;
        this.telefone = telefone;
        this.email = email;
    }

    public static FonecedoresDTO of(Fornecedor fornecedor){
        return new FonecedoresDTO(
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
        this.nomeFan = nomeFan;
    }

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
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

        return "Fonecedores{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", nomefan='" + nomeFan + '\'' +
                ", cnpj='" + endereço + '\'' +
                ", cnpj='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
