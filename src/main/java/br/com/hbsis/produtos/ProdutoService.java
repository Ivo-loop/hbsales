package br.com.hbsis.produtos;

import br.com.hbsis.Tools.ExportImport.ExportCSV;
import br.com.hbsis.Tools.ExportImport.ImportCSV;
import br.com.hbsis.categorias.Categoria;
import br.com.hbsis.categorias.CategoriaDTO;
import br.com.hbsis.categorias.CategoriaService;
import br.com.hbsis.fornecedor.Fornecedor;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.linhas.Linhas;
import br.com.hbsis.linhas.LinhasDTO;
import br.com.hbsis.linhas.LinhasService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final IProdutosRepository iProdutosRepository;
    private final FornecedorService fornecedorService;
    private final CategoriaService categoriaService;
    private final LinhasService linhasService;
    private final ExportCSV exportCSV;
    private final ImportCSV importCSV;

    public ProdutoService(FornecedorService fornecedorService, CategoriaService categoriaService, LinhasService linhasService, IProdutosRepository iProdutosRepository, ExportCSV exportCSV, ImportCSV importCSV) {
        this.fornecedorService = fornecedorService;
        this.categoriaService = categoriaService;
        this.linhasService = linhasService;
        this.iProdutosRepository = iProdutosRepository;
        this.exportCSV = exportCSV;
        this.importCSV = importCSV;
    }

    @Autowired
    //busca tudo
    public List<Produtos> findAll() {
        return iProdutosRepository.findAll();
    }

    //busca produto pro Id
    public ProdutosDTO findById(Long id) {
        Optional<Produtos> ProdutoOpcional = this.iProdutosRepository.findById(id);

        if (ProdutoOpcional.isPresent()) {
            return ProdutosDTO.of(ProdutoOpcional.get());
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //busca produto pro Id
    public Produtos findByIdProduto(Long id) {
        Optional<Produtos> ProdutoOpcional = this.iProdutosRepository.findById(id);

        if (ProdutoOpcional.isPresent()) {
            return ProdutoOpcional.get();
        }

        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    private ProdutosDTO findByCodProdutos(String cod) {
        Optional<Produtos> categoriaOpcional = this.iProdutosRepository.findByCodProdutos(cod);

        if (categoriaOpcional.isPresent()) {
            return ProdutosDTO.of(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", cod));
    }

    //importa produto do para o fornecedor
    void importProdutoFornecedor(String cod, MultipartFile multipartFile) {
        String[][] CSV = importCSV.leitorCSV(multipartFile);

        for (String[] campo : CSV) {
            if (campo[0] != null) {

                String codProduto = campo[0],   nomeProduto = campo[1];
                String precoProduto = campo[2], quant = campo[3];
                String peso = campo[4],         unidadeMed = campo[5];
                String codLinha = campo[7],     nomeLinha = campo[8];
                String codCat = campo[9],       nomeCAT = campo[10];
                String validade = (campo[6].substring(6, 10) + campo[6].substring(2, 6)
                        + campo[6].substring(0, 2) + "T00:00:00").replaceAll("/", "-");

                //fornecedor existe
                Optional<Fornecedor> optionalFornecedor = fornecedorService.findByCnpjOptional(cod);
                if (optionalFornecedor.isPresent()) {

                    //Categoria nao existe cria categoria
                    Optional<Categoria> optionalCategoria = categoriaService.findByCodCategoriaOptinal(codCat);
                    if (!optionalCategoria.isPresent()) {
                        CategoriaDTO categoriaDTO;
                        categoriaDTO = new CategoriaDTO(null, nomeCAT, fornecedorService.findByCnpj(cod).getId(), codCat.substring(7, 10));
                        categoriaService.save(categoriaDTO);
                    }

                    //Categoria existe procura e pega ela
                    Optional<Categoria> optionalCategoria2 = categoriaService.findByCodCategoriaOptinal(codCat);
                    if (!optionalCategoria.isPresent() && optionalCategoria2.isPresent()) {

                        CategoriaDTO categoriaDTO;
                        categoriaDTO = categoriaService.findByCodCategoria(codCat);

                        Categoria categoriaclass = categoriaService.setCategoria(categoriaDTO);
                        categoriaclass.setFornecedor(fornecedorService.findById(fornecedorService.findByCnpj(cod).getId()));

                    }

                    //Linhas nao existe cria linhas
                    Optional<Linhas> optionalLinhas = linhasService.findByCodLinhasOptional(codLinha);
                    if (!optionalLinhas.isPresent()) {
                        LinhasDTO linhasDTO;
                        linhasDTO = new LinhasDTO(null, nomeLinha, codLinha,
                                categoriaService.findByCodCategoria(codCat).getId());
                        linhasService.save(linhasDTO);
                    }

                    //Linhas existe procura e pega ela
                    Optional<Linhas> optionalLinhas1 = linhasService.findByCodLinhasOptional(codLinha);
                    if (!optionalLinhas.isPresent() && optionalLinhas1.isPresent()) {
                        Categoria categoriaclass = new Categoria();

                        Linhas linhasClass = new Linhas();
                        LinhasDTO linhasDTO;

                        linhasDTO = linhasService.findByCodLinhasDTO(codLinha);

                        linhasClass.setId(linhasDTO.getId());
                        linhasClass.setNomeLinhas(linhasDTO.getNomeLinhas());
                        linhasClass.setCodLinhas(linhasDTO.getCodLinhas());
                        linhasClass.setCategoria(categoriaclass);
                    }

                    //Produto nao existe cria produtos
                    Optional<Produtos> optionalProdutos = this.iProdutosRepository.findByCodProdutos(codProduto);
                    if (!optionalProdutos.isPresent()) {

                        ProdutosDTO produtosDTO;
                        produtosDTO = new ProdutosDTO(null, nomeProduto, codProduto,
                                Float.parseFloat(precoProduto),
                                linhasService.findByCodLinhasDTO(codLinha).getId(), Float.parseFloat(nomeProduto),
                                Float.parseFloat(peso), unidadeMed, (LocalDateTime.parse(validade))
                        );
                        this.save(produtosDTO);
                    }

                    if (optionalProdutos.isPresent()) {
                        Linhas linhasClass = new Linhas();
                        Produtos produtos = new Produtos();

                        produtos.setCodProdutos(codProduto.toUpperCase());
                        produtos.setNomeProduto(nomeProduto);
                        produtos.setPreco(Float.parseFloat(precoProduto));
                        produtos.setUniPerCax(Float.parseFloat(quant));
                        produtos.setPesoPerUni(Float.parseFloat(peso));
                        produtos.setUnidade(unidadeMed);
                        produtos.setValidade(LocalDateTime.parse((validade)));
                        produtos.setLinhas(linhasClass);

                        this.update(ProdutosDTO.of(produtos), this.findByCodProdutos(codProduto).getId());
                    }
                }
            }
            //fornecedor nao existe
            else {
                throw new IllegalArgumentException("fornecedor nao existe:" + cod);
            }
        }

    }


    //Faz a exportacao do banco
    void exportCSV(HttpServletResponse response) throws IOException, ParseException {

        //seta cabeça do cvs
        String header = " código ; nome ; preço ; quantidade ; peso ; validade ;" +
                " código da linha ; nome da Linha ;" +
                " Código da Categoria ; Nome da Categoria ;" +
                "CNPJ ; razão social";

        exportCSV.writerHeader(response, header, "Produtos");
        PrintWriter printWriter = response.getWriter();

        for (Produtos produtos : iProdutosRepository.findAll()) {

            String splitar = String.valueOf(produtos.getValidade());
            String validade = splitar.substring(0,10).replaceAll("-","/");

            String nome = produtos.getNomeProduto();
            String cod = produtos.getCodProdutos();
            String preco = "R$" + (produtos.getPreco());
            String perCaixa = String.valueOf(produtos.getUniPerCax());
            String pesoPerUnid = produtos.getPesoPerUni() + produtos.getUnidade();

            String codLinnhas = produtos.getLinhas().getCodLinhas();
            String nomeLinhas = produtos.getLinhas().getNomeLinhas();
            String codCategoria = produtos.getLinhas().getCategoria().getCodCategoria();
            String nomeCategoria = produtos.getLinhas().getCategoria().getNomeCategoria();
            String cnpj = fornecedorService.getCnpjMask(produtos.getLinhas().getCategoria().getFornecedor().getCnpj());
            String razao = produtos.getLinhas().getCategoria().getFornecedor().getRazao();

            //escreve os dados
            printWriter.println(cod + ";" + nome + ";" + preco + ";" + perCaixa + ";" + pesoPerUnid
                    + ";" + validade + ";" + codLinnhas + ";" + nomeLinhas + ";" + codCategoria
                    + ";" + nomeCategoria + ";" + cnpj + ";" + razao
            );
        }
        printWriter.close();
    }

    //Faz a importacao do banco
    void importCSV(MultipartFile multipartFile) {
        String[][] CSV = importCSV.leitorCSV(multipartFile);

        for (String[] campo : CSV) {
            if (campo[0] != null) {
                String codProdutos = campo[0];
                String nomeProduto = campo[1];
                String preco = campo[2].replaceAll("[R$]", "");
                String uniPerCax = campo[3];
                String pesoPerUni = campo[4].replaceAll("[kgm]", "");
                String unidade = campo[4].replaceAll("[\\d,]", "");
                String validade = (campo[5].substring(6, 10) + campo[5].substring(2, 6)
                        + campo[5].substring(0, 2) + "T00:00:00").replaceAll("/", "-");
                String codLinhas = campo[5];

                Optional<LinhasDTO> linhasOptional = Optional.ofNullable(linhasService.findByCodLinhasDTO(campo[6]));
                Optional<Produtos> produtoExisteOptional = this.iProdutosRepository.findByCodProdutos(campo[0]);

                //confere se existe se nao ele inseri
                if (!(produtoExisteOptional.isPresent()) && linhasOptional.isPresent()) {
                    ProdutosDTO produtosDTO = new ProdutosDTO(null, nomeProduto,
                            codProdutos, Float.parseFloat(preco),
                            linhasService.findByCodLinhasDTO(codLinhas).getId(),
                            Float.parseFloat(uniPerCax), Float.parseFloat(pesoPerUni),
                            unidade, LocalDateTime.parse(validade)
                    );
                    this.save(produtosDTO);
                }
            }
        }
    }


    //salva
    public ProdutosDTO save(ProdutosDTO produtosDTO) {
        this.validate(produtosDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Produto");
        LOGGER.debug("br.com.hbsis.Produto: {}", produtosDTO);

        Produtos produtos = new Produtos();
        StringBuilder cont = new StringBuilder(String.valueOf(produtosDTO.getCodProdutos()));
        for (; cont.length() < 10; ) {
            cont.insert(0, "0");
        }

        produtos.setNomeProduto(produtosDTO.getNomeProduto());
        produtos.setCodProdutos(cont.toString().toUpperCase());
        produtos.setPreco(produtosDTO.getPreco());
        produtos.setPesoPerUni(produtosDTO.getPesoPerUni());
        produtos.setUniPerCax(produtosDTO.getUniPerCax());
        produtos.setValidade(produtosDTO.getValidade());
        produtos.setUnidade(produtosDTO.getUnidade());
        Long id = produtosDTO.getIdProdutosLinhas();

        produtos.setLinhas(linhasService.findById(id));

        //Retorna para o postman
        Produtos save = this.iProdutosRepository.save(produtos);
        return ProdutosDTO.of(save);
    }

    //valida
    private void validate(ProdutosDTO produtosDTO) {
        LOGGER.info("Validando Produto");

        if (produtosDTO == null) {
            throw new IllegalArgumentException("ProdutoDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(produtosDTO.getNomeProduto())) {
            throw new IllegalArgumentException("Nome da Produto não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(produtosDTO.getCodProdutos())) {
            throw new IllegalArgumentException("Cod linhas não deve ser nula/vazia");
        }
        if (produtosDTO.getCodProdutos().length() > 10) {
            throw new IllegalArgumentException("Cod linhas nao deve possui mais de 10 digitos");
        }
        if (produtosDTO.getPesoPerUni() == null) {
            throw new IllegalArgumentException("Cod peso por unidade não deve ser nula/vazia");
        }
        if (produtosDTO.getUniPerCax() == null) {
            throw new IllegalArgumentException("Cod peso por unidade não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(produtosDTO.getUnidade())) {
            throw new IllegalArgumentException("Unidade de medida nao pode ser nula/vazia");
        }
    }

    //altera
    public ProdutosDTO update(ProdutosDTO produtosDTO, Long id) {
        Optional<Produtos> ProdutoExistencialOpcional = this.iProdutosRepository.findById(id);
        this.validate(produtosDTO);
        if (ProdutoExistencialOpcional.isPresent()) {
            Produtos produtosExistente = ProdutoExistencialOpcional.get();

            LOGGER.info("Atualizando Produto... id: [{}]", produtosExistente.getId());
            LOGGER.debug("Payload: {}", produtosDTO);
            LOGGER.debug("Produto Existente: {}", produtosExistente);

            produtosExistente.setNomeProduto(produtosDTO.getNomeProduto());
            produtosExistente.setCodProdutos(produtosDTO.getCodProdutos());
            produtosExistente.setPreco(produtosDTO.getPreco());
            produtosExistente.setLinhas(linhasService.findById(produtosDTO.getIdProdutosLinhas()));
            produtosExistente.setUniPerCax(produtosDTO.getUniPerCax());
            produtosExistente.setPesoPerUni(produtosDTO.getPesoPerUni());
            produtosExistente.setValidade(produtosDTO.getValidade());

            produtosExistente = this.iProdutosRepository.save(produtosExistente);

            return ProdutosDTO.of(produtosExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //deleta
    public void delete(Long id) {
        LOGGER.info("Executando delete para Produto de ID: [{}]", id);

        this.iProdutosRepository.deleteById(id);
    }
}
