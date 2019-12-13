package br.com.hbsis.linhas;

import br.com.hbsis.categorias.Categoria;
import br.com.hbsis.categorias.CategoriaDTO;
import br.com.hbsis.categorias.CategoriaService;
import br.com.hbsis.categorias.ICategoriaRepository;
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
    private final ICategoriaRepository iCategoriaRepository;

    @Autowired
    public LinhasService(ILinhasRepository iLinhasRepository, CategoriaService categoriaService, ICategoriaRepository iCategoriaRepository) {
        this.iLinhasRepository = iLinhasRepository;
        this.categoriaService = categoriaService;
        this.iCategoriaRepository = iCategoriaRepository;
    }

    //puxa a linha pelo Id dele, retorna como DTO
    public LinhasDTO findById(Long id) {
        Optional<Linhas> linhaOptional = this.iLinhasRepository.findById(id);

        if (linhaOptional.isPresent()) {
            return LinhasDTO.of(linhaOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa o linhas pelo Id dela
    public Linhas findBylinhasId(Long id) {
        Optional<Linhas> linhaOptional = this.iLinhasRepository.findById(id);

        if (linhaOptional.isPresent()) {
            return linhaOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa as linhas pelo cod dela
    public LinhasDTO findByCodLinhas(String cod) {
        Optional<Linhas> linhaOptional = this.iLinhasRepository.findByCodLinhas(cod);

        if (linhaOptional.isPresent()) {
            return LinhasDTO.of(linhaOptional.get());
        }
        throw new IllegalArgumentException(String.format("Cod %s não existe", cod));
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

        String nomeColunas[] = {"codigo", "nome",
                "codigo_categoria", "nome_categoria"};
        csvWriter.writeNext(nomeColunas);

        for (Linhas linhas : this.findAll()) {
            csvWriter.writeNext(new String[]{linhas.getCodLinhas(),
                    linhas.getNomeLinhas(),
                    linhas.getCategoria().getCodCategoria(),
                    linhas.getCategoria().getNomeCategoria()
            });
        }
    }

    //le csv
    public List<Linhas> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();

        List<String[]> linhas = csvReader.readAll();
        List<Linhas> resultadoLeitura = new ArrayList<>();

        for (String[] l : linhas) {
            try {
                String[] bean = l[0].replaceAll("\"", "").split(";");

                Linhas linhasCategoria = new Linhas();
                Categoria categoria = new Categoria();
                CategoriaDTO categoriaDTO;

                Optional<Linhas> optionalLinhas = this.iLinhasRepository.findByCodLinhas(bean[0]);

                if(!optionalLinhas.isPresent()) {

                    linhasCategoria.setCodLinhas(bean[0]);
                    linhasCategoria.setNomeLinhas(bean[1]);
                    Optional<Categoria> optionalCategoria = iCategoriaRepository.findByCodCategoria(bean[3]);

                    if (optionalCategoria.isPresent()) {
                        categoriaDTO = categoriaService.findByCodCategoria(bean[3]);
                        categoria.setId(categoriaDTO.getId());
                        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
                        linhasCategoria.setCategoria(categoria);

                        resultadoLeitura.add(linhasCategoria);
                        //manda salvar
                        return iLinhasRepository.saveAll(resultadoLeitura);
                    }else{
                        throw new IllegalArgumentException("deu ruim pegar categoria");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return resultadoLeitura;
    }

    // salva
    public LinhasDTO save(LinhasDTO linhasDTO) {
        this.validate(linhasDTO);

        LOGGER.info("Salvando br.com.hbsis.Linhas");
        LOGGER.debug("br.com.hbsis.Linhas: {}", linhasDTO);

        String cont = String.valueOf(linhasDTO.getCodLinhas());

        for (; cont.length() < 10; ) {
            cont = "0" + cont;
        }

        Linhas linhas = new Linhas();
        linhas.setNomeLinhas(linhasDTO.getNomeLinhas());
        linhas.setCodLinhas(cont.toUpperCase());
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
        if (linhasDTO.getidLinhasCategoria() == null) {
            throw new IllegalArgumentException("Id Categoria nao deve ser nula/vazia");
        }
        if (linhasDTO.getCodLinhas().length() > 10) {
            throw new IllegalArgumentException("Cod maior que 10");
        }
    }

    // altera
    public LinhasDTO update(LinhasDTO linhasDTO, Long id) {
        Optional<Linhas> LinhasExistenteOptional = this.iLinhasRepository.findById(id);
        this.validate(linhasDTO);
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
}