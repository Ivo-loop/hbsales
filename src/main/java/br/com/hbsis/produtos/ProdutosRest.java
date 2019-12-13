package br.com.hbsis.produtos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/produto")
public class ProdutosRest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutosRest.class);

    private final ProdutoService produtoService;

    @Autowired
    public ProdutosRest(ProdutoService linhasService) {
        this.produtoService = linhasService;
    }

    @PostMapping
    public ProdutosDTO save(@RequestBody ProdutosDTO produtosDTO) {
        LOGGER.info("Recebendo solicitação de persistência de Linhas...");
        LOGGER.debug("Payaload: {}", produtosDTO);

        //manda salva
        return this.produtoService.save(produtosDTO);
    }

    @PostMapping("/import-produtos-fornecedor/{id}")
    public void importProdutoFornecedor(@PathVariable("id") String cod, @RequestParam MultipartFile file) throws Exception {
        LOGGER.info("atualizando Produtos do Fornecedor de ID... [{}]", cod);

        //manda importa
        //produtoService.importProdutoFornecedor(cod, file);
    }

    @GetMapping("/export-csv")
    public void exportCSV(HttpServletResponse retorno) throws Exception {
        LOGGER.info("Recebendo solicitação de exportacao de categoria...");

        //manda exporta
       // produtoService.exportCSV(retorno);
    }

    @PostMapping("/import-csv")
    public void importCSV(@RequestParam("file") MultipartFile file) throws Exception {
        LOGGER.info("Recebendo solicitação de importacao de categoria...");

        //manda importa
        //produtoService.readAll(file);
    }

    @GetMapping("/{id}")
    public ProdutosDTO find(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo find by ID... id: [{}]", id);

        //manda buscar o produto diferenciado
        return this.produtoService.findById(id);
    }

    @PutMapping("/{id}")
    public ProdutosDTO udpate(@PathVariable("id") Long id, @RequestBody ProdutosDTO produtosDTO) {
        LOGGER.info("Recebendo Update para linhas de ID: {}", id);
        LOGGER.debug("Payload: {}", produtosDTO);

        //manda alterar
        return this.produtoService.update(produtosDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        LOGGER.info("Recebendo Delete para linhas de ID: {}", id);

        //manda sumir
        this.produtoService.delete(id);
    }
}
