package br.com.hbsis.Produtos;

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
@RequestMapping("/Produto")
public class ProdutosRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutosRest.class);

    private final ProdutoService produtoService;

    @Autowired
    public ProdutosRest(ProdutoService linhasService) {
        this.produtoService = linhasService;
    }

    @PostMapping("/save")
    public ProdutosDTO save(@RequestBody ProdutosDTO produtosDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Linhas...");
        LOGGER.debug("Payaload: {}", produtosDTO);

        return this.produtoService.save(produtosDTO);
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

        for (Produtos produtos : produtoService.findAll()) {
            csvWriter.writeNext(new String[]{String.valueOf(produtos.getId()), produtos.getNomeProduto(),produtos.getCodProdutos(),
                    String.valueOf(produtos.getLinhas().getId()), String.valueOf(produtos.getPesoPerUni()), String.valueOf(produtos.getUniPerCax())});
        }

    }

    @PostMapping("/import-csv-linhas")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        produtoService.readAll(file);
    }

    @GetMapping("/{id}")
    public ProdutosDTO find(@PathVariable("id") Long id) {

        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        return this.produtoService.findById(id);
    }

    @PutMapping("/{id}")
    public ProdutosDTO udpate(@PathVariable("id") Long id, @RequestBody ProdutosDTO produtosDTO) {
        LOGGER.info("Recebendo Update para linhas de ID: {}", id);
        LOGGER.debug("Payload: {}", produtosDTO);

        return this.produtoService.update(produtosDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para linhas de ID: {}", id);

        this.produtoService.delete(id);
    }
}
