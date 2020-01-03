package br.com.hbsis.carrinho.ItensCarrinho;

import br.com.hbsis.carrinho.Carrinho;
import br.com.hbsis.carrinho.CarrinhoService;
import br.com.hbsis.produtos.ProdutoService;
import br.com.hbsis.produtos.Produtos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItensCarrinhoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItensCarrinhoService.class);
    private final ItensCarRepository itensCarRepository;
    private final CarrinhoService carrinhoService;
    private final ProdutoService produtoService;

    public ItensCarrinhoService(ItensCarRepository itensCarRepository, CarrinhoService carrinhoService, ProdutoService produtoService) {
        this.itensCarRepository = itensCarRepository;
        this.carrinhoService = carrinhoService;
        this.produtoService = produtoService;
    }

    //busca tudo
    public List<ItensCarrinho> findAll() {
        return itensCarRepository.findAll();
    }

    //puxa o fornecedor pelo Id dele, seta ele como DTO
    public ItensCarrinhoDTO findById(Long id) {
        Optional<ItensCarrinho> fornecedorOptional = this.itensCarRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return ItensCarrinhoDTO.of(fornecedorOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public List<ItensCarrinho> findByIdsCarrinho(Long id) {
        List<ItensCarrinho> fornecedorOptional = this.itensCarRepository.findAllByCarrinho_IdIs(id);

        if (!fornecedorOptional.isEmpty()) {
            return fornecedorOptional;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public ItensCarrinho findByIdpedido(Long id) {
        Optional<ItensCarrinho> fornecedorOptional = this.itensCarRepository.findByCarrinho_IdIs(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public Long soma(Long idPedido, String idProduto) {
        Long total = 0L;
        for (ItensCarrinho itensCarrinho : this.findByIdsCarrinho(idPedido)) {
            if (itensCarrinho.getProdutos().getNomeProduto().equals(idProduto)) {
                total += itensCarrinho.getAmount();
            }
        }
        return total;
    }

    //salva o ItensCarrinho no Database
    public ItensCarrinhoDTO save(ItensCarrinhoDTO itensCarrinhoDTO) {

        this.validate(itensCarrinhoDTO);

        LOGGER.info("Salvando br.com.hbsis.Item");
        LOGGER.debug("br.com.hbsis.Item: {}", itensCarrinhoDTO);

        ItensCarrinho itensCarrinho = new ItensCarrinho();
        itensCarrinho.setCarrinho(carrinhoService.findbyId(itensCarrinhoDTO.getIdCarrinho()));
        itensCarrinho.setProdutos(produtoService.findByIdProduto(itensCarrinhoDTO.getIdProduto()));
        itensCarrinho.setAmount(itensCarrinhoDTO.getAmount());

        itensCarrinho = this.itensCarRepository.save(itensCarrinho);

        //Retorna para o postman
        return ItensCarrinhoDTO.of(itensCarrinho);
    }

    //valida as informacoes
    private void validate(ItensCarrinhoDTO itensCarrinhoDTO) {
        LOGGER.info("Validando Item");
        Produtos produtos = produtoService.findByIdProduto(itensCarrinhoDTO.getIdProduto());
        Carrinho pedido = carrinhoService.findbyId(itensCarrinhoDTO.getIdCarrinho());

        if (itensCarrinhoDTO == null) {
            throw new IllegalArgumentException("fonecedoresDTO não deve ser nulo");
        }

        if (itensCarrinhoDTO.getAmount() == null) {
            throw new IllegalArgumentException("Amount não deve ser nula/vazia");
        }
        if (itensCarrinhoDTO.getIdCarrinho() == null) {
            throw new IllegalArgumentException("IdPedido não deve ser nula/vazia");
        }
        if (itensCarrinhoDTO.getIdProduto() == null) {
            throw new IllegalArgumentException("IdProduto não deve ser nula/vazia");
        }
        if (!pedido.getFornecedor().getCnpj().equals(produtos.getLinhas().getCategoria().getFornecedor().getCnpj())) {
            throw new IllegalArgumentException("este produto não é desse fornecedor");
        }
    }

    // Altera as informacoes do banco
    public ItensCarrinhoDTO update(ItensCarrinhoDTO itensCarrinhoDTO, Long id) {
        Optional<ItensCarrinho> itemExistenteOptional = this.itensCarRepository.findById(id);
        this.validate(itensCarrinhoDTO);
        if (itemExistenteOptional.isPresent()) {
            ItensCarrinho itensCarrinho = itemExistenteOptional.get();

            LOGGER.info("Atualizando Item... id: [{}]", itensCarrinhoDTO);
            LOGGER.debug("Payload: {}", itensCarrinhoDTO);
            LOGGER.debug("Item Existente: {}", itensCarrinho);

            itensCarrinho.setCarrinho(carrinhoService.findbyId(itensCarrinhoDTO.getIdCarrinho()));
            itensCarrinho.setProdutos(produtoService.findByIdProduto(itensCarrinhoDTO.getIdProduto()));
            itensCarrinho.setAmount(itensCarrinhoDTO.getAmount());

            itensCarrinho = this.itensCarRepository.save(itensCarrinho);

            return ItensCarrinhoDTO.of(itensCarrinho);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //delete o Item do Database
    public void delete(Long id) {
        LOGGER.info("Executando delete para Fornecedor de ID: [{}]", id);

        this.itensCarRepository.deleteById(id);
    }
}
