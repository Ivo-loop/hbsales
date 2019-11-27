package br.com.hbsis.CategoriaProduto;

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
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
@RequestMapping("/Produto")
public class ProdutoRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoRest.class);

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoRest(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping("/save")
    public ProdutoDTO save(@RequestBody ProdutoDTO produtoDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Fornecedor...");
        LOGGER.debug("Payaload: {}", produtoDTO);

        return this.produtoService.save(produtoDTO);
    }

    @GetMapping("/export-csv-categorias")
    public void exportCSV(HttpServletResponse response) throws Exception {
        String nomearquivo = "categorias.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nomearquivo + "\"");


        PrintWriter writer = response.getWriter();

        ICSVWriter csvWriter = new CSVWriterBuilder(writer)
                .withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END)
                .build();
        String headerCSV[] = {"id_categoria","nome_categoria", "id_fornecedor"};
        csvWriter.writeNext(headerCSV);

        for (Produto linha : produtoService.findAll()) {
            csvWriter.writeNext(new String[] {String.valueOf(linha.getId()), linha.getNomeProduto(), String.valueOf(linha.getId_produto_fornecedor().getId())});
        }

    }
    @PostMapping("/import-csv-categorias")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {

        Reader reader = Files.newBufferedReader(Paths.get("C:\\hbsales\\categorias.csv"));
        produtoService.saveAll(produtoService.readAll(reader));
    }

    @GetMapping("/{id}")
    public ProdutoDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.produtoService.findById(id);
    }

    @PutMapping("/{id}")
    public ProdutoDTO udpate(@PathVariable("id") Long id, @RequestBody ProdutoDTO fonecedoresDTO) {
        LOGGER.info("Recebendo Update para Fornecedor de ID: {}", id);
        LOGGER.debug("Payload: {}", fonecedoresDTO);

        return this.produtoService.update(fonecedoresDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para Fornecedor de ID: {}", id);

        this.produtoService.delete(id);
    }


}
