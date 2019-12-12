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

    //puxa o fornecedor pelo Cnpj dele
    public FornecedoresDTO findByCnpj(String cnpj) {
        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findByCnpj(cnpj);

        if (fornecedorOptional.isPresent()) {
            return FornecedoresDTO.of(fornecedorOptional.get());
        }
        throw new IllegalArgumentException(String.format("Cnpj %s não existe", cnpj));
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
                    String.valueOf(fornecedor.getCnpj()),
                    fornecedor.getNomefan(),
                    fornecedor.getEndereco(),
                    String.valueOf(fornecedor.getTelefone()),
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
                fornecedor.setTelefone(Long.parseLong(bean[6]));
                fornecedor.setEmail(bean[7]);

                resultadoLeitura.add(fornecedor);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return iFonecedoresRepository.saveAll(resultadoLeitura);
    }

    //salva o fornecedor no Database
    public FornecedoresDTO save(FornecedoresDTO fornecedoresDTO) {

        this.validate(fornecedoresDTO);

        LOGGER.info("Salvando br.com.hbsis.fornecedor");
        LOGGER.debug("br.com.hbsis.fornecedor: {}", fornecedoresDTO);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazao(fornecedoresDTO.getRazao());
        fornecedor.setCnpj(fornecedoresDTO.getCnpj());
        fornecedor.setNomefan(fornecedoresDTO.getNomeFan());
        fornecedor.setEndereco(fornecedoresDTO.getEndereco());
        fornecedor.setTelefone(fornecedoresDTO.getTelefone());
        fornecedor.setEmail(fornecedoresDTO.getEmail());

        fornecedor = this.iFonecedoresRepository.save(fornecedor);

        //Retorna para o postman
        return FornecedoresDTO.of(fornecedor);
    }

    //valida as informacoes
    private void validate(FornecedoresDTO fornecedoresDTO) {
        LOGGER.info("Validando Fornecedor");

        if (fornecedoresDTO == null) {
            throw new IllegalArgumentException("fonecedoresDTO não deve ser nulo");
        }

        if (fornecedoresDTO.getCnpj() == null) {
            throw new IllegalArgumentException("Cnpj não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(fornecedoresDTO.getRazao())) {
            throw new IllegalArgumentException("Razao não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedoresDTO.getEmail())) {
            throw new IllegalArgumentException("email não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(fornecedoresDTO.getEndereco())) {
            throw new IllegalArgumentException("endereço não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedoresDTO.getNomeFan())) {
            throw new IllegalArgumentException("nome fantasia não deve ser nula/vazia");
        }
        if (fornecedoresDTO.getTelefone() == null) {
            throw new IllegalArgumentException("telefone não deve ser nulo/vazio");
        }
        String cont1 = String.valueOf(fornecedoresDTO.getCnpj());
        if (cont1.length() != 14) {
            throw new IllegalArgumentException("Cnpj diferente que 14," +
                    "Confira se colocou algum caracter especial");
        }
        String cont2 = String.valueOf(fornecedoresDTO.getTelefone());
        if (cont2.length() != 13) {
            throw new IllegalArgumentException("telefone diferente que 13 digitos, confira se possui DDD e DDI");
        }
        if (fornecedoresDTO.getEmail().length() > 50) {
            throw new IllegalArgumentException("email muito grande sinto muito");
        }
    }

    // Altera as informacoes do banco
    public FornecedoresDTO update(FornecedoresDTO fonecedoresDTO, Long id) {
        Optional<Fornecedor> fornecedorExistenteOptional = this.iFonecedoresRepository.findById(id);
        this.validate(fonecedoresDTO);
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