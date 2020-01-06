package br.com.hbsis.fornecedor;

import br.com.hbsis.categorias.AlterCod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);
    private final IFornecedorRepository iFornecedorRepository;
    private final FornecedorValidate fornecedorValidate;
    private final FornecedorFind fornecedorFind;
    private final AlterCod alterCod;

    @Autowired
    public FornecedorService(IFornecedorRepository iFornecedorRepository, FornecedorFind fornecedorFind, FornecedorValidate fornecedorValidate, AlterCod alterCod) {
        this.iFornecedorRepository = iFornecedorRepository;
        this.fornecedorValidate = fornecedorValidate;
        this.fornecedorFind = fornecedorFind;
        this.alterCod = alterCod;
    }

    //puxa o fornecedor pelo Id dele
    public Fornecedor findById(Long id) {
        return fornecedorFind.findFornecedorById(id);
    }

    //puxa o fornecedor pelo Id dele, seta ele como DTO
    public FornecedorDTO findByIdDTO(Long id) {
        return fornecedorFind.findFornecedorDTOById(id);
    }

    //puxa o fornecedor pelo Cnpj dele
    public FornecedorDTO findByCnpj(String cnpj) {
        return fornecedorFind.findByCnpj(cnpj);
    }

    //puxa o fornecedor pelo Cnpj dele, seta ele como optional
    public Optional<Fornecedor> findByCnpjOptional(String cnpj) {
        return fornecedorFind.findByCnpjOptional(cnpj);
    }

    //busca todos os fornecedores
    public List<Fornecedor> findAll() {
        return fornecedorFind.findAll();
    }

    //valida as informacoes
    private void validate(FornecedorDTO fornecedorDTO) {
        fornecedorValidate.validate(fornecedorDTO);
    }

    private Fornecedor setFornecedor(FornecedorDTO fornecedorDTO) {

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazao(fornecedorDTO.getRazao());
        fornecedor.setCnpj(fornecedorDTO.getCnpj());
        fornecedor.setNomefan(fornecedorDTO.getNomeFan());
        fornecedor.setEndereco(fornecedorDTO.getEndereco());
        fornecedor.setTelefone(fornecedorDTO.getTelefone());
        fornecedor.setEmail(fornecedorDTO.getEmail());

        return fornecedor;
    }

    private Fornecedor setFornecedor(FornecedorDTO fornecedorDTO, Fornecedor fornecedor) {

        fornecedor.setRazao(fornecedorDTO.getRazao());
        fornecedor.setCnpj(fornecedorDTO.getCnpj());
        fornecedor.setNomefan(fornecedorDTO.getNomeFan());
        fornecedor.setEndereco(fornecedorDTO.getEndereco());
        fornecedor.setTelefone(fornecedorDTO.getTelefone());
        fornecedor.setEmail(fornecedorDTO.getEmail());

        return fornecedor;
    }

    public String getCnpjMask(String cnpj) throws ParseException {
        MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
        mask.setValueContainsLiteralCharacters(false);
        return mask.valueToString(cnpj);
    }

    //salva o fornecedor no Database
    public FornecedorDTO save(FornecedorDTO fornecedorDTO) {

        this.validate(fornecedorDTO);

        LOGGER.info("Salvando br.com.hbsis.fornecedor");
        LOGGER.debug("br.com.hbsis.fornecedor: {}", fornecedorDTO);

        Fornecedor fornecedor = this.setFornecedor(fornecedorDTO);
        fornecedor = this.iFornecedorRepository.save(fornecedor);

        return FornecedorDTO.of(fornecedor);
    }

    // Altera as informacoes do banco
    public FornecedorDTO update(FornecedorDTO fonecedoresDTO, Long id) {

        this.validate(fonecedoresDTO);

        Fornecedor findfornecedor = this.findById(id);

        LOGGER.info("Atualizando Fornecedor... id: [{}]", findfornecedor.getId());
        LOGGER.debug("Payload: {}", fonecedoresDTO);
        LOGGER.debug("Fornecedor Existente: {}", findfornecedor);

        Fornecedor fornecedor = this.setFornecedor(fonecedoresDTO, findfornecedor);

        alterCod.alterCODCat(fornecedor);

        fornecedor = this.iFornecedorRepository.save(fornecedor);

        return FornecedorDTO.of(fornecedor);

    }

    //delete o fornecedor do Database
    public void delete(Long id) {
        LOGGER.info("Executando delete para Fornecedor de ID: [{}]", id);
        this.iFornecedorRepository.deleteById(id);
    }
}