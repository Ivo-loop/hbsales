package br.com.hbsis.api.invoice;

import br.com.hbsis.pedido.Pedido;

import java.util.List;

public class InvoiceDTO {
    private String cnpjFornecedor;
    private String employeeUuid;
    private List<InvoiceItemDTO> invoiceItemDTOSet;
    private Float totalValue;

    //todo:manter get e setter para springr gerar magica

    public InvoiceDTO() {
    }

    public InvoiceDTO(String cnpjFornecedor, String employeeUuid, List<InvoiceItemDTO> invoiceItemDTOSet, Float totalValue) {
        this.cnpjFornecedor = cnpjFornecedor;
        this.employeeUuid = employeeUuid;
        this.invoiceItemDTOSet = invoiceItemDTOSet;
        this.totalValue = totalValue;
    }

    public static InvoiceDTO of(Pedido pedido, List<InvoiceItemDTO> invoiceItemDTO, String uiid, Float total) {
        return new InvoiceDTO(
                pedido.getFornecedor().getCnpj(),
                uiid,
                invoiceItemDTO,
                total
        );
    }

    public String getCnpjFornecedor() {
        return cnpjFornecedor;
    }

    public void setCnpjFornecedor(String cnpjFornecedor) {
        this.cnpjFornecedor = cnpjFornecedor;
    }

    public String getEmployeeUuid() {
        return employeeUuid;
    }

    public void setEmployeeUuid(String employeeUuid) {
        this.employeeUuid = employeeUuid;
    }

    public List<InvoiceItemDTO> getInvoiceItemDTOSet() {
        return invoiceItemDTOSet;
    }

    public void setInvoiceItemDTOSet(List<InvoiceItemDTO> invoiceItemDTOSet) {
        this.invoiceItemDTOSet = invoiceItemDTOSet;
    }

    public Float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Float totalValue) {
        this.totalValue = totalValue;
    }

    @Override
    public String toString() {
        return "InvoiceDTO{" +
                "cnpjFornecedor='" + cnpjFornecedor + '\'' +
                ", employeeUuid='" + employeeUuid + '\'' +
                ", invoiceItemDTOSet=" + invoiceItemDTOSet +
                ", totalValue=" + totalValue +
                '}';
    }
}
