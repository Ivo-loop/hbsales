package br.com.hbsis.linhas;

import br.com.hbsis.categorias.Categoria;
import br.com.hbsis.categorias.CategoriaDTO;
import br.com.hbsis.categorias.CategoriaService;
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
public class LinhasService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhasService.class);
    private final ILinhasRepository iLinhasRepository;
    private final CategoriaService categoriaService;

    @Autowired
    public LinhasService(ILinhasRepository iLinhasRepository, br.com.hbsis.categorias.CategoriaService categoriaService) {
        this.iLinhasRepository = iLinhasRepository;
        this.categoriaService = categoriaService;
    }

    //puxa a linha pelo Id dele, seta ele como DTO
    public LinhasDTO findById(Long id) {
        Optional<Linhas> linhaOptional = this.iLinhasRepository.findById(id);

        if (linhaOptional.isPresent()) {
            return LinhasDTO.of(linhaOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa o fornecedor linha Id dele
    public Linhas findBylinhasId(Long id) {
        Optional<Linhas> linhaOptional = this.iLinhasRepository.findById(id);

        if (linhaOptional.isPresent()) {
            return linhaOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //salva tudo
    public List<Linhas> findAll() {
        return iLinhasRepository.findAll();
    }

    //exporta csv
    public void exportCSV(HttpServletResponse response) throws Exception {
        String nomearquivo = "linhas.csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nomearquivo + "\"");

        PrintWriter writer = response.getWriter();

        ICSVWriter csvWriter = new CSVWriterBuilder(writer).withSeparator(';')
                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();

        for (Linhas linhas : this.findAll()) {
            csvWriter.writeNext(new String[]{String.valueOf(linhas.getId()),
                    linhas.getNomeLinhas(),
                    String.valueOf(linhas.getCategoria().getId())});
        }
    }

    //le csv
    public List<Linhas> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(0).build();

        List<String[]> linhas = csvReader.readAll();
        List<Linhas> resultadoLeitura = new ArrayList<>();

        for (String[] l : linhas) {
            try {
                String[] feijao = l[0].replaceAll("\"", "").split(";");

                Linhas linhasCategoria = new Linhas();
                Categoria categoria = new Categoria();
                CategoriaDTO categoriaDTO;

                linhasCategoria.setId(Long.parseLong(feijao[0]));
                linhasCategoria.setNomeLinhas(feijao[1]);
                categoriaDTO = categoriaService.findById(Long.parseLong(feijao[2]));

                categoria.setId(categoriaDTO.getId());
                categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
                categoria.setCodCategoria(categoriaDTO.getCodCategoria());
                linhasCategoria.setCategoria(categoria);

                resultadoLeitura.add(linhasCategoria);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return iLinhasRepository.saveAll(resultadoLeitura);
    }

    // salva
    public LinhasDTO save(LinhasDTO linhasDTO) {

        this.validate(linhasDTO);

        LOGGER.info("Salvando br.com.hbsis.Linhas");
        LOGGER.debug("br.com.hbsis.Linhas: {}", linhasDTO);

        Linhas linhas = new Linhas();
        linhas.setNomeLinhas(linhasDTO.getNomeLinhas());
        linhas.setCodLinhas(linhasDTO.getCodLinhas());
        Long id = linhasDTO.getidLinhasCategoria();
        Categoria byCategoriaId = categoriaService.findByCategoriaId(id);
        linhas.setCategoria(byCategoriaId);

        linhas = this.iLinhasRepository.save(linhas);

        //Retorna para o postman
        return LinhasDTO.of(linhas);
    }

    //valida
    private void validate(LinhasDTO linhasDTO) {
        LOGGER.info("Validando Linhas");

        if (linhasDTO == null) {
            throw new IllegalArgumentException("LinhasDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(linhasDTO.getNomeLinhas())) {
            throw new IllegalArgumentException("Nome da Linhas não deve ser nula/vazia");
        }

        if (StringUtils.isEmpty(linhasDTO.getCodLinhas())) {
            throw new IllegalArgumentException("Cod não deve ser nulo/vazio");
        }

    }

    // altera
    public LinhasDTO update(LinhasDTO linhasDTO, Long id) {
        Optional<Linhas> LinhasExistenteOptional = this.iLinhasRepository.findById(id);

        if (LinhasExistenteOptional.isPresent()) {
            Linhas LinhasExistente = LinhasExistenteOptional.get();

            LOGGER.info("Atualizando Linhas... id: [{}]", LinhasExistente.getId());
            LOGGER.debug("Payload: {}", linhasDTO);
            LOGGER.debug("linhas Existente: {}", LinhasExistente);

            LinhasExistente.setNomeLinhas(linhasDTO.getNomeLinhas());
            LinhasExistente.setCodLinhas(linhasDTO.getCodLinhas());
            LinhasExistente.setCategoria(categoriaService.findByCategoriaId(id));

            LinhasExistente = this.iLinhasRepository.save(LinhasExistente);

            return LinhasDTO.of(LinhasExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //deleta
    public void delete(Long id) {
        LOGGER.info("Executando delete para linhas de ID: [{}]", id);

        this.iLinhasRepository.deleteById(id);
    }

    //lista
    public List<Linhas> listarLinhas() {
        List<Linhas> linhas;
        linhas = this.iLinhasRepository.findAll();
        return linhas;
    }
}