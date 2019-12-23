package br.com.hbsis.pedido;

import br.com.hbsis.api.Invoice.Input.InvoiceDTO;
import br.com.hbsis.api.Invoice.Input.InvoiceItemDTO;
import br.com.hbsis.api.Invoice.InvoiceService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.FornecedoresDTO;
import br.com.hbsis.funcionario.Funcionario;
import br.com.hbsis.funcionario.FuncionarioService;
import br.com.hbsis.produtos.ProdutoService;
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
    private final ProdutoService produtoService;
    private final VendasService vendasService;
    private final FuncionarioService funcionarioService;

    public PedidoService(IPedidoRepository iPedidoRepository, FornecedorService fornecedorService, ProdutoService produtoService, VendasService vendasService, FuncionarioService funcionarioService) {
        this.iPedidoRepository = iPedidoRepository;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
        this.vendasService = vendasService;
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
            return PedidoDTO.of(fornecedorOptional.get(), null);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //salva o fornecedor no Database
    public PedidoDTO save(PedidoDTO pedidoDTO) {

        this.validate(pedidoDTO);
        if(vendasService.validaCompra(LocalDateTime.now(),pedidoDTO.getIdFornecedorPedido())){
            throw new IllegalArgumentException("Nao esta no periodo de venda");
        }

        LOGGER.info("Salvando br.com.hbsis.fornecedor");
        LOGGER.debug("br.com.hbsis.fornecedor: {}", pedidoDTO);

        Pedido pedido = new Pedido();
        pedido.setCodPedido(pedidoDTO.getCodPedido());
        pedido.setStatus(pedidoDTO.getStatus());
        pedido.setFornecedorPedido(fornecedorService.findByFornecedorId(pedidoDTO.getId()));
        pedido.setProdutosPedido(produtoService.findByIdProduto(pedidoDTO.getIdProdutosPedido()));
        pedido.setDia(LocalDateTime.now());
        pedido.setAmount(pedidoDTO.getAmount());

        String nome = produtoService.findByIdProduto(pedidoDTO.getIdProdutosPedido()).getNomeProduto();
        Funcionario funcionario = funcionarioService.findByIdFuncionario(pedidoDTO.getIdFuncionario());
        InvoiceItemDTO invoiceItemDTO = InvoiceItemDTO.of(pedidoDTO,nome);
        InvoiceDTO invoiceDTO = InvoiceDTO.of(pedido,invoiceItemDTO,funcionario);
        InvoiceService.HBInvoice(invoiceDTO);

        pedido = this.iPedidoRepository.save(pedido);

        //Retorna para o postman
        return PedidoDTO.of(pedido, null);
    }

    private void validate(PedidoDTO pedidoDTO) {
        LOGGER.info("Validando Fornecedor");

        if (StringUtils.isEmpty(pedidoDTO.getCodPedido())) {
            throw new IllegalArgumentException("Razao não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(pedidoDTO.getStatus())) {
            throw new IllegalArgumentException("Razao não deve ser nulo/vazio");
        }
        if (pedidoDTO.getIdFornecedorPedido() == null) {
            throw new IllegalArgumentException("Razao não deve ser nulo/vazio");
        }
        if (pedidoDTO.getIdProdutosPedido() == null) {
            throw new IllegalArgumentException("Razao não deve ser nulo/vazio");
        }
        if (pedidoDTO.getAmount() == null) {
            throw new IllegalArgumentException("Amount não deve ser nulo/vazio");
        }
        Long idForneValido = produtoService.findByIdProduto(pedidoDTO.getIdProdutosPedido()).getLinhas().getCategoria().getFornecedor().getId();
        if (pedidoDTO.getIdFornecedorPedido() == idForneValido) {
            throw new IllegalArgumentException("Esse fornecedor nao é dono desse produto");
        }
    }

    // Altera as informacoes do banco
    public PedidoDTO update(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> produtoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validate(pedidoDTO);

        if(vendasService.validaCompra(LocalDateTime.now(),pedidoDTO.getIdFornecedorPedido())){
            throw new IllegalArgumentException("Nao esta no periodo de venda");
        }

        if (produtoExistenteOptional.isPresent()) {
            Pedido pedido = produtoExistenteOptional.get();

            LOGGER.info("Atualizando Pedido... id: [{}]", pedido.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido Existente: {}", pedido);

            pedido.setAmount(pedidoDTO.getAmount());
            pedido.setFornecedorPedido(fornecedorService.findByFornecedorId(pedidoDTO.getId()));
            pedido.setProdutosPedido(produtoService.findByIdProduto(pedidoDTO.getIdProdutosPedido()));
            pedido.setCodPedido(pedidoDTO.getCodPedido());

            pedido = this.iPedidoRepository.save(pedido);

            return PedidoDTO.of(pedido, null);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    // Altera as informacoes do banco
    public PedidoDTO cancela(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> produtoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validate(pedidoDTO);
        if(vendasService.validaCompra(LocalDateTime.now(),pedidoDTO.getIdFornecedorPedido())){
            throw new IllegalArgumentException("Nao esta no periodo de venda");
        }
        if (produtoExistenteOptional.isPresent()) {
            Pedido pedido = produtoExistenteOptional.get();

            LOGGER.info("Cancelando Pedido... id: [{}]", pedido.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido Existente: {}", pedido);

            pedido.setStatus("Cancelado");

            pedido = this.iPedidoRepository.save(pedido);

            return PedidoDTO.of(pedido, null);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    // Altera as informacoes do banco
    public PedidoDTO retirado(PedidoDTO pedidoDTO, Long id) {
        Optional<Pedido> produtoExistenteOptional = this.iPedidoRepository.findById(id);
        this.validate(pedidoDTO);
        if(vendasService.validaRetirada(LocalDateTime.now(),pedidoDTO.getIdFornecedorPedido())){
            throw new IllegalArgumentException("Nao pode retirar o pedido hoje");
        }
        if (produtoExistenteOptional.isPresent()) {
            Pedido pedido = produtoExistenteOptional.get();

            LOGGER.info("Retirando Pedido... id: [{}]", pedido.getId());
            LOGGER.debug("Payload: {}", pedidoDTO);
            LOGGER.debug("Pedido Existente: {}", pedido);

            pedido.setStatus("Retirado");

            pedido = this.iPedidoRepository.save(pedido);

            return PedidoDTO.of(pedido, null);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //delete o Pedido do Database
    public void delete(Long id) {
        LOGGER.info("Executando delete para Pedido de ID: [{}]", id);

        this.iPedidoRepository.deleteById(id);
    }
}
