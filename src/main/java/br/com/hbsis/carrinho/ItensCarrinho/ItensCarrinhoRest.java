package br.com.hbsis.carrinho.ItensCarrinho;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itemCarrinho")
public class ItensCarrinhoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItensCarrinhoRest.class);
    private final ItensCarrinhoService itensCarrinhoService;

    public ItensCarrinhoRest(ItensCarrinhoService itensCarrinhoService) {
        this.itensCarrinhoService = itensCarrinhoService;
    }

    @PostMapping
    public ItensCarrinhoDTO criar(@RequestBody ItensCarrinhoDTO itensCarrinhoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Pedido...");
        LOGGER.debug("Payaload: {}", itensCarrinhoDTO);

        //manda salva
        return this.itensCarrinhoService.save(itensCarrinhoDTO);
    }

    @GetMapping("/{id}")
    public ItensCarrinhoDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda buscar o Pedido
        return this.itensCarrinhoService.findById(id);
    }

    @PutMapping("/{id}")
    public ItensCarrinhoDTO udpate(@PathVariable("id") Long id, @RequestBody ItensCarrinhoDTO pedidoDTO) {
        LOGGER.info("Recebendo Update para Pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);

        //manda alterar
        return this.itensCarrinhoService.update(pedidoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Pedido de ID: {}", id);

        //manda sumir
        this.itensCarrinhoService.delete(id);
    }
}
