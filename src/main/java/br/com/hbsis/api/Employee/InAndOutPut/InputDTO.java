package br.com.hbsis.api.Employee.InAndOutPut;

import br.com.hbsis.funcionario.FuncionarioDTO;

public class InputDTO {
    private String nome;

    public InputDTO(String responseName) {
        this.nome = responseName;
    }

    public static InputDTO of(FuncionarioDTO funcionarioDTO) {
        return new InputDTO(
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
        return "InputDTO{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
