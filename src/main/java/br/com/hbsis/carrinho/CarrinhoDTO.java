package br.com.hbsis.carrinho;

public class CarrinhoDTO {

    private Long id;
    private String codPedido;
    private Long idFornecedor;
    private Long idFuncionario;

    public CarrinhoDTO() {
    }

    public CarrinhoDTO(Long id, String codPedido, Long idfornecedor, Long idFuncionario) {
        this.id = id;
        this.codPedido = codPedido;
        this.idFornecedor = idfornecedor;
        this.idFuncionario = idFuncionario;
    }

    public static CarrinhoDTO of(Carrinho carrinho){
        return new CarrinhoDTO(
                carrinho.getId(),
                carrinho.getCodPedido(),
                carrinho.getFornecedor().getId(),
                carrinho.getFuncionario().getId()
        );
    }

    @Override
    public String toString() {
        return "CarrinhoDTO{" +
                "id=" + id +
                ", codPedido='" + codPedido + '\'' +
                ", idfornecedor=" + idFornecedor +
                ", idfuncionario=" + idFuncionario +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public Long getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }
}
