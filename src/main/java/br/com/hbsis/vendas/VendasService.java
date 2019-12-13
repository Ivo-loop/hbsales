package br.com.hbsis.vendas;

import br.com.hbsis.fornecedor.FornecedorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VendasService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VendasService.class);
    private final IVendasRepository iVendasRepository;
    private final FornecedorService fornecedorService;

    public VendasService(IVendasRepository iVendasRepository, FornecedorService fornecedorService) {
        this.iVendasRepository = iVendasRepository;
        this.fornecedorService = fornecedorService;
    }

    //busca tudo
    public List<Vendas> findAll() {
        return iVendasRepository.findAll();
    }

    //busca produto pro Id
    public VendasDTO findById(Long id) {
        Optional<Vendas> vendasOptional = this.iVendasRepository.findById(id);

        if (vendasOptional.isPresent()) {
            return VendasDTO.of(vendasOptional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //salva
    public VendasDTO save(VendasDTO vendasDTO) {
        this.validate(vendasDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Produto");
        LOGGER.debug("br.com.hbsis.Produto: {}", vendasDTO);

        Vendas vendas = new Vendas();

        vendas.setDescricaoVendas(vendasDTO.getDescricaoVendas());
        vendas.setDiaInicial(vendasDTO.getDiaInicial());
        vendas.setDiaFinal(vendasDTO.getDiaFinal());
        vendas.setDiaRetirada(vendasDTO.getDiaRetirada());
        Long id = vendasDTO.getIdFornecedor();
        vendas.setFornecedorVenda(fornecedorService.findByFornecedorId(id));

        for(Vendas vendasValidar : iVendasRepository.findAll()){
            if(vendas.getDiaInicial().isBefore(vendasValidar.getDiaFinal())&& vendas.getDiaInicial().isAfter(vendasValidar.getDiaInicial())||
                    vendas.getDiaInicial().isEqual(vendasValidar.getDiaFinal())){
                throw new IllegalArgumentException(" data inicial invalida");
            }
            if(vendas.getDiaFinal().isAfter(vendasValidar.getDiaInicial()) && vendas.getDiaFinal().isBefore(vendasValidar.getDiaFinal()) ||
                    vendas.getDiaFinal().isEqual(vendasValidar.getDiaInicial())){
                throw new IllegalArgumentException(" data final invalida");
            }
        }

        //Retorna para o postman
        Vendas save = this.iVendasRepository.save(vendas);
        return VendasDTO.of(save);
    }

    //Alterar
    public VendasDTO update(VendasDTO vendasDTO, Long id) {
        Optional<Vendas> ProdutoExistencialOpcional = this.iVendasRepository.findById(id);

        if (ProdutoExistencialOpcional.isPresent()) {
            Vendas vendas = ProdutoExistencialOpcional.get();

            if(vendas.getDiaFinal().isBefore(LocalDateTime.now())){
                throw new IllegalArgumentException("Desculpa nao polde ser alterado");
            }

            LOGGER.info("Atualizando Produto... id: [{}]", vendas.getId());
            LOGGER.debug("Payload: {}", vendasDTO);
            LOGGER.debug("Produto Existente: {}", vendas);

            vendas.setDescricaoVendas(vendasDTO.getDescricaoVendas());
            vendas.setDiaInicial(vendasDTO.getDiaInicial());
            vendas.setDiaFinal(vendasDTO.getDiaFinal());
            vendas.setDiaRetirada(vendasDTO.getDiaRetirada());
            Long cod = vendasDTO.getIdFornecedor();
            vendas.setFornecedorVenda(fornecedorService.findByFornecedorId(cod));

            vendas = this.iVendasRepository.save(vendas);

            return VendasDTO.of(vendas);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //valida
    private void validate(VendasDTO vendasDTO) {
        LOGGER.info("Validando Produto");

        Long validador = Long.parseLong(String.valueOf(LocalDateTime.now()).substring(0, 10).replaceAll("-", ""));

        Long validarInicial = Long.parseLong(String.valueOf(vendasDTO.getDiaInicial()).substring(0, 10).replaceAll("-", ""));
        Long validarFinal = Long.parseLong(String.valueOf(vendasDTO.getDiaFinal()).substring(0, 10).replaceAll("-", ""));
        Long validarRetirada = Long.parseLong(String.valueOf(vendasDTO.getDiaRetirada()).substring(0, 10).replaceAll("-", ""));

        if (validador > validarInicial) {
            throw new IllegalArgumentException("tem algo de errado nas data1 meu bom");
        }if( validarInicial >= validarFinal){
            throw new IllegalArgumentException("tem algo de errado nas data2 meu bom");
        }if( validarFinal > validarRetirada){
            throw new IllegalArgumentException("tem algo de errado nas data3 meu bom");
        }
        if (vendasDTO == null) {
            throw new IllegalArgumentException("ProdutoDTO não deve ser nulo");
        }
        if (vendasDTO.getDescricaoVendas() == null) {
            throw new IllegalArgumentException("Nome da Produto não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(vendasDTO.getDiaInicial()))) {
            throw new IllegalArgumentException("Cod linhas não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(vendasDTO.getDiaFinal()))) {
            throw new IllegalArgumentException("Cod peso por unidade não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(vendasDTO.getDiaRetirada()))) {
            throw new IllegalArgumentException("Cod peso por unidade não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(vendasDTO.getIdFornecedor()))) {
            throw new IllegalArgumentException("Nome da Produto não deve ser nula/vazia");
        }
        //if()
    }

    //deleta
    public void delete(Long id) {
        LOGGER.info("Executando delete para Produto de ID: [{}]", id);

        this.iVendasRepository.deleteById(id);

    }
}
