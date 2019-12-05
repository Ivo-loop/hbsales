package br.com.hbsis.fornecedor;

import javax.persistence.*;

@Entity
@Table(name = "seg_fornecedores")
public class Fornecedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long   id;
    @Column(name = "razao_social", unique = true, nullable = false, length = 100)
    private String razaosocial;
    @Column(name = "cnpj", unique = true, nullable = false, length = 14)
    private String cnpj;
    @Column(name = "nome_fan", unique = true, nullable = false, length = 100)
    private String nomefan;
    @Column(name = "endereco", unique = true, nullable = false, length = 500)
    private String endereco;
    @Column(name = "telefone", unique = true, nullable = false, length = 11)
    private String telefone;
    @Column(name = "email", unique = true, nullable = false, length = 200)
    private String email;

    public Fornecedor(Long id, String razaosocial, String cnpj, String nomefan, String endereco, String telefone, String email) {
        this.id = id;
        this.razaosocial = razaosocial;
        this.cnpj = cnpj;
        this.nomefan = nomefan;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
    }

    public Fornecedor() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazao() {
        return razaosocial;
    }

    public void setRazao(String nome) {
        this.razaosocial = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomefan() {
        return nomefan;
    }

    public void setNomefan(String nomefantasia) {
        this.nomefan = nomefantasia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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
        return "seg_fornecedores{" +
                "id=" + id +
                ", razao ='"    +razaosocial+ '\'' +
                ", cnpj='"      +cnpj       + '\'' +
                ", nomefan='"   +nomefan    + '\'' +
                ", endereco='"  +endereco   + '\'' +
                ", telefone='"  +telefone   + '\'' +
                ", email='"     +email      + '\'' +
                '}';

    }

}