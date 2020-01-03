package br.com.hbsis.pedido.itens;

import br.com.hbsis.carrinho.ItensCarrinho.ItensCarrinho;
import br.com.hbsis.carrinho.ItensCarrinho.ItensCarrinhoService;
import br.com.hbsis.pedido.PedidoBusca;
import br.com.hbsis.produtos.ProdutoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItensTransforme {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemComponent.class);
    private final ItensRepository itensRepository;
    private final ItensCarrinhoService itensCarrinhoService;
    private final PedidoBusca pedidoBusca;
    private final ProdutoService produtoService;

    public ItensTransforme(ItensRepository itensRepository, ItensCarrinhoService itensCarrinhoService, PedidoBusca pedidoBusca, ProdutoService produtoService) {
        this.itensRepository = itensRepository;
        this.itensCarrinhoService = itensCarrinhoService;
        this.pedidoBusca = pedidoBusca;
        this.produtoService = produtoService;
    }

    public void Itensproduto(Long idPedido, Long idCarrinho) {

        List<ItensCarrinho> itensCarrinho = itensCarrinhoService.findByIdsCarrinho(idCarrinho);

        for (ItensCarrinho itenscar : itensCarrinho) {
            ItensDTO itensDTO = new ItensDTO(
                    null,
                    itenscar.getProdutos().getId(),
                    idPedido,
                    itenscar.getAmount()
            );

            LOGGER.info("Salvando br.com.hbsis.Item");
            LOGGER.debug("br.com.hbsis.Item: {}", itensDTO);

            Itens item = new Itens();
            item.setPedido(pedidoBusca.findByIdPedido(idPedido));
            item.setProdutos(produtoService.findByIdProduto(itensDTO.getIdProduto()));
            item.setAmount(itensDTO.getAmount());

            itensRepository.save(item);
        }
    }
}
