package br.com.hbsis.categorias;


import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.FornecedoresDTO;
import br.com.hbsis.fornecedor.IFornecedoresRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);
    private final ICategoriaRepository iCategoriaRepository;
    private final FornecedorService fornecedorService;
    private final IFornecedoresRepository iFornecedoresRepository;

    @Autowired
    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService, IFornecedoresRepository iFornecedoresRepository) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;
        this.iFornecedoresRepository = iFornecedoresRepository;
    }

    //busca toda categoria
    public List<Categoria> findAll() {
        return iCategoriaRepository.findAll();
    }

    //busca a Categoria pelo Id, retorna ele como DTO
    public CategoriaDTO findById(Long id) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findById(id);

        if (categoriaOpcional.isPresent()) {
            return CategoriaDTO.of(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //busca a Categoria pelo Id, retorna categoria
    public Categoria findByCategoriaId(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (categoriaOptional.isPresent()) {
            return categoriaOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaDTO findByCodCategoria(String cod) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findByCodCategoria(cod);

        if (categoriaOpcional.isPresent()) {
            return CategoriaDTO.of(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", cod));
    }

    //passa o Database para csv
    public void exportCSV(HttpServletResponse response) throws Exception {
        String nomearquivo = "categorias.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nomearquivo + "\"");

        PrintWriter writer = response.getWriter();

        //-separa os dados do arquivo
        ICSVWriter csvWriter = new CSVWriterBuilder(writer).withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        String nomeColunas[] = {"codigo_categoria", "nome_categoria",
                "razao_social_fornecedor", "cnpj_fornecedor"};
        csvWriter.writeNext(nomeColunas);

        //monta as linhas do arquivo
        for (Categoria linha : this.findAll()) {

            String batata = String.valueOf(linha.getFornecedor().getCnpj());
            String part = batata.substring(0, 2) + "." + batata.substring(2, 5) + "." + batata.substring(5, 8) + "/" +
                    batata.substring(8, 12) + "-" + batata.substring(12, 14);

            csvWriter.writeNext(new String[]{String.valueOf(linha.getCodCategoria()),
                    linha.getNomeCategoria(),
                    linha.getFornecedor().getRazao(),
                    part});
        }
    }

    //le csv
    public List<Categoria> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> linhas = csvReader.readAll();
        List<Categoria> resultadoLeitura = new ArrayList<>();

        //passa linhas por linha e inseri
        for (String[] l : linhas) {
            try {
                //substitui barra por espaço vazio
                String[] bean = l[0].replaceAll("[-\"/]", "")
                        .split(";");

                Categoria categoria = new Categoria();
                Fornecedor fornecedor = new Fornecedor();
                FornecedoresDTO fornecedoresDTO;

                Optional<Categoria> optionalCategoria = this.iCategoriaRepository.findByCodCategoria(bean[0]);

                if (!optionalCategoria.isPresent()) {
                    categoria.setNomeCategoria(bean[1]);
                    categoria.setCodCategoria(bean[0]);
                    Optional<Fornecedor> optionalFornecedor = this.iFornecedoresRepository.findByCnpj(bean[3]);

                    if (optionalFornecedor.isPresent()) {
                        fornecedoresDTO = fornecedorService.findByCnpj(bean[3]);
                        bean[3] = String.valueOf(fornecedoresDTO.getId());
                        fornecedor.setId(Long.parseLong(bean[3]));
                        categoria.setFornecedor(fornecedor);
                        resultadoLeitura.add(categoria);
                        //manda salvar tudo
                        iCategoriaRepository.saveAll(resultadoLeitura);
                    } else {
                        throw new IllegalArgumentException("deu ruim ao buscar o fornecedor");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultadoLeitura;
    }

    //Salva Categoria no Database
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        this.validate(categoriaDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Categoria");
        LOGGER.debug("br.com.hbsis.Categoria: {}", categoriaDTO);

        Categoria categoria = new Categoria();

        String cont = String.valueOf(categoriaDTO.getCodigo());

        for (; cont.length() < 3; ) {
            cont = "0" + cont;
        }

        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoria.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getIdCategoriaFornecedor()));
        categoria.setCodCategoria("CAT" + categoria.getFornecedor().getCnpj().substring(10, 14) + cont);
        System.out.println(categoria.getCodCategoria());

        //Retorna para o postman
        Categoria save = this.iCategoriaRepository.save(categoria);
        return CategoriaDTO.of(save);
    }

    // valida os dados
    private void validate(CategoriaDTO categoriaDTO) {
        LOGGER.info("Validando Categoria");

        if (categoriaDTO == null) {
            throw new IllegalArgumentException("CategoriaDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())) {
            throw new IllegalArgumentException("Nome da categoria não deve ser nula/vazia");
        }
        if (categoriaDTO.getIdCategoriaFornecedor() == null) {
            throw new IllegalArgumentException("id do fornecedor nao pode ser nulo");
        }
        if (categoriaDTO.getCodigo() == null) {
            throw new IllegalArgumentException("Numero nao pode ser nulo");
        }
        if (StringUtils.isBlank(categoriaDTO.getCodigo())) {
            throw new IllegalArgumentException("Numero nao pode ser maior que 3");
        }
    }

    // altera as informaçoes da categoria
    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> CategoriaExistencialOpcional = this.iCategoriaRepository.findById(id);
        this.validate(categoriaDTO);
        if (CategoriaExistencialOpcional.isPresent()) {
            Categoria categoriaExistente = CategoriaExistencialOpcional.get();

            LOGGER.info("Atualizando Categoria... id: [{}]", categoriaExistente.getId());
            LOGGER.debug("Payload: {}", categoriaDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaExistente);

            categoriaExistente.setNomeCategoria(categoriaDTO.getNomeCategoria());
            categoriaExistente.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getIdCategoriaFornecedor()));

            categoriaExistente = this.iCategoriaRepository.save(categoriaExistente);

            return br.com.hbsis.categorias.CategoriaDTO.of(categoriaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    // deleta a categoria
    public void delete(Long id) {
        LOGGER.info("Executando delete para Categoria de ID: [{}]", id);

        this.iCategoriaRepository.deleteById(id);
    }
}
