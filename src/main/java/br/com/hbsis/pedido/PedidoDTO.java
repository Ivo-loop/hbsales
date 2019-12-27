package br.com.hbsis.pedido;

public class PedidoDTO {

    private Long id;
    private String codPedido;
    private Long idFornecedorPedido;
    private Long idFuncionario;

    public PedidoDTO() {
    }

    public PedidoDTO(Long id, String codPedido, Long idFornecedorPedido, Long idFuncionario) {
        this.id = id;
        this.codPedido = codPedido;
        this.idFornecedorPedido = idFornecedorPedido;
        this.idFuncionario = idFuncionario;
    }

    public static PedidoDTO of(Pedido pedido) {
        return new PedidoDTO(
                pedido.getId(),
                pedido.getCodPedido(),
                pedido.getFornecedor().getId(),
                pedido.getFuncionario().getId()
        );
    }

    @Override
    public String toString() {
        return "PedidoDTO{" +
                "id=" + id +
                ", codPedido='" + codPedido + '\'' +
                ", idFornecedorPedido=" + idFornecedorPedido +
                ", idFuncionario=" + idFuncionario +
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

    public Long getIdFornecedorPedido() {
        return idFornecedorPedido;
    }

    public void setIdFornecedorPedido(Long idFornecedorPedido) {
        this.idFornecedorPedido = idFornecedorPedido;
    }
}