package br.com.hbsis.pedido.itens;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Item")
public class ItensRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItensRest.class);
    private final ItensService itensService;

    public ItensRest(ItensService itensService) {
        this.itensService = itensService;
    }

    @PostMapping
    public ItensDTO criar(@RequestBody ItensDTO itensDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Pedido...");
        LOGGER.debug("Payaload: {}", itensDTO);

        //manda salva
        return this.itensService.save(itensDTO);
    }

    @GetMapping("/{id}")
    public ItensDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda buscar o Pedido
        return this.itensService.findById(id);
    }

    @PutMapping("/{id}")
    public ItensDTO udpate(@PathVariable("id") Long id, @RequestBody ItensDTO pedidoDTO) {
        LOGGER.info("Recebendo Update para Pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);

        //manda alterar
        return this.itensService.update(pedidoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Pedido de ID: {}", id);

        //manda sumir
        this.itensService.delete(id);
    }
}
