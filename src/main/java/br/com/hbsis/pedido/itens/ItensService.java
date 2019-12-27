package br.com.hbsis.pedido.itens;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.pedido.PedidoService;
import br.com.hbsis.produtos.ProdutoService;
import br.com.hbsis.produtos.Produtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItensService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItensService.class);
    private final ItensRepository itensRepository;
    private final PedidoService pedidoService;
    private final ProdutoService produtoService;

    public ItensService(ItensRepository itensRepository, PedidoService pedidoService, ProdutoService produtoService) {
        this.itensRepository = itensRepository;
        this.pedidoService = pedidoService;
        this.produtoService = produtoService;
    }

    //busca tudo
    public List<Itens> findAll() {
        return itensRepository.findAll();
    }

    //puxa o fornecedor pelo Id dele, seta ele como DTO
    public ItensDTO findById(Long id) {
        Optional<Itens> fornecedorOptional = this.itensRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return ItensDTO.of(fornecedorOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<Itens> findByIdspedido(Long id) {
        List<Itens> fornecedorOptional = this.itensRepository.findAllByPedido_IdIs(id);

        if (!fornecedorOptional.isEmpty()) {
            return fornecedorOptional;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Itens findByIdpedido(Long id) {
        Optional<Itens> fornecedorOptional = this.itensRepository.findByPedido_IdIs(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Long soma(Long idPedido, String idProduto) {
        Long total = 0L;
        for (Itens itens : this.findByIdspedido(idPedido)) {
            if (itens.getProdutos().getNomeProduto().equals(idProduto)) {
                total += itens.getAmount();
            }
        }
        return total;
    }

    //salva o Itens no Database
    public ItensDTO save(ItensDTO itensDTO) {

        this.validate(itensDTO);

        LOGGER.info("Salvando br.com.hbsis.Item");
        LOGGER.debug("br.com.hbsis.Item: {}", itensDTO);

        Itens itens = new Itens();
        itens.setPedido(pedidoService.findByIdPedido(itensDTO.getIdPedido()));
        itens.setProdutos(produtoService.findByIdProduto(itensDTO.getIdProduto()));
        itens.setAmount(itensDTO.getAmount());

        itens = this.itensRepository.save(itens);

        //Retorna para o postman
        return ItensDTO.of(itens);
    }

    //valida as informacoes
    private void validate(ItensDTO itensDTO) {
        LOGGER.info("Validando Item");
        Produtos produtos = produtoService.findByIdProduto(itensDTO.getIdProduto());
        Pedido pedido = pedidoService.findByIdPedido(itensDTO.getIdPedido());

        if (itensDTO == null) {
            throw new IllegalArgumentException("fonecedoresDTO não deve ser nulo");
        }

        if (itensDTO.getAmount() == null) {
            throw new IllegalArgumentException("Amount não deve ser nula/vazia");
        }
        if (itensDTO.getIdPedido() == null) {
            throw new IllegalArgumentException("IdPedido não deve ser nula/vazia");
        }
        if (itensDTO.getIdProduto() == null) {
            throw new IllegalArgumentException("IdProduto não deve ser nula/vazia");
        }
        if (!pedido.getFornecedor().getCnpj().equals(produtos.getLinhas().getCategoria().getFornecedor().getCnpj())) {
            throw new IllegalArgumentException("este produto não é desse fornecedor");
        }
    }

    // Altera as informacoes do banco
    public ItensDTO update(ItensDTO itensDTO, Long id) {
        Optional<Itens> itemExistenteOptional = this.itensRepository.findById(id);
        this.validate(itensDTO);
        if (itemExistenteOptional.isPresent()) {
            Itens itens = itemExistenteOptional.get();

            LOGGER.info("Atualizando Item... id: [{}]", itensDTO);
            LOGGER.debug("Payload: {}", itensDTO);
            LOGGER.debug("Item Existente: {}", itens);

            itens.setPedido(pedidoService.findByIdPedido(itensDTO.getIdPedido()));
            itens.setProdutos(produtoService.findByIdProduto(itensDTO.getIdProduto()));
            itens.setAmount(itensDTO.getAmount());

            itens = this.itensRepository.save(itens);

            return ItensDTO.of(itens);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //delete o Item do Database
    public void delete(Long id) {
        LOGGER.info("Executando delete para Fornecedor de ID: [{}]", id);

        this.itensRepository.deleteById(id);
    }
}
