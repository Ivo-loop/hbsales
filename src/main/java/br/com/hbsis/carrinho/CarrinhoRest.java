package br.com.hbsis.carrinho;

import br.com.hbsis.pedido.PedidoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarrinhoRest.class);
    private final CarrinhoService carrinhoService;

    public CarrinhoRest(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @PostMapping
    public CarrinhoDTO criar(@RequestBody CarrinhoDTO pedidoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Pedido...");
        LOGGER.debug("Payaload: {}", pedidoDTO);

        //manda salva
        return this.carrinhoService.criarPedido(pedidoDTO);
    }

    @GetMapping("/{id}")
    public CarrinhoDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda buscar o Pedido
        return this.carrinhoService.findbyIdDTO(id);
    }

    @PutMapping("/{id}")
    public CarrinhoDTO udpate(@PathVariable("id") Long id, @RequestBody CarrinhoDTO carrinhoDTO) {
        LOGGER.info("Recebendo Update para Pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", carrinhoDTO);

        //manda alterar
        return this.carrinhoService.update(carrinhoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Pedido de ID: {}", id);

        //manda sumir
        this.carrinhoService.delete(id);
    }
}
