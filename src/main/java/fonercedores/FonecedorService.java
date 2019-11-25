package fonercedores;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class FonecedorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FonecedorService.class);
    private final IFonecedoresRepository iFonecedoresRepository;

    @Autowired
    public FonecedorService(IFonecedoresRepository iFonecedoresRepository) {
        this.iFonecedoresRepository = iFonecedoresRepository;
    }
//    public void save(FonecedoresDTO fonecedoresDTO){
//        Fornecedor fornecedor = new Fornecedor(
//        fonecedoresDTO.getId(),
//        fonecedoresDTO.getRazao(),
//        fonecedoresDTO.getCnpj(),
//        fonecedoresDTO.getNomeFan(),
//        fonecedoresDTO.getEndereço(),
//        fonecedoresDTO.getTelefone(),
//        fonecedoresDTO.getEmail()
//        );
//        this.iFonecedoresRepository.save(fornecedor);
//    }

    public FonecedoresDTO save(FonecedoresDTO FonecedoresDTO) {

        this.validate(FonecedoresDTO);

        LOGGER.info("Salvando fornecedor");
        LOGGER.debug("fornecedor: {}", FonecedoresDTO);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(FonecedoresDTO.getId());
        fornecedor.setRazao(FonecedoresDTO.getRazao());
        fornecedor.setCnpj(FonecedoresDTO.getCnpj());
        fornecedor.setNomefan(FonecedoresDTO.getNomeFan());
        fornecedor.setEndereco(FonecedoresDTO.getEndereço());
        fornecedor.setTelefone(FonecedoresDTO.getTelefone());
        fornecedor.setEmail(FonecedoresDTO.getEmail());

        fornecedor = this.iFonecedoresRepository.save(fornecedor);

        return FonecedoresDTO.of(fornecedor);
    }

    private void validate(FonecedoresDTO fonecedoresDTO) {
        LOGGER.info("Validando Fornecedor");

        if (fonecedoresDTO == null) {
            throw new IllegalArgumentException("fonecedoresDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(fonecedoresDTO.getCnpj())) {
            throw new IllegalArgumentException("Senha não deve ser nula/vazia");
        }

        if (StringUtils.isEmpty(fonecedoresDTO.getRazao())) {
            throw new IllegalArgumentException("Login não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fonecedoresDTO.getEmail())) {
            throw new IllegalArgumentException("Senha não deve ser nula/vazia");
        }

        if (StringUtils.isEmpty(fonecedoresDTO.getEndereço())) {
            throw new IllegalArgumentException("Login não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fonecedoresDTO.getNomeFan())) {
            throw new IllegalArgumentException("Senha não deve ser nula/vazia");
        }

        if (StringUtils.isEmpty(fonecedoresDTO.getTelefone())) {
            throw new IllegalArgumentException("Login não deve ser nulo/vazio");
        }

    }
    public FonecedoresDTO findById(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return FonecedoresDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public FonecedoresDTO update(FonecedoresDTO fonecedoresDTO, Long id) {
        Optional<Fornecedor> fornecedorExistenteOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorExistenteOptional.isPresent()) {
            Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();

            LOGGER.info("Atualizando Fornecedor... id: [{}]", fornecedorExistente.getId());
            LOGGER.debug("Payload: {}", fonecedoresDTO);
            LOGGER.debug("Fornecedor Existente: {}", fornecedorExistente);

            fornecedorExistente.setCnpj(fonecedoresDTO.getCnpj());
            fornecedorExistente.setEmail(fonecedoresDTO.getEmail());
            fornecedorExistente.setTelefone(fonecedoresDTO.getTelefone());
            fornecedorExistente.setRazao(fonecedoresDTO.getRazao());
            fornecedorExistente.setNomefan(fonecedoresDTO.getNomeFan());
            fornecedorExistente.setEndereco(fonecedoresDTO.getEndereço());

            fornecedorExistente = this.iFonecedoresRepository.save(fornecedorExistente);

            return FonecedoresDTO.of(fornecedorExistente);
        }


        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para Fornecedor de ID: [{}]", id);

        this.iFonecedoresRepository.deleteById(id);
    }
}
