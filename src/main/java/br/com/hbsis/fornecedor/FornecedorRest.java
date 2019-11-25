package br.com.hbsis.fornecedor;

import br.com.hbsis.fornecedor.FornecedorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Fonecedores")
public class FornecedorRest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorRest.class);

    private final FornecedorService fornecedorService;

    @Autowired
    public FornecedorRest(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @PostMapping("/pizza")
    public FonecedoresDTO save(@RequestBody FonecedoresDTO fonecedoresDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Fornecedor...");
        LOGGER.debug("Payaload: {}", fonecedoresDTO);

        return this.fornecedorService.save(fonecedoresDTO);
    }

    @GetMapping("/{id}")
    public FonecedoresDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.fornecedorService.findById(id);
    }

    @PutMapping("/{id}")
    public FonecedoresDTO udpate(@PathVariable("id") Long id, @RequestBody FonecedoresDTO fonecedoresDTO) {
        LOGGER.info("Recebendo Update para Fornecedor de ID: {}", id);
        LOGGER.debug("Payload: {}", fonecedoresDTO);

        return this.fornecedorService.update(fonecedoresDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Fornecedor de ID: {}", id);

        this.fornecedorService.delete(id);
    }
}