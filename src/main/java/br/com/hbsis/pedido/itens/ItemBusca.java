package br.com.hbsis.pedido.itens;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemBusca {
    private final ItensRepository itensRepository;

    public ItemBusca(ItensRepository itensRepository) {
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
