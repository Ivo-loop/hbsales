package br.com.hbsis.produtos;

import br.com.hbsis.fornecedor.IFornecedoresRepository;
import br.com.hbsis.linhas.Linhas;
import br.com.hbsis.linhas.LinhasDTO;
import br.com.hbsis.linhas.LinhasService;
import com.opencsv.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final IProdutosRepository iProdutosRepository;
    private final LinhasService linhasService;
    private final IFornecedoresRepository iFornecedoresRepository;

    @Autowired
    public ProdutoService(IProdutosRepository iProdutosRepository, LinhasService linhasService, IFornecedoresRepository iFornecedoresRepository) {
        this.iProdutosRepository = iProdutosRepository;
        this.linhasService = linhasService;
        this.iFornecedoresRepository = iFornecedoresRepository;
    }

    //busca tudo
    public List<Produtos> findAll() {
        return iProdutosRepository.findAll();
    }

    //busca produto pro Id
    public ProdutosDTO findById(Long id) {
        Optional<Produtos> ProdutoOpcional = this.iProdutosRepository.findById(id);

        if (ProdutoOpcional.isPresent()) {
            return ProdutosDTO.of(ProdutoOpcional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //importa produto do para o fornecedor
    public void importProdutoFornecedor(Long id, MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());

        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
                .withSkipLines(0)
                .build();

        List<String[]> linhaString = csvReader.readAll();

        for (String[] linha : linhaString) {
            try {
                String[] feijao = linha[0].replaceAll("\"", "").split(";");

                Produtos produtos = new Produtos();
                if (iFornecedoresRepository.existsById(id)) {
                    produtos.setId(Long.parseLong(feijao[0]));
                    produtos.setNomeProduto(feijao[1]);
                    produtos.setCodProdutos(feijao[2]);
                    produtos.setPreco(Float.parseFloat(feijao[3]));
                    produtos.setLinhas(linhasService.findBylinhasId(Long.parseLong(feijao[4])));

                    produtos.setUniPerCax(Float.parseFloat(feijao[5]));
                    produtos.setPesoPerUni(Float.parseFloat(feijao[6]));
                    produtos.setValidade(LocalDateTime.parse(feijao[7]));

                    if (iProdutosRepository.existsById(produtos.getId()) &&
                            id.equals(produtos.getLinhas().getCategoria().getFornecedor().getId())) {

                        produtos.setId(iProdutosRepository.findById(produtos.getId()).get().getId());
                        update(ProdutosDTO.of(produtos), produtos.getId());

                    } else if (id.equals(produtos.getLinhas().getCategoria().getFornecedor().getId())) {
                        iProdutosRepository.save(produtos);
                    } else {
                        LOGGER.info("deu rolo nao conseguimos roubar o produto: ", produtos.getId());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //exporta
    public void exportCSV(HttpServletResponse retorno) throws Exception {
        String nameArq = "produto.csv";
        retorno.setContentType("text/csv");
        retorno.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nameArq + "\"");

        PrintWriter info = retorno.getWriter();

        ICSVWriter csvInfo = new CSVWriterBuilder(info).withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        for (Produtos produtos : this.findAll()) {
            csvInfo.writeNext(new String[]{String.valueOf(produtos.getId()),
                    produtos.getNomeProduto(),
                    produtos.getCodProdutos(),
                    String.valueOf(produtos.getPreco()),
                    String.valueOf(produtos.getLinhas().getId()),
                    String.valueOf(produtos.getUniPerCax()),
                    String.valueOf(produtos.getPesoPerUni()),
                    String.valueOf(produtos.getValidade())});
        }
    }

    // faz a importacao
    public List<Produtos> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(0).build();

        List<String[]> linhas = csvReader.readAll();
        List<Produtos> resultadoLeitura = new ArrayList<>();

        for (String[] l : linhas) {
            try {
                String[] feijao = l[0].replaceAll("\"", "").split(";");

                Produtos produtos = new Produtos();
                Linhas linhasClass = new Linhas();
                LinhasDTO linhasDTO;

                produtos.setId(Long.parseLong(feijao[0]));
                produtos.setNomeProduto(feijao[1]);
                produtos.setCodProdutos(feijao[2]);
                produtos.setPreco(Float.parseFloat(feijao[3]));

                linhasDTO = linhasService.findById(Long.parseLong(feijao[4]));

                produtos.setUniPerCax(Float.parseFloat(feijao[5]));
                produtos.setPesoPerUni(Float.parseFloat(feijao[6]));
                produtos.setValidade(LocalDateTime.parse(feijao[7]));

                linhasClass.setId(linhasDTO.getId());
                linhasClass.setNomeLinhas(linhasDTO.getNomeLinhas());
                linhasClass.setCodLinhas(linhasDTO.getCodLinhas());
                produtos.setLinhas(linhasClass);

                produtos.setLinhas(linhasClass);

                resultadoLeitura.add(produtos);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return iProdutosRepository.saveAll(resultadoLeitura);
    }

    //salva
    public ProdutosDTO save(ProdutosDTO produtosDTO) {
        this.validate(produtosDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Produto");
        LOGGER.debug("br.com.hbsis.Produto: {}", produtosDTO);

        Produtos produtos = new Produtos();

        produtos.setNomeProduto(produtosDTO.getNomeProduto());
        produtos.setCodProdutos(produtosDTO.getCodProdutos());
        produtos.setPreco(produtosDTO.getPreco());
        produtos.setPesoPerUni(produtosDTO.getPesoPerUni());
        produtos.setUniPerCax(produtosDTO.getUniPerCax());
        produtos.setValidade(produtosDTO.getValidade());
        Long id = produtosDTO.getIdProdutosLinhas();

        produtos.setLinhas(linhasService.findBylinhasId(id));

        //Retorna para o postman
        Produtos save = this.iProdutosRepository.save(produtos);
        return ProdutosDTO.of(save);
    }

    //valida
    private void validate(ProdutosDTO produtosDTO) {
        LOGGER.info("Validando Produto");

        if (produtosDTO == null) {
            throw new IllegalArgumentException("ProdutoDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(produtosDTO.getNomeProduto())) {
            throw new IllegalArgumentException("Nome da Produto não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(produtosDTO.getCodProdutos())) {
            throw new IllegalArgumentException("Cod linhas não deve ser nula/vazia");
        }
        if (produtosDTO.getPesoPerUni() == null) {
            throw new IllegalArgumentException("Cod peso por unidade não deve ser nula/vazia");
        }
        if (produtosDTO.getUniPerCax() == null) {
            throw new IllegalArgumentException("Cod peso por unidade não deve ser nula/vazia");
        }
    }

    //altera
    public ProdutosDTO update(ProdutosDTO produtosDTO, Long id) {
        Optional<Produtos> ProdutoExistencialOpcional = this.iProdutosRepository.findById(id);

        if (ProdutoExistencialOpcional.isPresent()) {
            Produtos produtosExistente = ProdutoExistencialOpcional.get();

            LOGGER.info("Atualizando Produto... id: [{}]", produtosExistente.getId());
            LOGGER.debug("Payload: {}", produtosDTO);
            LOGGER.debug("Produto Existente: {}", produtosExistente);

            produtosExistente.setNomeProduto(produtosDTO.getNomeProduto());
            produtosExistente.setCodProdutos(produtosDTO.getCodProdutos());
            produtosExistente.setPreco(produtosDTO.getPreco());
            produtosExistente.setLinhas(linhasService.findBylinhasId(produtosDTO.getIdProdutosLinhas()));
            produtosExistente.setUniPerCax(produtosDTO.getUniPerCax());
            produtosExistente.setPesoPerUni(produtosDTO.getPesoPerUni());
            produtosExistente.setValidade(produtosDTO.getValidade());

            produtosExistente = this.iProdutosRepository.save(produtosExistente);

            return ProdutosDTO.of(produtosExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //deleta
    public void delete(Long id) {
        LOGGER.info("Executando delete para Produto de ID: [{}]", id);

        this.iProdutosRepository.deleteById(id);
    }

    //lista
    public List<Produtos> listarProdutos() {
        List<Produtos> produtos;
        produtos = this.iProdutosRepository.findAll();
        return produtos;
    }

}
