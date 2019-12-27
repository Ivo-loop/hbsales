package br.com.hbsis.api.invoice;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class InvoiceService {

    private static final String INVOICE = "http://10.2.54.25:9999/api/invoice";
    private static final String KEY = "d04841c4-90ad-4dc8-842b-b3cce24cce30";


    public static Boolean HBInvoice(InvoiceDTO inputDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<InvoiceDTO> dtoHttpEntity = new HttpEntity<>(inputDTO, getHeaders());
        ResponseEntity<InvoiceDTO> ResponseEntity = restTemplate.exchange(INVOICE, HttpMethod.POST, dtoHttpEntity, InvoiceDTO.class);

        if (ResponseEntity.getStatusCodeValue() == 200) {
            return true;
        }
        throw new IllegalArgumentException("Invoice não teve retorno");
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        addAPI(httpHeaders);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    private static HttpHeaders addAPI(HttpHeaders httpHeaders) {
        header("Authorization", KEY, httpHeaders);
        return httpHeaders;
    }

    private static HttpHeaders header(String name, String key, HttpHeaders httpHeaders) {
        httpHeaders.add(name, key);
        return httpHeaders;
    }
}
