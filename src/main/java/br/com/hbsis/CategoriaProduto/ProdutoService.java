package br.com.hbsis.CategoriaProduto;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final IProdutoRepository iProdutoRepository;

    @Autowired
    public ProdutoService(IProdutoRepository iProdutoRepository){
        this.iProdutoRepository = iProdutoRepository;
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO){
        this.validate(produtoDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Produto");
        LOGGER.debug("br.com.hbsis.Produto: {}", produtoDTO);

        Produto produto = new Produto();

        produto.setNomeProduto(produtoDTO.getNomeProduto());
        produto.setCodProduto(produtoDTO.getCodProduto());

        produto = this.iProdutoRepository.save(produto);

        return produtoDTO.of(produto);
    }

    private void validate(ProdutoDTO ProdutoDTO) {
        LOGGER.info("Validando Produto");

        if (ProdutoDTO == null) {
            throw new IllegalArgumentException("ProdutoDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(ProdutoDTO.getNomeProduto())) {
            throw new IllegalArgumentException("produto não deve ser nula/vazia");
        }
        if(StringUtils.isEmpty(ProdutoDTO.getCodProduto())) {
            throw new IllegalArgumentException("Cod não deve ser nula/vazia");
        }

    }
    public ProdutoDTO findById(Long id) {
        Optional<Produto> ProdutoOpcional = this.iProdutoRepository.findById(id);

        if (ProdutoOpcional.isPresent()) {
            return ProdutoDTO.of(ProdutoOpcional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public ProdutoDTO update(ProdutoDTO fonecedoresDTO, Long id) {
        Optional<Produto> ProdutoExistencialOpcional = this.iProdutoRepository.findById(id);

        if (ProdutoExistencialOpcional.isPresent()) {
            Produto ProdutoExistente = ProdutoExistencialOpcional.get();

            LOGGER.info("Atualizando Fornecedor... id: [{}]", ProdutoExistente.getId());
            LOGGER.debug("Payload: {}", fonecedoresDTO);
            LOGGER.debug("Fornecedor Existente: {}", ProdutoExistente);

            ProdutoExistente.setNomeProduto(fonecedoresDTO.getNomeProduto());


            ProdutoExistente = this.iProdutoRepository.save(ProdutoExistente);

            return ProdutoDTO.of(ProdutoExistente);
        }


        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public void delete(Long id) {
        LOGGER.info("Executando delete para Fornecedor de ID: [{}]", id);

        this.iProdutoRepository.deleteById(id);
    }

}
