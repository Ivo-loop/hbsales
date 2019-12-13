package br.com.hbsis.fornecedor;

import br.com.hbsis.categorias.Categoria;
import br.com.hbsis.categorias.CategoriaDTO;
import br.com.hbsis.categorias.CategoriaService;
import br.com.hbsis.categorias.ICategoriaRepository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public class FornecedorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FornecedorService.class);
    private final IFornecedoresRepository iFonecedoresRepository;
    private final CategoriaService categoriaService;
    private final ICategoriaRepository iCategoriaRepository;

    /*- falar com kbral bug de loop
     * ele so acontece se o fornecedor da categoria mude, entao nao deve ser utilizado no categoria
     */
    @Autowired
    public FornecedorService(IFornecedoresRepository iFonecedoresRepository, @Lazy CategoriaService categoriaService, ICategoriaRepository iCategoriaRepository) {
        this.iFonecedoresRepository = iFonecedoresRepository;
        this.categoriaService = categoriaService;
        this.iCategoriaRepository = iCategoriaRepository;
    }

    //puxa o fornecedor pelo Id dele, seta ele como DTO
    public FornecedoresDTO findById(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return FornecedoresDTO.of(fornecedorOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa o fornecedor pelo Id dele
    public Fornecedor findByFornecedorId(Long id) {
        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa o fornecedor pelo Cnpj dele
    public FornecedoresDTO findByCnpj(String cnpj) {

        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findByCnpj(cnpj);

        if (fornecedorOptional.isPresent()) {
            return FornecedoresDTO.of(fornecedorOptional.get());
        }
        throw new IllegalArgumentException(String.format("Cnpj %s não existe", cnpj));
    }

    //busca tudo
    public List<Fornecedor> findAll() {
        return iFonecedoresRepository.findAll();
    }


    //salva o fornecedor no Database
    public FornecedoresDTO save(FornecedoresDTO fornecedoresDTO) {

        this.validate(fornecedoresDTO);

        LOGGER.info("Salvando br.com.hbsis.fornecedor");
        LOGGER.debug("br.com.hbsis.fornecedor: {}", fornecedoresDTO);

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setRazao(fornecedoresDTO.getRazao());
        fornecedor.setCnpj(fornecedoresDTO.getCnpj());
        fornecedor.setNomefan(fornecedoresDTO.getNomeFan());
        fornecedor.setEndereco(fornecedoresDTO.getEndereco());
        fornecedor.setTelefone(fornecedoresDTO.getTelefone());
        fornecedor.setEmail(fornecedoresDTO.getEmail());

        fornecedor = this.iFonecedoresRepository.save(fornecedor);

        //Retorna para o postman
        return FornecedoresDTO.of(fornecedor);
    }

    //valida as informacoes
    private void validate(FornecedoresDTO fornecedoresDTO) {
        LOGGER.info("Validando Fornecedor");

        if (fornecedoresDTO == null) {
            throw new IllegalArgumentException("fonecedoresDTO não deve ser nulo");
        }

        if (fornecedoresDTO.getCnpj() == null) {
            throw new IllegalArgumentException("Cnpj não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(fornecedoresDTO.getRazao())) {
            throw new IllegalArgumentException("Razao não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedoresDTO.getEmail())) {
            throw new IllegalArgumentException("email não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(fornecedoresDTO.getEndereco())) {
            throw new IllegalArgumentException("endereço não deve ser nulo/vazio");
        }
        if (StringUtils.isEmpty(fornecedoresDTO.getNomeFan())) {
            throw new IllegalArgumentException("nome fantasia não deve ser nula/vazia");
        }
        if (fornecedoresDTO.getTelefone() == null) {
            throw new IllegalArgumentException("telefone não deve ser nulo/vazio");
        }
        String cont1 = String.valueOf(fornecedoresDTO.getCnpj());
        if (cont1.length() != 14) {
            throw new IllegalArgumentException("Cnpj diferente que 14," +
                    "Confira se colocou algum caracter especial");
        }
        String cont2 = String.valueOf(fornecedoresDTO.getTelefone());
        if (cont2.length() != 13) {
            throw new IllegalArgumentException("telefone diferente que 13 digitos, confira se possui DDD e DDI");
        }
        if (fornecedoresDTO.getEmail().length() > 50) {
            throw new IllegalArgumentException("email muito grande sinto muito");
        }
    }

    // Altera as informacoes do banco
    public FornecedoresDTO update(FornecedoresDTO fonecedoresDTO, Long id) {
        // TODO: 12/12/2019 recalcular os códigos das categorias que estão relacionadas com o fornecedor
        // TODO: 13/12/2019 resolvi
        Optional<Fornecedor> fornecedorExistenteOptional = this.iFonecedoresRepository.findById(id);
        this.validate(fonecedoresDTO);
        if (fornecedorExistenteOptional.isPresent()) {
            Fornecedor fornecedorExistente = fornecedorExistenteOptional.get();

            LOGGER.info("Atualizando Fornecedor... id: [{}]", fornecedorExistente.getId());
            LOGGER.debug("Payload: {}", fonecedoresDTO);
            LOGGER.debug("Fornecedor Existente: {}", fornecedorExistente);

            fornecedorExistente.setRazao(fonecedoresDTO.getRazao());
            fornecedorExistente.setCnpj(fonecedoresDTO.getCnpj());
            fornecedorExistente.setNomefan(fonecedoresDTO.getNomeFan());
            fornecedorExistente.setEndereco(fonecedoresDTO.getEndereco());
            fornecedorExistente.setTelefone(fonecedoresDTO.getTelefone());
            fornecedorExistente.setEmail(fonecedoresDTO.getEmail());

            List<Categoria> buscaCategorias = iCategoriaRepository.findAllByFornecedor_IdIs(id);
            for (Categoria categorai :buscaCategorias) {
                CategoriaDTO categoriaDTO = new CategoriaDTO();
                categoriaDTO.setId(categorai.getId());
                categoriaDTO.setNomeCategoria(categorai.getNomeCategoria());
                categoriaDTO.setIdCategoriaFornecedor(categorai.getFornecedor().getId());
                categoriaDTO.setCodigo("CAT"+fornecedorExistente.getCnpj().substring(10,14)+categorai.getCodCategoria().substring(7,10));
                categoriaService.update(categoriaDTO, categoriaDTO.getId());
            }
            fornecedorExistente = this.iFonecedoresRepository.save(fornecedorExistente);

            return FornecedoresDTO.of(fornecedorExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //delete o fornecedor do Database
    public void delete(Long id) {
        LOGGER.info("Executando delete para Fornecedor de ID: [{}]", id);

        this.iFonecedoresRepository.deleteById(id);
    }

    public String getCnpjMask(String cnpj) throws ParseException {

        MaskFormatter mask = new MaskFormatter("##.###.###/####-##");
        mask.setValueContainsLiteralCharacters(false);

        String formatado = mask.valueToString(cnpj);
        return formatado;
    }
}