package br.com.hbsis.CategoriaProduto;


import br.com.hbsis.fornecedor.FonecedoresDTO;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedoresRepository;
import com.opencsv.CSVReader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final IProdutoRepository iProdutoRepository;
    private final IFornecedoresRepository iFornecedorRepository;
    private final FornecedorService fornecedorService;

    @Autowired
    public ProdutoService(IProdutoRepository iProdutoRepository, IFornecedoresRepository iFornecedorRepository, FornecedorService fornecedorService){
        this.iProdutoRepository = iProdutoRepository;
        this.iFornecedorRepository = iFornecedorRepository;
        this.fornecedorService = fornecedorService;
    }
    public List<Produto> findAll(){
        return iProdutoRepository.findAll();
    }
    public List<String> listToPrint(){
        List<String> lista = new ArrayList<>();
        for(Produto linhaCSV : iProdutoRepository.findAll()){
            String construtor = linhaCSV.getId() + ";" + linhaCSV.getId() + ";" + linhaCSV.getNomeProduto() + ";"
                    + linhaCSV.getId_produto_fornecedor().getId()+ ";";
            lista.add(construtor);
        }
        return lista;
    }
    public List<Produto> saveAll(List<Produto> produtossave) throws Exception {

        LOGGER.info("\"Salvando br.com.hbsis.Produto");
        LOGGER.debug("br.com.hbsis.Produto: {}", produtossave);

        return iProdutoRepository.saveAll(produtossave);
    }

    public <FornecedorDTO> List<Produto> readAll(Reader reader) throws Exception {
        CSVReader csvReader = new CSVReader(reader);
        List<Produto> resultadoLeitura = new ArrayList<>();
        String[] linha;
        while((linha = csvReader.readNext()) != null ) {
            Produto categoria = new Produto();

            FornecedorService fornecedorService = new FornecedorService(iFornecedorRepository);
            FornecedorDTO fornecedorDTO;
            Fornecedor fornecedor = new Fornecedor();

            fornecedorDTO = (FornecedorDTO) fornecedorService.findById(Long.parseLong(linha[3]));

            fornecedor.setRazao(((FonecedoresDTO) fornecedorDTO).getRazao());
            fornecedor.setCnpj(((FonecedoresDTO) fornecedorDTO).getCnpj());
            fornecedor.setNomefan(((FonecedoresDTO) fornecedorDTO).getNomeFan());
            fornecedor.setEndereco(((FonecedoresDTO) fornecedorDTO).getEndereco());
            fornecedor.setTelefone(((FonecedoresDTO) fornecedorDTO).getTelefone());
            fornecedor.setEmail(((FonecedoresDTO) fornecedorDTO).getEmail());

            categoria.setId(Long.parseLong(linha[1]));
            categoria.setNomeProduto(linha[2]);
            categoria.setId_produto_fornecedor(fornecedor);

            resultadoLeitura.add(categoria);
        }
        reader.close();
        csvReader.close();
        return resultadoLeitura;
    }

    public ProdutoDTO save(ProdutoDTO produtoDTO){
        this.validate(produtoDTO);
        Fornecedor findByFornecedorId = fornecedorService.findByFornecedorId(produtoDTO.getId_produto_fornecedor());

        LOGGER.info("\"Salvando br.com.hbsis.Produto");
        LOGGER.debug("br.com.hbsis.Produto: {}", produtoDTO);

        Produto produto = new Produto();

        produto.setNomeProduto(produtoDTO.getNomeProduto());
        produto.setCodProduto(produtoDTO.getCodProduto());
        produto.setId_produto_fornecedor(fornecedorService.findByFornecedorId(produtoDTO.getId_produto_fornecedor()));

        //produto.setId_produto_fornecedor(findByFornecedorId);

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
