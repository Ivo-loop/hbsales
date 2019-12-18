package br.com.hbsis.api.Employee;

import br.com.hbsis.funcionario.FuncionarioDTO;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class EmployeeService {

    private static final String URL_API = "http://10.2.54.25:9999/swagger-ui.html#/employee-rest/saveUsingPOST";
    private static final String EMPLOYEES = URL_API + "/employees";

    private EmployeeService() {
    }

    public static void validateFuncionarioWithHBEmployee(FuncionarioDTO funcionarioDTO) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<FuncionarioDTO> dtoHttpEntity = new HttpEntity<>(funcionarioDTO, getHeaders());
        ResponseEntity<EmployeeDTO> ResponseEntity = restTemplate.exchange(EMPLOYEES,
                HttpMethod.POST, dtoHttpEntity, EmployeeDTO.class);

        if (ResponseEntity.getStatusCodeValue() == 200) {
            funcionarioDTO.setNomeFuncionario(ResponseEntity.getBody().getEmployeeName());
            funcionarioDTO.setUuid(ResponseEntity.getBody().getEmployeeUuid());
            return;
        }
        throw new ReturnException("Employee não teve retorno");
    }

    public static HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        addAPI(httpHeaders);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }

    private static HttpHeaders addAPI(HttpHeaders httpHeaders) {
        header("Authorization", "d04841c4-90ad-4dc8-842b-b3cce24cce30", httpHeaders);
        return httpHeaders;
    }

    public static HttpHeaders header(String Name, String key, HttpHeaders httpHeaders) {
        httpHeaders.add(Name, key);
        return httpHeaders;
    }
}
