package br.com.hbsis.pedido;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/pedido")
public class PedidoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoRest.class);
    private final PedidoService pedidoService;
    private final PedidoCSVs pedidoCSVs;

    public PedidoRest(PedidoService pedidoService, PedidoCSVs pedidoCSVs) {
        this.pedidoService = pedidoService;
        this.pedidoCSVs = pedidoCSVs;
    }

    @PostMapping
    public PedidoDTO criar(@RequestBody PedidoDTO pedidoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Pedido...");
        LOGGER.debug("Payaload: {}", pedidoDTO);

        //manda salva
        return this.pedidoService.criarPedido(pedidoDTO);
    }

    @GetMapping("/export-csv/{id}")
    public void exportCSV(HttpServletResponse response, @PathVariable("id") Long id) throws IOException, ParseException {
        LOGGER.info("Recebendo solicitação de exportacao de categoria...");

        //manda exporta
        pedidoCSVs.exportCSV(response, id);
    }

    @PostMapping("/save/{id}")
    public PedidoDTO save(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo solicitação de persistência de Pedido...");
        LOGGER.debug("Payaload: {}", id);

        //manda salva
        return this.pedidoService.save(id);
    }

    @GetMapping("/{id}")
    public PedidoDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda buscar o Pedido
        return this.pedidoService.findById(id);
    }

    @PutMapping("/alterar/{id}")
    public PedidoDTO udpate(@PathVariable("id") Long id, @RequestBody PedidoDTO pedidoDTO) {
        LOGGER.info("Recebendo Update para Pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);

        //manda alterar
        return this.pedidoService.update(pedidoDTO, id);
    }
    @PutMapping("/cancelar/{id}")
    public PedidoDTO cancela(@PathVariable("id") Long id, @RequestBody PedidoDTO pedidoDTO) {
        LOGGER.info("Recebendo Update para Pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);

        //manda alterar
        return this.pedidoService.cancela(pedidoDTO, id);
    }@PutMapping("/retirar/{id}")
    public PedidoDTO retira(@PathVariable("id") Long id, @RequestBody PedidoDTO pedidoDTO) {
        LOGGER.info("Recebendo Update para Pedido de ID: {}", id);
        LOGGER.debug("Payload: {}", pedidoDTO);

        //manda alterar
        return this.pedidoService.retirado(pedidoDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Pedido de ID: {}", id);

        //manda sumir
        this.pedidoService.delete(id);
    }
}
