package br.com.hbsis.produtos;

import br.com.hbsis.categorias.CategoriaService;
import br.com.hbsis.categorias.ICategoriaRepository;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedoresRepository;
import br.com.hbsis.linhas.ILinhasRepository;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProdutoService.class);
    private final IFornecedoresRepository iFornecedoresRepository;
    private final FornecedorService fornecedorService;
    private final ICategoriaRepository iCategoriaRepository;
    private final CategoriaService categoriaService;
    private final ILinhasRepository iLinhasRepository;
    private final LinhasService linhasService;
    private final IProdutosRepository iProdutosRepository;

    @Autowired
    public ProdutoService(IProdutosRepository iProdutosRepository, LinhasService linhasService, IFornecedoresRepository iFornecedoresRepository, FornecedorService fornecedorService, FornecedorService fornecedorService1, ICategoriaRepository iCategoriaRepository, CategoriaService categoriaService, CategoriaService categoriaService1, ILinhasRepository iLinhasRepository) {
        this.iFornecedoresRepository = iFornecedoresRepository;
        this.fornecedorService = fornecedorService;
        this.iCategoriaRepository = iCategoriaRepository;
        this.categoriaService = categoriaService;
        this.iProdutosRepository = iProdutosRepository;
        this.linhasService = linhasService;
        this.iLinhasRepository = iLinhasRepository;
    }

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

    public ProdutosDTO findByCodProdutos(String cod) {
        Optional<Produtos> categoriaOpcional = this.iProdutosRepository.findByCodProdutos(cod);

        if (categoriaOpcional.isPresent()) {
            return ProdutosDTO.of(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", cod));
    }

    //importa produto do para o fornecedor
//    public void importProdutoFornecedor(String cod, MultipartFile file) throws Exception {
//        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
//
//        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader)
//                .withSkipLines(1)
//                .build();
//
//        List<String[]> linhaString = csvReader.readAll();
//        List<Produtos> resultadoLeitura = new ArrayList<>();
//
//        for (String[] linha : linhaString) {
//            try {
//                String[] bean = linha[0].replaceAll("\" ", "").replaceAll("/", "-").split(";");
//
//                //criar na hora que eu for setar
//                // TODO: 12/12/2019 só realizar a consulta quando a for utilizar o valor
//                Optional<Fornecedor> optionalFornecedor = iFornecedoresRepository.findByCnpj(cod);
//                Optional<Categoria> optionalCategoria = iCategoriaRepository.findByCodCategoria(bean[9]);
//                Optional<Linhas> optionalLinhas = iLinhasRepository.findByCodLinhas(bean[7]);
//                Optional<Produtos> optionalProdutos = this.iProdutosRepository.findByCodProdutos(bean[0]);
//
//                //fornecedor existe
//                if (optionalFornecedor.isPresent()) {
//
//                    //Categoria nao existe cria categoria
//                    if (!optionalCategoria.isPresent()) {
//                        CategoriaDTO categoriaDTO;
//                        categoriaDTO = new CategoriaDTO(null, bean[10], fornecedorService.findByCnpj(cod).getId(), bean[9].substring(7,10));
//                        categoriaService.save(categoriaDTO);
//                    }
//
//                    //Categoria existe procura e pega ela
//                    Optional<Categoria> optionalCategoria2 = iCategoriaRepository.findByCodCategoria(bean[9]);
//                    if (!optionalCategoria.isPresent() && optionalCategoria2.isPresent()) {
//                        Categoria categoriaclass = new Categoria();
//                        CategoriaDTO categoriaDTO;
//
//                        categoriaDTO = categoriaService.findByCodCategoria(bean[9]);
//
//                        categoriaclass.setId(categoriaDTO.getId());
//                        categoriaclass.setCodCategoria(bean[9]);
//                        categoriaclass.setNomeCategoria(bean[10]);
//                        categoriaclass.setFornecedor(fornecedorService.findByFornecedorId(fornecedorService.findByCnpj(cod).getId()));
//                    }
//
//                    //Linhas nao existe cria linhas
//                    if (!optionalLinhas.isPresent()) {
//                        LinhasDTO linhasDTO;
//                        linhasDTO = new LinhasDTO(null, bean[8], bean[7], categoriaService.findByCodCategoria(bean[9]).getId());
//                        linhasService.save(linhasDTO);
//                    }
//
//                    //Linhas existe procura e pega ela
//                    Optional<Linhas> optionalLinhas1 = iLinhasRepository.findByCodLinhas(bean[7]);
//                    if (!optionalLinhas.isPresent() && optionalLinhas1.isPresent()) {
//                        Categoria categoriaclass = new Categoria();
//
//                        Linhas linhasClass = new Linhas();
//                        LinhasDTO linhasDTO;
//
//                        linhasDTO = linhasService.findByCodLinhas(bean[7]);
//
//                        linhasClass.setId(linhasDTO.getId());
//                        linhasClass.setNomeLinhas(linhasDTO.getNomeLinhas());
//                        linhasClass.setCodLinhas(linhasDTO.getCodLinhas());
//                        linhasClass.setCategoria(categoriaclass);
//                    }
//
//                    //Produto nao existe cria produtos
//                    if (!optionalProdutos.isPresent()) {
//
//                        ProdutosDTO produtosDTO;
//                        produtosDTO = new ProdutosDTO(null, bean[1], bean[0],
//                                Float.parseFloat(bean[2].replaceAll("[R$]", "")),
//                                linhasService.findByCodLinhas(bean[7]).getId(), Float.parseFloat(bean[3]),
//                                Float.parseFloat(bean[4]), bean[5], (LocalDateTime.parse(bean[6].substring(6, 10)
//                                + bean[6].substring(2, 6)
//                                + bean[6].substring(0, 2) + "T00:00:00"))
//                        );
//                        this.save(produtosDTO);
//                    } else {
//
//                        Optional<Produtos> optionalProdutos1 = this.iProdutosRepository.findByCodProdutos(bean[0]);
//                        if (!optionalProdutos.isPresent() && optionalProdutos1.isPresent()) {
//                            Linhas linhasClass = new Linhas();
//
//                            Produtos produtos = new Produtos();
//
//                            produtos.setCodProdutos(bean[0].toUpperCase());
//                            produtos.setNomeProduto(bean[1]);
//                            produtos.setPreco(Float.parseFloat(bean[2].replaceAll("[R$]", "")));
//                            produtos.setUniPerCax(Float.parseFloat(bean[3]));
//                            produtos.setPesoPerUni(Float.parseFloat(bean[4]));
//                            produtos.setUnidade(bean[5]);
//                            System.out.println(bean[6]);
//                            produtos.setValidade(LocalDateTime.parse(bean[6].substring(6, 10)
//                                    + bean[6].substring(2, 6)
//                                    + bean[6].substring(0, 2) + "T00:00:00"));
//                            produtos.setLinhas(linhasClass);
//
//                            produtos.setLinhas(linhasClass);
//
//
//                            this.update(ProdutosDTO.of(produtos), this.findByCodProdutos(bean[0]).getId());
//                        }
//                    }
//                }
//                //fornecedor nao existe
//                else {
//                    throw new IllegalArgumentException("fornecedor nao existe:" + cod);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    //Faz a exportacao do banco
    public void exportCSV(HttpServletResponse response) throws IOException, ParseException {

        //seta o nome do arq
        String categoriaCSV = "produto.csv";

        //seta o tipo do arq da resposta
        response.setContentType("text/csv");

        //config do header
        String headerKey = "Content-Disposition";

        //como é aberto em anexo
        String headerValue = String.format("attachment; filename=\"%s\"", categoriaCSV);

        response.setHeader(headerKey, headerValue);

        //instancia Print e seta como escritor
        PrintWriter printWriter = response.getWriter();

        //seta cabeça do cvs
        String header = " código ; nome ; preço ; quantidade ; peso ; validade ;"+
                " código da linha ; nome da Linha ;"+
                " Código da Categoria ; Nome da Categoria ;"+
                "CNPJ ; razão social";

        // escreve o cabeçario
        printWriter.println(header);
        for (Produtos produtos : iProdutosRepository.findAll()) {

            String splitar = String.valueOf(produtos.getValidade());
            String[] splitado = splitar.split("-");

            String nome = produtos.getNomeProduto();
            String cod = produtos.getCodProdutos();
            String preco = "R$"+(produtos.getPreco());
            String perCaixa = String.valueOf(produtos.getUniPerCax());
            String pesoPerUnid = produtos.getPesoPerUni()+produtos.getUnidade();
            String validade = splitado[2].substring(0, 2) + "/" + splitado[1] + "/" + splitado[0];

            String codLinnhas = produtos.getLinhas().getCodLinhas();
            String nomeLinhas = produtos.getLinhas().getNomeLinhas();
            String codCategoria = produtos.getLinhas().getCategoria().getCodCategoria();
            String nomeCategoria = produtos.getLinhas().getCategoria().getNomeCategoria();
            String cnpj = fornecedorService.getCnpjMask(produtos.getLinhas().getCategoria().getFornecedor().getCnpj());
            String razao = produtos.getLinhas().getCategoria().getFornecedor().getRazao();
            //escreve os dados
            printWriter.println(cod +";"+ nome +";"+ preco +";"+ perCaixa +";"+ pesoPerUnid +";"+ validade
                    +";"+ codLinnhas +";"+ nomeLinhas +";"+ codCategoria +";"+ nomeCategoria
                    +";"+ cnpj +";"+ razao
            );
        }
        printWriter.close();
    }

    //Faz a importacao do banco
    public void importCSV(MultipartFile importCategoria) {
        String arquivo = "";
        String separator = ";";
        //achado na net
        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importCategoria.getInputStream()))) {
            //para pular uma linha
            leitor.readLine();
            //le as linhas
            while ((arquivo = leitor.readLine()) != null) {
                String[] produtoCSV = arquivo.split(separator);
                Optional<LinhasDTO> linhasOptional =  Optional.ofNullable(linhasService.findByCodLinhas(produtoCSV[6]));
                Optional<Produtos> produtoExisteOptional = this.iProdutosRepository.findByCodProdutos(produtoCSV[0]);

                //confere se existe se nao ele inseri
                if (!(produtoExisteOptional.isPresent()) && linhasOptional.isPresent()) {
                    ProdutosDTO produtosDTO = new ProdutosDTO();
                    produtosDTO.setNomeProduto(produtoCSV[1]);
                    produtosDTO.setCodProdutos(produtoCSV[0]);
                    produtosDTO.setPreco(Float.parseFloat(produtoCSV[2].replaceAll("[R$]","")));
                    produtosDTO.setUniPerCax(Float.parseFloat(produtoCSV[3]));
                    produtosDTO.setPesoPerUni(Float.parseFloat(produtoCSV[4].substring(0,produtoCSV[4].length()-2)));
                    produtosDTO.setUnidade(produtoCSV[4].substring(produtoCSV[4].length()-2, produtoCSV[4].length()));
                    produtosDTO.setValidade(LocalDateTime.parse((produtoCSV[5].substring(6, 10)
                            + produtoCSV[5].substring(2, 6)
                            + produtoCSV[5].substring(0, 2) + "T00:00:00").replaceAll("/","-")));
                    LinhasDTO linhasDTO = linhasService.findByCodLinhas(produtoCSV[6]);
                    produtosDTO.setIdProdutosLinhas(linhasDTO.getId());
                    this.save(produtosDTO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //salva
    public ProdutosDTO save(ProdutosDTO produtosDTO) {
        this.validate(produtosDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Produto");
        LOGGER.debug("br.com.hbsis.Produto: {}", produtosDTO);

        Produtos produtos = new Produtos();

        String cont = String.valueOf(produtosDTO.getCodProdutos());

        for (; cont.length() < 10; ) {
            cont = "0" + cont;
        }

        produtos.setNomeProduto(produtosDTO.getNomeProduto());
        produtos.setCodProdutos(cont.toUpperCase());
        produtos.setPreco(produtosDTO.getPreco());
        produtos.setPesoPerUni(produtosDTO.getPesoPerUni());
        produtos.setUniPerCax(produtosDTO.getUniPerCax());
        produtos.setValidade(produtosDTO.getValidade());
        produtos.setUnidade(produtosDTO.getUnidade());
        Long id = produtosDTO.getIdProdutosLinhas();

        produtos.setLinhas(linhasService.findBylinhasId(id));

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
            produtosExistente.setLinhas(linhasService.findBylinhasId(produtosDTO.getIdProdutosLinhas()));
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
