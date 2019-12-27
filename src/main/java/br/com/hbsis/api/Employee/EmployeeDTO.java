package br.com.hbsis.api.Employee;

import br.com.hbsis.funcionario.FuncionarioDTO;

public class EmployeeDTO {
    private String nome;

    public EmployeeDTO() {
    }

    public EmployeeDTO(String responseName) {
        this.nome = responseName;
    }

    public static EmployeeDTO of(FuncionarioDTO funcionarioDTO) {
        return new EmployeeDTO(
                funcionarioDTO.getNomeFuncionario()
        );
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
