package br.com.hbsis.fornecedor;

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
public class FornecedorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);
    private final IFornecedoresRepository iFonecedoresRepository;

    @Autowired
    public FornecedorService(IFornecedoresRepository iFonecedoresRepository) {
        this.iFonecedoresRepository = iFonecedoresRepository;
    }

    //puxa o fornecedor pelo Id dele, seta ele como DTO
    public FornecedoresDTO findById(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return FornecedoresDTO.of(fornecedorOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa o fornecedor pelo Id dele
    public Fornecedor findByFornecedorId(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //busca tudo
    public List<Fornecedor> findAll() {
        return iFonecedoresRepository.findAll();
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

        for (Fornecedor fornecedor : this.findAll()) {
            csvInfo.writeNext(new String[]{String.valueOf(fornecedor.getId()),
                    fornecedor.getRazao(),
                    fornecedor.getCnpj(),
                    fornecedor.getNomefan(),
                    fornecedor.getEndereco(),
                    fornecedor.getTelefone(),
                    fornecedor.getEmail()});
        }
    }

    // faz a importacao
    public List<Fornecedor> readAll(MultipartFile file) throws Exception {
        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(0).build();

        List<String[]> linhas = csvReader.readAll();
        List<Fornecedor> resultadoLeitura = new ArrayList<>();

        for (String[] l : linhas) {
            try {
                String[] bean = l[0].replaceAll("\"", "").split(";");

                Fornecedor fornecedor = new Fornecedor();

                fornecedor.setId(Long.parseLong(bean[0]));
                fornecedor.setRazao(bean[1]);
                fornecedor.setCnpj(bean[2]);
                fornecedor.setNomefan(bean[3]);
                fornecedor.setEndereco(bean[5]);
                fornecedor.setTelefone(bean[6]);
                fornecedor.setEmail(bean[7]);

                resultadoLeitura.add(fornecedor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return iFonecedoresRepository.saveAll(resultadoLeitura);
    }

    //salva o fornecedor no Database
    public FornecedoresDTO save(FornecedoresDTO fonecedoresDTO) {

        this.validate(fonecedoresDTO);

        LOGGER.info("Salvando br.com.hbsis.fornecedor");
        LOGGER.debug("br.com.hbsis.fornecedor: {}", fonecedoresDTO);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazao(fonecedoresDTO.getRazao());
        fornecedor.setCnpj(fonecedoresDTO.getCnpj());
        fornecedor.setNomefan(fonecedoresDTO.getNomeFan());
        fornecedor.setEndereco(fonecedoresDTO.getEndereco());
        fornecedor.setTelefone(fonecedoresDTO.getTelefone());
        fornecedor.setEmail(fonecedoresDTO.getEmail());

        fornecedor = this.iFonecedoresRepository.save(fornecedor);

        //Retorna para o postman
        return FornecedoresDTO.of(fornecedor);
    }

    //valida as informacoes
    private void validate(FornecedoresDTO fonecedoresDTO) {
        LOGGER.info("Validando Fornecedor");

        if (fonecedoresDTO == null) {
            throw new IllegalArgumentException("fonecedoresDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(fonecedoresDTO.getCnpj())) {
            throw new IllegalArgumentException("Cnpj não deve ser nula/vazia");
        }
        if (fonecedoresDTO.getCnpj().length() != 14) {
            throw new IllegalArgumentException("Cnpj possui mais ou menos caracteres nula/vazia");
        }
        if (StringUtils.isEmpty(fonecedoresDTO.getRazao())) {
            throw new IllegalArgumentException("Razao não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fonecedoresDTO.getEmail())) {
            throw new IllegalArgumentException("email não deve ser nula/vazia");
        }

        if (StringUtils.isEmpty(fonecedoresDTO.getEndereco())) {
            throw new IllegalArgumentException("endereço não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fonecedoresDTO.getNomeFan())) {
            throw new IllegalArgumentException("nome fantasia não deve ser nula/vazia");
        }

        if (StringUtils.isEmpty(fonecedoresDTO.getTelefone())) {
            throw new IllegalArgumentException("telefone não deve ser nulo/vazio");
        }

    }

    // Altera as informacoes do banco
    public FornecedoresDTO update(FornecedoresDTO fonecedoresDTO, Long id) {
        Optional<Fornecedor> fornecedorExistenteOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorExistenteOptional.isPresent()) {
            Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();

            LOGGER.info("Atualizando Fornecedor... id: [{}]", fornecedorExistente.getId());
            LOGGER.debug("Payload: {}", fonecedoresDTO);
            LOGGER.debug("Fornecedor Existente: {}", fornecedorExistente);

            fornecedorExistente.setRazao(fonecedoresDTO.getRazao());
            fornecedorExistente.setCnpj(fonecedoresDTO.getCnpj());
            fornecedorExistente.setNomefan(fonecedoresDTO.getNomeFan());
            fornecedorExistente.setEndereco(fonecedoresDTO.getEndereco());
            fornecedorExistente.setTelefone(fonecedoresDTO.getTelefone());
            fornecedorExistente.setEmail(fonecedoresDTO.getEmail());

            fornecedorExistente = this.iFonecedoresRepository.save(fornecedorExistente);

            return FornecedoresDTO.of(fornecedorExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //delete o fornecedor do Database
    public void delete(Long id) {
        LOGGER.info("Executando delete para Fornecedor de ID: [{}]", id);

        this.iFonecedoresRepository.deleteById(id);
    }

    //listar os fornecedores
    public List<Fornecedor> listarForne() {
        List<Fornecedor> fornecedores;
        fornecedores = this.iFonecedoresRepository.findAll();
        return fornecedores;
    }
}