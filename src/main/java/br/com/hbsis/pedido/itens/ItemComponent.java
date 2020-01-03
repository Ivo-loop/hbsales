package br.com.hbsis.pedido.itens;


import br.com.hbsis.carrinho.ItensCarrinho.ItensCarrinho;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemComponent.class);
    private final ItensRepository itensRepository;

    public ItemComponent(ItensRepository itensRepository) {
        this.itensRepository = itensRepository;
    }

    //Busca
    public List findAllByPedido_IdIs(Long id) {
        List<Itens> ItemOpcional = this.itensRepository.findAllByPedido_IdIs(id);
        if (!ItemOpcional.isEmpty()) {
            return ItemOpcional;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
}
