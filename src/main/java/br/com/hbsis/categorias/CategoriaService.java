package br.com.hbsis.categorias;


import br.com.hbsis.fornecedor.FornecedoresDTO;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
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

    @Autowired
    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;
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

        //monta as linhas do arquivo
        for (Categoria linha : this.findAll()) {
            csvWriter.writeNext(new String[]{String.valueOf(linha.getId()), linha.getNomeCategoria(), String.valueOf(linha.getFornecedor().getId())});
        }
    }

    //le csv
    public List<Categoria> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(0).build();

        List<String[]> linhas = csvReader.readAll();
        List<Categoria> resultadoLeitura = new ArrayList<>();

        //passa linhas por linha e inseri
        for (String[] l : linhas) {
            try {
                //substitui aspas por espaço vazio
                String[] bean = l[0].replaceAll("\"", "").split(";");

                Categoria categoria = new Categoria();
                Fornecedor fornecedor = new Fornecedor();
                FornecedoresDTO fornecedorDTO;

                categoria.setId(Long.parseLong(bean[0]));
                categoria.setNomeCategoria(bean[1]);
                fornecedorDTO = fornecedorService.findById(Long.parseLong(bean[2]));

                fornecedor.setId(fornecedorDTO.getId());
                fornecedor.setRazao(fornecedorDTO.getRazao());
                fornecedor.setCnpj(fornecedorDTO.getCnpj());
                fornecedor.setNomefan(fornecedorDTO.getNomeFan());
                fornecedor.setEndereco(fornecedorDTO.getEndereco());
                fornecedor.setTelefone(fornecedorDTO.getTelefone());
                fornecedor.setEmail(fornecedorDTO.getEmail());
                categoria.setFornecedor(fornecedor);

                resultadoLeitura.add(categoria);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        //manda salva tudo
        return iCategoriaRepository.saveAll(resultadoLeitura);
    }

    //Salva Categoria no Database
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        this.validate(categoriaDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Categoria");
        LOGGER.debug("br.com.hbsis.Categoria: {}", categoriaDTO);

        Categoria categoria = new Categoria();

        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoria.setCodCategoria(categoriaDTO.getCodCategoria());
        categoria.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getIdCategoriaFornecedor()));

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
        if (StringUtils.isEmpty(categoriaDTO.getCodCategoria())) {
            throw new IllegalArgumentException("Cod não deve ser nula/vazia");
        }
    }

    // altera as informaçoes da categoria
    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> CategoriaExistencialOpcional = this.iCategoriaRepository.findById(id);

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

    //lista categoria
    public List<Categoria> listarCategoria() {
        List<Categoria> categorias;
        categorias = this.iCategoriaRepository.findAll();
        return categorias;
    }
}
