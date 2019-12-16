package br.com.hbsis.linhas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/linhas")
public class LinhasRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinhasRest.class);

    private final LinhasService linhasService;

    @Autowired
    public LinhasRest(LinhasService linhasService) {
        this.linhasService = linhasService;
    }

    @PostMapping
    public LinhasDTO save(@RequestBody LinhasDTO linhasDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Linhas...");
        LOGGER.debug("Payaload: {}", linhasDTO);

        //manda salva
        return this.linhasService.save(linhasDTO);
    }

    @GetMapping("/export-csv")
    public void exportCSV(HttpServletResponse response) throws Exception {
        LOGGER.info("Recebendo solicitação de exportacao de Linhas...");

        //manda exporta
        linhasService.exportCSV(response);
    }

    @PostMapping("/import-csv")
    public void importCSV(@RequestParam("file") MultipartFile file) {
        LOGGER.info("Recebendo solicitação de importacao de Linhas...");

        //manda importa
        linhasService.importCSV(file);
    }

    @GetMapping("/{id}")
    public LinhasDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda buscar
        return this.linhasService.findById(id);
    }

    @PutMapping("/{id}")
    public LinhasDTO udpate(@PathVariable("id") Long id, @RequestBody LinhasDTO linhasDTO) {
        LOGGER.info("Recebendo Update para linhas de ID: {}", id);
        LOGGER.debug("Payload: {}", linhasDTO);

        //manda alterar
        return this.linhasService.update(linhasDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para linhas de ID: {}", id);

        //manda sumi com o individuo mal intencionado
        this.linhasService.delete(id);
    }
}
