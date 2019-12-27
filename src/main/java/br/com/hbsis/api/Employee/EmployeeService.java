package br.com.hbsis.api.Employee;

import br.com.hbsis.funcionario.FuncionarioDTO;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class EmployeeService {

    private static final String EMPLOYEES = "http://10.2.54.25:9999/api/employees";
    private static final String KEY = "d04841c4-90ad-4dc8-842b-b3cce24cce30";

    private EmployeeService() {
    }

    public static FuncionarioDTO FuncionarioHBEmployee(EmployeeDTO inputDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<EmployeeDTO> dtoHttpEntity = new HttpEntity<>(inputDTO, getHeaders());
        ResponseEntity<EmployeeOutputDTO> ResponseEntity = restTemplate.exchange(EMPLOYEES, HttpMethod.POST, dtoHttpEntity, EmployeeOutputDTO.class);

        if (ResponseEntity.getStatusCodeValue() == 200) {
            FuncionarioDTO funcionarioDTO = new FuncionarioDTO();
            funcionarioDTO.setNomeFuncionario(ResponseEntity.getBody().getEmployeeName());
            funcionarioDTO.setUuid(ResponseEntity.getBody().getEmployeeUuid());
            return funcionarioDTO;
        }
        throw new IllegalArgumentException("Employee não teve retorno");
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
