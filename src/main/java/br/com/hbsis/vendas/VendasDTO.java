package br.com.hbsis.vendas;

import java.time.LocalDateTime;

public class VendasDTO {

    private Long id;
    private String descricaoVendas;
    private LocalDateTime diaInicial;
    private LocalDateTime diaFinal;
    private LocalDateTime diaRetirada;
    private Long idFornecedor;

    @Override
    public String toString() {
        return "VendasDTO{" +
                "id=" + id +
                ", descricaoVendas=" + descricaoVendas +
                ", diaInicial=" + diaInicial +
                ", diaFinal=" + diaFinal +
                ", diaRetirada=" + diaRetirada +
                ", idFornecedor=" + idFornecedor +
                '}';
    }

    public VendasDTO(Long id, String descricaoVendas, LocalDateTime diaInicial, LocalDateTime diaFinal, LocalDateTime diaRetirada, Long idFornecedor ) {
        this.id = id;
        this.descricaoVendas = descricaoVendas;
        this.diaInicial = diaInicial;
        this.diaFinal = diaFinal;
        this.diaRetirada = diaRetirada;
        this.idFornecedor = idFornecedor;
    }

    public static VendasDTO of(Vendas vendas) {
        return new VendasDTO(
                vendas.getId(),
                vendas.getDescricaoVendas(),
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

    public String getDescricaoVendas() {
        return descricaoVendas;
    }

    public void setDescricaoVendas(String descricaoVendas) {
        this.descricaoVendas = descricaoVendas;
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
        return idFornecedor;
    }

    public void setIdFornecedor(Long idFornecedor) {
        this.idFornecedor = idFornecedor;
    }
}