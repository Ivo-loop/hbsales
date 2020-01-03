package br.com.hbsis.pedido;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PedidoBusca {
    private final IPedidoRepository iPedidoRepository;

    public PedidoBusca(IPedidoRepository iPedidoRepository) {
        this.iPedidoRepository = iPedidoRepository;
    }

    //puxa o fornecedor pelo Id dele, seta ele como DTO
    public Pedido findByIdPedido(Long id) {
        Optional<Pedido> fornecedorOptional = this.iPedidoRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }
}
