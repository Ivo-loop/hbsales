package br.com.hbsis.vendas;

import br.com.hbsis.fornecedor.FornecedorService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

        vendas.setCodVendas(vendasDTO.getCodVendas());
        vendas.setDiaInicial(vendasDTO.getDiaInicial());
        vendas.setDiaFinal(vendasDTO.getDiaFinal());
        vendas.setDiaRetirada(vendasDTO.getDiaRetirada());
        Long id = vendasDTO.getIdFornecedor();
        vendas.setFornecedorVenda(fornecedorService.findByFornecedorId(id));

        //Retorna para o postman
        Vendas save = this.iVendasRepository.save(vendas);
        return VendasDTO.of(save);
    }


    //Alterar
    public VendasDTO update(VendasDTO vendasDTO, Long id) {
        Optional<Vendas> ProdutoExistencialOpcional = this.iVendasRepository.findById(id);

        if (ProdutoExistencialOpcional.isPresent()) {
            Vendas vendas = ProdutoExistencialOpcional.get();

            LOGGER.info("Atualizando Produto... id: [{}]", vendas.getId());
            LOGGER.debug("Payload: {}", vendasDTO);
            LOGGER.debug("Produto Existente: {}", vendas);

            vendas.setCodVendas(vendasDTO.getCodVendas());
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

        if (vendasDTO == null) {
            throw new IllegalArgumentException("ProdutoDTO não deve ser nulo");
        }
        if (vendasDTO.getCodVendas() == null) {
            throw new IllegalArgumentException("Nome da Produto não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(vendasDTO.getDiaInicial()))) {
            throw new IllegalArgumentException("Cod linhas não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(vendasDTO.getDiaInicial()))) {
            throw new IllegalArgumentException("Cod peso por unidade não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(vendasDTO.getDiaInicial()))) {
            throw new IllegalArgumentException("Cod peso por unidade não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(String.valueOf(vendasDTO.getIdFornecedor()))) {
            throw new IllegalArgumentException("Nome da Produto não deve ser nula/vazia");
        }
    }

    //deleta
    public void delete(Long id) {
        LOGGER.info("Executando delete para Produto de ID: [{}]", id);

        this.iVendasRepository.deleteById(id);

    }

    //lista
    public List<Vendas> listarVendas() {
        List<Vendas> vendas;
        vendas = this.iVendasRepository.findAll();
        return vendas;
    }
}
