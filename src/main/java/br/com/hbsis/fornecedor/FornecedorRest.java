package br.com.hbsis.fornecedor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/fornecedores")
public class FornecedorRest {
    //Controle do Postman
    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorRest.class);

    private final FornecedorService fornecedorService;

    @Autowired
    public FornecedorRest(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping
    public FornecedoresDTO save(@RequestBody FornecedoresDTO fonecedoresDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Fornecedor...");
        LOGGER.debug("Payaload: {}", fonecedoresDTO);

        //manda salvar
        return this.fornecedorService.save(fonecedoresDTO);
    }

    @GetMapping("/{id}")
    public FornecedoresDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda buscar
        return this.fornecedorService.findById(id);
    }

    @PutMapping("/{id}")
    public FornecedoresDTO udpate(@PathVariable("id") Long id, @RequestBody FornecedoresDTO fonecedoresDTO) {
        LOGGER.info("Recebendo Update para Fornecedor de ID: {}", id);
        LOGGER.debug("Payload: {}", fonecedoresDTO);

        //manda alterar
        return this.fornecedorService.update(fonecedoresDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Fornecedor de ID: {}", id);

        //desasparece com ele
        this.fornecedorService.delete(id);
    }
}