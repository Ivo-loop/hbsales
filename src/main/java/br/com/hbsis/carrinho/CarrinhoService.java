package br.com.hbsis.carrinho;

import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.vendas.VendasService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CarrinhoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarrinhoService.class);
    private final ICarrinhoRepository iCarrinhoRepository;
    private final FornecedorService fornecedorService;
    private final FuncionarioService funcionarioService;
    private final VendasService vendasService;

    public CarrinhoService(ICarrinhoRepository iCarrinhoRepository, FornecedorService fornecedorService, FuncionarioService funcionarioService, VendasService vendasService) {
        this.iCarrinhoRepository = iCarrinhoRepository;
        this.fornecedorService = fornecedorService;
        this.funcionarioService = funcionarioService;
        this.vendasService = vendasService;
    }

    public Carrinho findbyId(Long id) {
        Optional<Carrinho> carrinhoOptional = iCarrinhoRepository.findById(id);
        if (carrinhoOptional.isPresent()) {
            return carrinhoOptional.get();
        }
        throw new IllegalArgumentException(String.format("IdCarrinho %s não existe", id));
    }

    CarrinhoDTO findbyIdDTO(Long id) {
        Optional<Carrinho> carrinhoOptional = iCarrinhoRepository.findById(id);
        if (carrinhoOptional.isPresent()) {
            return CarrinhoDTO.of(carrinhoOptional.get());
        }
        throw new IllegalArgumentException(String.format("IdCarrinho %s não existe", id));
    }

    //salva o fornecedor no Database
    CarrinhoDTO criarPedido(CarrinhoDTO carrinhoDTO) {

        this.validate(carrinhoDTO);

        LOGGER.info("Criando br.com.hbsis.Pedido");
        LOGGER.debug("br.com.hbsis.Pedido: {}", carrinhoDTO);

        Carrinho carrinho = new Carrinho();
        carrinho.setCodPedido(carrinhoDTO.getCodPedido());
        carrinho.setFornecedor(fornecedorService.findById(carrinhoDTO.getIdFornecedor()));
        carrinho.setFuncionario(funcionarioService.findByIdFuncionario(carrinhoDTO.getIdFuncionario()));

        carrinho = this.iCarrinhoRepository.save(carrinho);

        //Retorna para o postman
        return CarrinhoDTO.of(carrinho);
    }

    private void validate(CarrinhoDTO carrinhoDTO) {
        LOGGER.info("Validando Carrinho");

        if (StringUtils.isEmpty(carrinhoDTO.getCodPedido())) {
            throw new IllegalArgumentException("Cod não deve ser nulo/vazio");
        }
        if (carrinhoDTO.getIdFornecedor() == null) {
            throw new IllegalArgumentException("Fornecedor não deve ser nulo/vazio");
        }
        if (carrinhoDTO.getIdFuncionario() == null) {
            throw new IllegalArgumentException("Funcionario não deve ser nulo/vazio");
        }
    }

    // Altera as informacoes do banco
    public CarrinhoDTO update(CarrinhoDTO carrinhoDTO, Long id) {
        Optional<Carrinho> produtoExistenteOptional = this.iCarrinhoRepository.findById(id);
        this.validate(carrinhoDTO);

        if (vendasService.validaCompra(LocalDateTime.now(), carrinhoDTO.getIdFornecedor())) {
            throw new IllegalArgumentException("Nao esta no periodo de venda");
        }

        if (produtoExistenteOptional.isPresent()) {
            Carrinho pedido = produtoExistenteOptional.get();

            LOGGER.info("Atualizando Pedido... id: [{}]", pedido.getId());
            LOGGER.debug("Payload: {}", carrinhoDTO);
            LOGGER.debug("Pedido Existente: {}", pedido);

            pedido.setFornecedor(fornecedorService.findById(carrinhoDTO.getId()));
            pedido.setCodPedido(carrinhoDTO.getCodPedido());

            pedido = this.iCarrinhoRepository.save(pedido);

            return CarrinhoDTO.of(pedido);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //delete o Pedido do Database
    public void delete(Long id) {
        LOGGER.info("Executando delete para Pedido de ID: [{}]", id);

        this.iCarrinhoRepository.deleteById(id);
    }
}
