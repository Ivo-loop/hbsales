package br.com.hbsis.fornecedor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class FornecedorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);
    private final IFornecedoresRepository iFonecedoresRepository;

    @Autowired
    public FornecedorService(IFornecedoresRepository iFonecedoresRepository) {
        this.iFonecedoresRepository = iFonecedoresRepository;
    }

    public FonecedoresDTO save(FonecedoresDTO FonecedoresDTO) {

        this.validate(FonecedoresDTO);

        LOGGER.info("Salvando br.com.hbsis.fornecedor");
        LOGGER.debug("br.com.hbsis.fornecedor: {}", FonecedoresDTO);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazao(FonecedoresDTO.getRazao());
        fornecedor.setCnpj(FonecedoresDTO.getCnpj());
        fornecedor.setNomefan(FonecedoresDTO.getNomeFan());
        fornecedor.setEndereco(FonecedoresDTO.getEndereco());
        fornecedor.setTelefone(FonecedoresDTO.getTelefone());
        fornecedor.setEmail(FonecedoresDTO.getEmail());

        fornecedor = this.iFonecedoresRepository.save(fornecedor);

        //Retorna para o postman
        return FonecedoresDTO.of(fornecedor);
    }

    private void validate(FonecedoresDTO fonecedoresDTO) {
        LOGGER.info("Validando Fornecedor");

        if (fonecedoresDTO == null) {
            throw new IllegalArgumentException("fonecedoresDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(fonecedoresDTO.getCnpj())) {
            throw new IllegalArgumentException("Cnpj não deve ser nula/vazia");
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
    public FonecedoresDTO findById(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return FonecedoresDTO.of(fornecedorOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Fornecedor findByFornecedorId(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
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
            fornecedorExistente.setEndereco(fonecedoresDTO.getEndereco());

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