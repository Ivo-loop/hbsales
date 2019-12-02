package br.com.hbsis.CategoriaProduto;


import br.com.hbsis.fornecedor.FonecedoresDTO;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
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
public class CategoriaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);
    private final ICategoriaRepository iCategoriaRepository;
    private final FornecedorService fornecedorService;

    @Autowired
    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;
    }

    public List<Categoria> findAll() {
        return iCategoriaRepository.findAll();
    }

    public CategoriaDTO findById(Long id) {
        Optional<Categoria> CategoriaOpcional = this.iCategoriaRepository.findById(id);

        if (CategoriaOpcional.isPresent()) {
            return CategoriaDTO.OF(CategoriaOpcional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Categoria findByCategoriaId(Long id) {
        Optional<Categoria> CategoriaOptional = this.iCategoriaRepository.findById(id);

        if (CategoriaOptional.isPresent()) {
            return CategoriaOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<Categoria> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(0).build();

        List<String[]> linhas = csvReader.readAll();
        List<Categoria> resultadoLeitura = new ArrayList<>();

        for (String[] l : linhas) {
            try {
                String[] bean = l[0].replaceAll("\"", "").split(";");

                Categoria categoria = new Categoria();
                Fornecedor fornecedor = new Fornecedor();
                FonecedoresDTO fornecedorDTO = new FonecedoresDTO();

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
        return iCategoriaRepository.saveAll(resultadoLeitura);
    }

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
        return CategoriaDTO.OF(save);
    }

    private void validate(CategoriaDTO CategoriaDTO) {
        LOGGER.info("Validando Categoria");

        if (CategoriaDTO == null) {
            throw new IllegalArgumentException("CategoriaDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(CategoriaDTO.getNomeCategoria())) {
            throw new IllegalArgumentException("Nome da categoria não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(CategoriaDTO.getCodCategoria())) {
            throw new IllegalArgumentException("Cod não deve ser nula/vazia");
        }

    }

    public CategoriaDTO update(CategoriaDTO CategoriaDTO, Long id) {
        Optional<Categoria> CategoriaExistencialOpcional = this.iCategoriaRepository.findById(id);

        if (CategoriaExistencialOpcional.isPresent()) {
            Categoria categoriaExistente = CategoriaExistencialOpcional.get();

            LOGGER.info("Atualizando Categoria... id: [{}]", categoriaExistente.getId());
            LOGGER.debug("Payload: {}", CategoriaDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaExistente);

            categoriaExistente.setNomeCategoria(CategoriaDTO.getNomeCategoria());


            categoriaExistente = this.iCategoriaRepository.save(categoriaExistente);

            return CategoriaDTO.OF(categoriaExistente);
        }


        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para Categoria de ID: [{}]", id);

        this.iCategoriaRepository.deleteById(id);
    }

    public List<Categoria> listarCategoria() {
        List<Categoria> categorias = new ArrayList<>();
        categorias = this.iCategoriaRepository.findAll();
        return categorias;
    }

}
