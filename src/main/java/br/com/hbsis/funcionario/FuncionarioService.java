package br.com.hbsis.funcionario;

import br.com.hbsis.api.Employee.InAndOutPut.InputDTO;
import br.com.hbsis.api.Employee.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FuncionarioService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioService.class);
    private final IFuncionarioRepository iFuncionarioRepository;

    public FuncionarioService(IFuncionarioRepository iFuncionarioRepository) {
        this.iFuncionarioRepository = iFuncionarioRepository;
    }

    //puxa o Funcionario pelo Id dele, seta ele como DTO
    public FuncionarioDTO findById(Long id) {
        Optional<Funcionario> FuncionarioOptional = this.iFuncionarioRepository.findById(id);

        if (FuncionarioOptional.isPresent()) {
            return FuncionarioDTO.of(FuncionarioOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //salva o Funcionario no Database
    public FuncionarioDTO save(FuncionarioDTO funcionarioDTO) {

        this.validate(funcionarioDTO);

        FuncionarioDTO dto = EmployeeService.FuncionarioHBEmployee(InputDTO.of(funcionarioDTO));

        LOGGER.info("Salvando br.com.hbsis.Funcionario");
        LOGGER.debug("br.com.hbsis.funcionario: {}", funcionarioDTO);

        Funcionario funcionario = new Funcionario();
        funcionario.setNomeFuncionario(funcionarioDTO.getNomeFuncionario());
        funcionario.setEmail(funcionarioDTO.getEmail());
        funcionario.setUuid(dto.getUuid());

        funcionario = this.iFuncionarioRepository.save(funcionario);

        //Retorna para o postman
        return FuncionarioDTO.of(funcionario);
    }

    //valida as informacoes
    private void validate(FuncionarioDTO funcionarioDTO) {
        LOGGER.info("Validando Funcionario");

        if (funcionarioDTO == null) {
            throw new IllegalArgumentException("fonecedoresDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(funcionarioDTO.getNomeFuncionario())) {
            throw new IllegalArgumentException("Nome não deve ser nulo/vazio");
        }
        if (funcionarioDTO.getNomeFuncionario().length() > 50) {
            throw new IllegalArgumentException("Nome muito grande sinto muito");
        }
        if (StringUtils.isEmpty(funcionarioDTO.getEmail())) {
            throw new IllegalArgumentException("email não deve ser nula/vazia");
        }
        if (funcionarioDTO.getEmail().length() > 50) {
            throw new IllegalArgumentException("email muito grande sinto muito");
        }
        if (!funcionarioDTO.getEmail().contains("@hbsis.com.br")) {
            throw new IllegalArgumentException("email invalido sinto muito");
        }
        if (StringUtils.isEmpty(funcionarioDTO.getUuid())) {
            throw new IllegalArgumentException("uuid não deve ser nulo/vazio");
        }
        if (funcionarioDTO.getUuid().length() > 36) {
            throw new IllegalArgumentException("uuid muito grande sinto muito");
        }
    }

    // Altera as informacoes do banco
    public FuncionarioDTO update(FuncionarioDTO funcionarioDTO, Long id) {
        Optional<Funcionario> FuncionarioExistenteOptional = this.iFuncionarioRepository.findById(id);
        this.validate(funcionarioDTO);
        if (FuncionarioExistenteOptional.isPresent()) {
            Funcionario funcionario = FuncionarioExistenteOptional.get();
            validate(funcionarioDTO);

            LOGGER.info("Atualizando Funcionario... id: [{}]", funcionario.getId());
            LOGGER.debug("Payload: {}", funcionarioDTO);
            LOGGER.debug("Funcionario Existente: {}", funcionario);

            funcionario.setNomeFuncionario(funcionarioDTO.getNomeFuncionario());
            funcionario.setEmail(funcionarioDTO.getEmail());
            funcionario.setUuid(funcionarioDTO.getUuid());

            funcionario = this.iFuncionarioRepository.save(funcionario);

            return FuncionarioDTO.of(funcionario);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //delete o Funcionario do Database
    public void delete(Long id) {
        LOGGER.info("Executando delete para Funcionario de ID: [{}]", id);

        this.iFuncionarioRepository.deleteById(id);
    }
}
