package br.com.hbsis.categorias;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;


@RestController
@RequestMapping("/categoria")
public class CategoriaRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaRest.class);

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaRest(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public CategoriaDTO save(@RequestBody CategoriaDTO categoriaDTO) {
        LOGGER.info("Recebendo solicitação de persistência de categoria...");
        LOGGER.debug("Payaload: {}", categoriaDTO);

        //manda salva
        return this.categoriaService.save(categoriaDTO);
    }

    @GetMapping("/export-csv")
    public void exportCSV(HttpServletResponse response) throws IOException, ParseException {
        LOGGER.info("Recebendo solicitação de exportacao de categoria...");

        //manda exporta
        categoriaService.exportCSV(response);
    }


    @PostMapping("/import-csv")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        LOGGER.info("Recebendo solicitação de importacao de categoria...");

        //manda ler
       categoriaService.importCSV(file);
    }

    @GetMapping("/{id}")
    public CategoriaDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda busca
        return this.categoriaService.findById(id);
    }

    @PutMapping("/{id}")
    public CategoriaDTO udpate(@PathVariable("id") Long id, @RequestBody CategoriaDTO categoriaDTO) {
        LOGGER.info("Recebendo Update para Categoria de ID: {}", id);
        LOGGER.debug("Payload: {}", categoriaDTO);

        //manda alterar
        return this.categoriaService.update(categoriaDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Categoria de ID: {}", id);

        //manda apaga
        this.categoriaService.delete(id);
    }
}
