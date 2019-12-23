package br.com.hbsis.api.Employee.InOutPut;

import br.com.hbsis.funcionario.FuncionarioDTO;

public class EmployeeInputDTO {
    private String nome;

    public EmployeeInputDTO() {
    }

    public EmployeeInputDTO(String responseName) {
        this.nome = responseName;
    }

    public static EmployeeInputDTO of(FuncionarioDTO funcionarioDTO) {
        return new EmployeeInputDTO(
                funcionarioDTO.getNomeFuncionario()
        );
    }

    @Override
    public String toString() {
        return "EmployeeInputDTO{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
