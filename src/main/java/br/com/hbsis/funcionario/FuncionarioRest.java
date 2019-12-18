package br.com.hbsis.funcionario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Funcionario")
public class FuncionarioRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioRest.class);
    private final FuncionarioService funcionarioService;

    public FuncionarioRest(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public FuncionarioDTO save(@RequestBody FuncionarioDTO funcionarioDTO) {
        LOGGER.info("Recebendo solicitação de persistência de funcionario...");
        LOGGER.debug("Payaload: {}", funcionarioDTO);

        //manda salva
        return this.funcionarioService.save(funcionarioDTO);
    }

    @GetMapping("/{id}")
    public FuncionarioDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda buscar o funcionario diferenciado
        return this.funcionarioService.findById(id);
    }

    @PutMapping("/{id}")
    public FuncionarioDTO udpate(@PathVariable("id") Long id, @RequestBody FuncionarioDTO funcionarioDTO) {
        LOGGER.info("Recebendo Update para funcionario de ID: {}", id);
        LOGGER.debug("Payload: {}", funcionarioDTO);

        //manda alterar
        return this.funcionarioService.update(funcionarioDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para funcionario de ID: {}", id);

        //manda sumir
        this.funcionarioService.delete(id);
    }
}
