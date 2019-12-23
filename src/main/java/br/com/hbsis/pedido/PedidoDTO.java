package br.com.hbsis.pedido;

import br.com.hbsis.funcionario.Funcionario;

public class PedidoDTO {

    private Long id;
    private String codPedido;
    private String status;
    private Long idFornecedorPedido;
    private Long idProdutosPedido;
    private Long idFuncionario;
    private Long amount;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, String codPedido, String status, Long idFornecedorPedido, Long idProdutosPedido, Long idFuncionario, Long amount) {
        this.id = id;
        this.codPedido = codPedido;
        this.status = status;
        this.idFornecedorPedido = idFornecedorPedido;
        this.idProdutosPedido = idProdutosPedido;
        this.idFuncionario = idFuncionario;
        this.amount = amount;
    }

    public static PedidoDTO of(Pedido pedido, Funcionario funcionario) {
        return new PedidoDTO(
                pedido.getId(),
                pedido.getCodPedido(),
                pedido.getStatus(),
                pedido.getFornecedorPedido().getId(),
                pedido.getProdutosPedido().getId(),
                pedido.getAmount(),
                funcionario.getId()
        );
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", codPedido='" + codPedido + '\'' +
                ", status='" + status + '\'' +
                ", idFornecedorPedido=" + idFornecedorPedido +
                ", idProdutosPedido=" + idProdutosPedido +
                ", amount=" + amount +
                '}';
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getIdFornecedorPedido() {
        return idFornecedorPedido;
    }

    public void setIdFornecedorPedido(Long idFornecedorPedido) {
        this.idFornecedorPedido = idFornecedorPedido;
    }

    public Long getIdProdutosPedido() {
        return idProdutosPedido;
    }

    public void setIdProdutosPedido(Long idProdutosPedido) {
        this.idProdutosPedido = idProdutosPedido;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}