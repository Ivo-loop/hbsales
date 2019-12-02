package br.com.hbsis.LinhasCategorias;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@RestController
@RequestMapping("/Linhas")
public class LinhasRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinhasRest.class);

    private final LinhasService linhasService;

    @Autowired
    public LinhasRest(LinhasService linhasService) {
        this.linhasService = linhasService;
    }

    @PostMapping("/save")
    public LinhasDTO save(@RequestBody LinhasDTO linhasDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Linhas...");
        LOGGER.debug("Payaload: {}", linhasDTO);

        return this.linhasService.save(linhasDTO);
    }

    @GetMapping("/export-csv-linhas")
    public void exportCSV(HttpServletResponse response) throws Exception {
        String nomearquivo = "linhas.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nomearquivo + "\"");


        PrintWriter writer = response.getWriter();

        ICSVWriter csvWriter = new CSVWriterBuilder(writer).withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        for (Linhas linhas : linhasService.findAll()) {
            csvWriter.writeNext(new String[]{String.valueOf(linhas.getId()), linhas.getNomeLinhas(), String.valueOf(linhas.getCategoria().getId())});
        }

    }

    @PostMapping("/import-csv-linhas")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        linhasService.readAll(file);
    }

    @GetMapping("/{id}")
    public LinhasDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.linhasService.findById(id);
    }

    @PutMapping("/{id}")
    public LinhasDTO udpate(@PathVariable("id") Long id, @RequestBody LinhasDTO linhasDTO) {
        LOGGER.info("Recebendo Update para linhas de ID: {}", id);
        LOGGER.debug("Payload: {}", linhasDTO);

        return this.linhasService.update(linhasDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para linhas de ID: {}", id);

        this.linhasService.delete(id);
    }
}
