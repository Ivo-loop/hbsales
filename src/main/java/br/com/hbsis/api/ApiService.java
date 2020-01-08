package br.com.hbsis.api;

import br.com.hbsis.api.Employee.EmployeeDTO;
import br.com.hbsis.api.Employee.EmployeeOutputDTO;
import br.com.hbsis.api.invoice.InvoiceDTO;
import br.com.hbsis.funcionario.FuncionarioDTO;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

public class ApiService {

    private static final String EMPLOYEES = "http://10.2.54.25:9999/api/employees";
    private static final String INVOICE = "http://10.2.54.25:9999/api/invoice";
    private static final String KEY = "d04841c4-90ad-4dc8-842b-b3cce24cce30";

    public static FuncionarioDTO FuncionarioHBEmployee(EmployeeDTO inputDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<EmployeeDTO> dtoHttpEntity = new HttpEntity<>(inputDTO, getHeaders());
        ResponseEntity<EmployeeOutputDTO> ResponseEntity = restTemplate.exchange(EMPLOYEES, HttpMethod.POST, dtoHttpEntity, EmployeeOutputDTO.class);

        if (ResponseEntity.getStatusCodeValue() == 200) {
            FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
            funcionarioDTO.setNomeFuncionario(Objects.requireNonNull(ResponseEntity.getBody()).getEmployeeName());
            funcionarioDTO.setUuid(ResponseEntity.getBody().getEmployeeUuid());
            return funcionarioDTO;
        }
        throw new IllegalArgumentException("Employee não teve retorno");
    }

     static void HBInvoice(InvoiceDTO inputDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<InvoiceDTO> dtoHttpEntity = new HttpEntity<>(inputDTO, getHeaders());
        ResponseEntity<InvoiceDTO> ResponseEntity = restTemplate.exchange(INVOICE, HttpMethod.POST, dtoHttpEntity, InvoiceDTO.class);

        if (ResponseEntity.getStatusCodeValue() == 200) {
            return;
        }
        throw new IllegalArgumentException("Invoice não teve retorno");
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        addAPI(httpHeaders);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    private static void addAPI(HttpHeaders httpHeaders) {
        header(httpHeaders);
    }

    private static void header(HttpHeaders httpHeaders) {
        httpHeaders.add("Authorization", ApiService.KEY);
    }
}
