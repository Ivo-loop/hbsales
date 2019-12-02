package br.com.hbsis.LinhasCategorias;

import br.com.hbsis.CategoriaProduto.Categoria;
import br.com.hbsis.CategoriaProduto.CategoriaDTO;
import br.com.hbsis.CategoriaProduto.CategoriaService;
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
public class LinhasService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhasService.class);
    private final ILinhasRepository iLinhasRepository;
    private final CategoriaService CategoriaService;

    @Autowired
    public LinhasService(ILinhasRepository iLinhasRepository, br.com.hbsis.CategoriaProduto.CategoriaService categoriaService) {
        this.iLinhasRepository = iLinhasRepository;
        CategoriaService = categoriaService;
    }

    public LinhasDTO findById(Long id) {
        Optional<Linhas> LinhaOptional = this.iLinhasRepository.findById(id);

        if (LinhaOptional.isPresent()) {
            return LinhasDTO.OF(LinhaOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<Linhas> findAll() {
        return iLinhasRepository.findAll();
    }

    public List<Linhas> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(0).build();

        List<String[]> linhas = csvReader.readAll();
        List<Linhas> resultadoLeitura = new ArrayList<>();

        for (String[] l : linhas) {
            try {
                String[] bean = l[0].replaceAll("\"", "").split(";");

                Linhas linhasCategoria = new Linhas();
                Categoria categoria = new Categoria();
                CategoriaDTO categoriaDTO = new CategoriaDTO();

                linhasCategoria.setId(Long.parseLong(bean[0]));
                linhasCategoria.setNomeLinhas(bean[1]);
                categoriaDTO = CategoriaService.findById(Long.parseLong(bean[2]));

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

    public LinhasDTO save(LinhasDTO linhasDTO) {

        this.validate(linhasDTO);

        LOGGER.info("Salvando br.com.hbsis.Linhas");
        LOGGER.debug("br.com.hbsis.Linhas: {}", linhasDTO);

        Linhas linhas = new Linhas();
        linhas.setNomeLinhas(linhasDTO.getNomeLinhas());
        linhas.setCodLinhas(linhasDTO.getCodLinhas());
        Long id = linhasDTO.getidLinhasCategoria();
        Categoria byCategoriaId = CategoriaService.findByCategoriaId(id);
        linhas.setCategoria(byCategoriaId);

        linhas = this.iLinhasRepository.save(linhas);

        //Retorna para o postman
        return linhasDTO.OF(linhas);
    }

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

    public LinhasDTO update(LinhasDTO linhasDTO, Long id) {
        Optional<Linhas> LinhasExistenteOptional = this.iLinhasRepository.findById(id);

        if (LinhasExistenteOptional.isPresent()) {
            Linhas LinhasExistente = LinhasExistenteOptional.get();

            LOGGER.info("Atualizando Linhas... id: [{}]", LinhasExistente.getId());
            LOGGER.debug("Payload: {}", linhasDTO);
            LOGGER.debug("linhas Existente: {}", LinhasExistente);

            LinhasExistente.setNomeLinhas(linhasDTO.getNomeLinhas());

            LinhasExistente = this.iLinhasRepository.save(LinhasExistente);

            return LinhasDTO.OF(LinhasExistente);
        }


        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para linhas de ID: [{}]", id);

        this.iLinhasRepository.deleteById(id);
    }

    public List<Linhas> listarLinhas() {
        List<Linhas> linhas = new ArrayList<>();
        linhas = this.iLinhasRepository.findAll();
        return linhas;
    }

}

