package br.com.hbsis.vendas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendas")
public class VendasRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendasRest.class);

    private final VendasService vendasService;

    @Autowired
    public VendasRest(VendasService vendasService) {
        this.vendasService = vendasService;
    }

    @PostMapping
    public VendasDTO save(@RequestBody VendasDTO vendasDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Linhas...");
        LOGGER.debug("Payaload: {}", vendasDTO);

        //manda salva
        return this.vendasService.save(vendasDTO);
    }

    @GetMapping("/{id}")
    public VendasDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda buscar o produto diferenciado
        return this.vendasService.findById(id);
    }

    @PutMapping("/{id}")
    public VendasDTO udpate(@PathVariable("id") Long id, @RequestBody VendasDTO vendasDTO) {
        LOGGER.info("Recebendo Update para linhas de ID: {}", id);
        LOGGER.debug("Payload: {}", vendasDTO);

        //manda alterar
        return this.vendasService.update(vendasDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para linhas de ID: {}", id);

        //manda sumir
        this.vendasService.delete(id);
    }
}
