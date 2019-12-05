package br.com.hbsis.vendas;

import java.time.LocalDateTime;

public class VendasDTO {

    private Long id;
    private Long codVendas;
    private LocalDateTime diaInicial;
    private LocalDateTime diaFinal;
    private LocalDateTime diaRetirada;
    private Long IdFornecedor;

    @Override
    public String toString() {
        return "VendasDTO{" +
                "id=" + id +
                ", codVendas=" + codVendas +
                ", diaInicial=" + diaInicial +
                ", diaFinal=" + diaFinal +
                ", diaRetirada=" + diaRetirada +
                ", IdFornecedor=" + IdFornecedor +
                '}';
    }

    public VendasDTO(Long id, Long codVendas, LocalDateTime diaInicial, LocalDateTime diaFinal, LocalDateTime diaRetirada, Long idFornecedor) {
        this.id = id;
        this.codVendas = codVendas;
        this.diaInicial = diaInicial;
        this.diaFinal = diaFinal;
        this.diaRetirada = diaRetirada;
        IdFornecedor = idFornecedor;
    }

    public static VendasDTO of(Vendas vendas) {
        return new VendasDTO(
                vendas.getId(),
                vendas.getCodVendas(),
                vendas.getDiaInicial(),
                vendas.getDiaFinal(),
                vendas.getDiaRetirada(),
                vendas.getFornecedorVenda().getId()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodVendas() {
        return codVendas;
    }

    public void setCodVendas(Long codVendas) {
        this.codVendas = codVendas;
    }

    public LocalDateTime getDiaInicial() {
        return diaInicial;
    }

    public void setDiaInicial(LocalDateTime diaInicial) {
        this.diaInicial = diaInicial;
    }

    public LocalDateTime getDiaFinal() {
        return diaFinal;
    }

    public void setDiaFinal(LocalDateTime diaFinal) {
        this.diaFinal = diaFinal;
    }

    public LocalDateTime getDiaRetirada() {
        return diaRetirada;
    }

    public void setDiaRetirada(LocalDateTime diaRetirada) {
        this.diaRetirada = diaRetirada;
    }

    public Long getIdFornecedor() {
        return IdFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        IdFornecedor = idFornecedor;
    }
}