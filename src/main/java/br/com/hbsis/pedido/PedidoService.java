package br.com.hbsis.pedido;

import br.com.hbsis.api.PortaAPI;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.vendas.VendasService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoService.class);
    private final IPedidoRepository iPedidoRepository;
    private final FornecedorService fornecedorService;
    private final VendasService vendasService;
    private final PortaAPI portaAPI;
    private final FuncionarioService funcionarioService;

    public PedidoService(IPedidoRepository iPedidoRepository, FornecedorService fornecedorService, VendasService vendasService, PortaAPI portaAPI, FuncionarioService funcionarioService) {
        this.iPedidoRepository = iPedidoRepository;
        this.fornecedorService = fornecedorService;
        this.vendasService = vendasService;
        this.portaAPI = portaAPI;
        this.funcionarioService = funcionarioService;
    }

    //busca tudo
    public List<Pedido> findAll() {
        return iPedidoRepository.findAll();
    }

    //puxa o fornecedor pelo Id dele, seta ele como DTO
    public PedidoDTO findById(Long id) {
        Optional<Pedido> fornecedorOptional = this.iPedidoRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return PedidoDTO.of(fornecedorOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa o fornecedor pelo Id dele, seta ele como DTO
    public Pedido findByIdPedido(Long id) {
        Optional<Pedido> fornecedorOptional = this.iPedidoRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }



    //salva o fornecedor no Database
    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {

        this.validate(pedidoDTO);
        if (vendasService.validaCompra(LocalDateTime.now(), pedidoDTO.getIdFornecedor())) {
            throw new IllegalArgumentException("Nao esta no periodo de venda");
        }

        LOGGER.info("Criando br.com.hbsis.Pedido");
        LOGGER.debug("br.com.hbsis.Pedido: {}", pedidoDTO);

        Pedido pedido = new Pedido();
        pedido.setCodPedido(pedidoDTO.getCodPedido());
        pedido.setStatus("Ativo");
        pedido.setFornecedor(fornecedorService.findByFornecedorId(pedidoDTO.getIdFornecedor()));
        pedido.setFuncionario(funcionarioService.findByIdFuncionario(pedidoDTO.getIdFuncionario()));
        pedido.setDia(LocalDateTime.now());

        pedido = this.iPedidoRepository.save(pedido);

        //Retorna para o postman
        return PedidoDTO.of(pedido);
    }

    //salva o fornecedor no Database
    public PedidoDTO save(Long id) {

        LOGGER.info("Salvando br.com.hbsis.Pedido");
        LOGGER.debug("br.com.hbsis.Pedido: {}", id);

        Pedido pedido = this.findByIdPedido(id);
        pedido.setStatus("Ativo");

        portaAPI.validaApi(pedido);

        pedido = this.iPedidoRepository.save(pedido);

        //Retorna para o postman
        return PedidoDTO.of(pedido);
    }

    private void validate(PedidoDTO pedidoDTO) {
        LOGGER.info("Validando Fornecedor");

        if (StringUtils.isEmpty(pedidoDTO.getCodPedido())) {
            throw new IllegalArgumentException("Cod não deve ser nulo/vazio");
        }
        if (pedidoDTO.getIdFornecedor() == null) {
            throw new IllegalArgumentException("Fornecedor não deve ser nulo/vazio");
        }
    }

    // Altera as informacoes do banco
    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> produtoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validate(pedidoDTO);

        if (vendasService.validaCompra(LocalDateTime.now(), pedidoDTO.getIdFornecedor())) {
            throw new IllegalArgumentException("Nao esta no periodo de venda");
        }

        if (produtoExistenteOptional.isPresent()) {
            Pedido pedido = produtoExistenteOptional.get();

            LOGGER.info("Atualizando Pedido... id: [{}]", pedido.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido Existente: {}", pedido);

            pedido.setFornecedor(fornecedorService.findByFornecedorId(pedidoDTO.getId()));
            pedido.setCodPedido(pedidoDTO.getCodPedido());

            pedido = this.iPedidoRepository.save(pedido);

            return PedidoDTO.of(pedido);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    // Altera as informacoes do banco
    public PedidoDTO cancela(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> produtoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validate(pedidoDTO);
        if (vendasService.validaCompra(LocalDateTime.now(), pedidoDTO.getIdFornecedor())) {
            throw new IllegalArgumentException("Nao esta no periodo de venda");
        }
        if (produtoExistenteOptional.isPresent()) {
            Pedido pedido = produtoExistenteOptional.get();

            LOGGER.info("Cancelando Pedido... id: [{}]", pedido.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido Existente: {}", pedido);

            pedido.setStatus("Cancelado");

            pedido = this.iPedidoRepository.save(pedido);

            return PedidoDTO.of(pedido);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    // Altera as informacoes do banco
    public PedidoDTO retirado(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> produtoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validate(pedidoDTO);
        if (vendasService.validaRetirada(LocalDateTime.now(), pedidoDTO.getIdFornecedor())) {
            throw new IllegalArgumentException("Nao pode retirar o pedido hoje");
        }
        if (produtoExistenteOptional.isPresent()) {
            Pedido pedido = produtoExistenteOptional.get();

            LOGGER.info("Retirando Pedido... id: [{}]", pedido.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido Existente: {}", pedido);

            pedido.setStatus("Retirado");

            pedido = this.iPedidoRepository.save(pedido);

            return PedidoDTO.of(pedido);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //delete o Pedido do Database
    public void delete(Long id) {
        LOGGER.info("Executando delete para Pedido de ID: [{}]", id);

        this.iPedidoRepository.deleteById(id);
    }
}
