package br.com.hbsis.Produtos;

import br.com.hbsis.LinhasCategorias.Linhas;
import br.com.hbsis.LinhasCategorias.LinhasDTO;
import br.com.hbsis.LinhasCategorias.LinhasService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final IProdutosRepository iProdutosRepository;
    private final LinhasService linhasService;

    @Autowired
    public ProdutoService(IProdutosRepository iProdutosRepository, LinhasService linhasService) {
        this.iProdutosRepository = iProdutosRepository;
        this.linhasService = linhasService;
    }

    public List<Produtos> findAll() {
        return iProdutosRepository.findAll();
    }

    public ProdutosDTO findById(Long id) {
        Optional<Produtos> ProdutoOpcional = this.iProdutosRepository.findById(id);

        if (ProdutoOpcional.isPresent()) {
            return ProdutosDTO.OF(ProdutoOpcional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }


    public List<Produtos> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(0).build();

        List<String[]> linhas = csvReader.readAll();
        List<Produtos> resultadoLeitura = new ArrayList<>();

        for (String[] l : linhas) {
            try {
                String[] bean = l[0].replaceAll("\"", "").split(";");

                Produtos produtosLinhas = new Produtos();
                Linhas linhas1 = new Linhas();
                LinhasDTO linhasDTO = new LinhasDTO();

                produtosLinhas.setId(Long.parseLong(bean[0]));
                produtosLinhas.setNomeProduto(bean[1]);
                linhasDTO = linhasService.findById(Long.parseLong(bean[2]));

                linhas1.setId(linhasDTO.getId());
                linhas1.setNomeLinhas(linhasDTO.getNomeLinhas());
                linhas1.setCodLinhas(linhasDTO.getCodLinhas());
                produtosLinhas.setLinhas(linhas1);

                produtosLinhas.setLinhas(linhas1);

                resultadoLeitura.add(produtosLinhas);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return iProdutosRepository.saveAll(resultadoLeitura);
    }

    public ProdutosDTO save(ProdutosDTO produtosDTO) {
        this.validate(produtosDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Categoria");
        LOGGER.debug("br.com.hbsis.Categoria: {}", produtosDTO);

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
        return ProdutosDTO.OF(save);
    }

    private void validate(ProdutosDTO linhasDTO) {
        LOGGER.info("Validando Categoria");

        if (linhasDTO == null) {
            throw new IllegalArgumentException("CategoriaDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(linhasDTO.getNomeProduto())) {
            throw new IllegalArgumentException("Nome da categoria não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(linhasDTO.getCodProdutos())) {
            throw new IllegalArgumentException("Cod não deve ser nula/vazia");
        }
        if (linhasDTO.getPesoPerUni() == null) {
            throw new IllegalArgumentException("Cod não deve ser nula/vazia");
        }
    }

    public ProdutosDTO update(ProdutosDTO linhasDTO, Long id) {
        Optional<Produtos> ProdutoExistencialOpcional = this.iProdutosRepository.findById(id);

        if (ProdutoExistencialOpcional.isPresent()) {
            Produtos produtosExistente = ProdutoExistencialOpcional.get();

            LOGGER.info("Atualizando Categoria... id: [{}]", produtosExistente.getId());
            LOGGER.debug("Payload: {}", linhasDTO);
            LOGGER.debug("Categoria Existente: {}", produtosExistente);

            produtosExistente.setNomeProduto(linhasDTO.getNomeProduto());


            produtosExistente = this.iProdutosRepository.save(produtosExistente);

            return ProdutosDTO.OF(produtosExistente);
        }


        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para Categoria de ID: [{}]", id);

        this.iProdutosRepository.deleteById(id);
    }

    public List<Produtos> listarCategoria() {
        List<Produtos> produtos = new ArrayList<>();
        produtos = this.iProdutosRepository.findAll();
        return produtos;
    }

}
