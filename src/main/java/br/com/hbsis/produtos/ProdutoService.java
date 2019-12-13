package br.com.hbsis.produtos;

import br.com.hbsis.categorias.CategoriaService;
import br.com.hbsis.categorias.ICategoriaRepository;
import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.IFornecedoresRepository;
import br.com.hbsis.linhas.ILinhasRepository;
import br.com.hbsis.linhas.LinhasService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //exporta
//    public void exportCSV(HttpServletResponse retorno) throws Exception {
//        String nameArq = "produto.csv";
//        retorno.setContentType("text/csv");
//        retorno.setHeader(HttpHeaders.CONTENT_DISPOSITION,
//                "attachment; filename=\"" + nameArq + "\"");
//
//        PrintWriter info = retorno.getWriter();
//
//        ICSVWriter csvWriter = new CSVWriterBuilder(info).withSeparator(';')
//                .withEscapeChar(CSVWriter.DEFAULT_ESCAPE_CHARACTER)
//                .withLineEnd(CSVWriter.DEFAULT_LINE_END).build();
//
//        String nomeColunas[] = {"codigo", "nome",
//                "preço", "quantidade_por_caixa",
//                "peso_por_unidade",
//                "data_validade", "codigo_linha_categoria",
//                "linha_categoria", "codigo_categoria",
//                "categoria", "razao_social_fornecedor",
//                "cnpj_fornecedor"
//        };
//        csvWriter.writeNext(nomeColunas);
//
//        for (Produtos produtos : this.findAll()) {
//            String splitar = String.valueOf(produtos.getValidade());
//            String[] splitado = splitar.split("-");
//            String formatar = produtos.getLinhas().getCategoria().getFornecedor().getCnpj();
//
//            String fornecedor = formatar.substring(0, 2) + "." + formatar.substring(2, 5) + "." + formatar.substring(5, 8) + "/" +
//                    formatar.substring(8, 12) + "-" + formatar.substring(12, 14);
//
//            csvWriter.writeNext(new String[]{
//                    produtos.getCodProdutos().toUpperCase(),
//                    produtos.getNomeProduto(),
//                    String.valueOf(produtos.getPreco()),
//                    String.valueOf(produtos.getUniPerCax()),
//                    (produtos.getPesoPerUni()) + produtos.getUnidade(),
//                    (splitado[2].substring(0, 2) + "/" + splitado[1] + "/" + splitado[0]),
//                    produtos.getLinhas().getCodLinhas(),
//                    produtos.getLinhas().getNomeLinhas(),
//                    produtos.getLinhas().getCategoria().getCodCategoria(),
//                    produtos.getLinhas().getCategoria().getNomeCategoria(),
//                    produtos.getLinhas().getCategoria().getFornecedor().getRazao(),
//                    fornecedor
//            });
//        }
//    }
//
//    // faz a importacao
//    public List<Produtos> readAll(MultipartFile file) throws Exception {
//        InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream());
//        CSVReader csvReader = new CSVReaderBuilder(inputStreamReader).withSkipLines(1).build();
//
//        List<String[]> linhas = csvReader.readAll();
//        List<Produtos> resultadoLeitura = new ArrayList<>();
//
//        for (String[] l : linhas) {
//            try {
//                String[] bean = l[0].replaceAll("\"", "").split(";");
//
//                Produtos produtos = new Produtos();
//                Linhas linhasClass = new Linhas();
//                LinhasDTO linhasDTO;
//
//                Optional<Produtos> optionalProdutos = this.iProdutosRepository.findByCodProdutos(bean[0]);
//
//                if (!optionalProdutos.isPresent()) {
//
//                    produtos.setCodProdutos(bean[0].toUpperCase());
//                    produtos.setNomeProduto(bean[1]);
//                    produtos.setPreco(Float.parseFloat(bean[2].replaceAll("[R$]", "")));
//                    produtos.setUniPerCax(Float.parseFloat(bean[3]));
//                    produtos.setPesoPerUni(Float.parseFloat(bean[4]));
//                    produtos.setUnidade(bean[5]);
//                    produtos.setValidade(LocalDateTime.parse(bean[6].substring(7, 11)
//                            + bean[6].substring(3, 7)
//                            + bean[6].substring(1, 3) + "T00:00:00"));
//
//                    Optional<Linhas> optionalLinhas = iLinhasRepository.findByCodLinhas(bean[7]);
//
//                    if (optionalLinhas.isPresent()) {
//
//                        linhasDTO = linhasService.findByCodLinhas(bean[7]);
//
//                        linhasClass.setId(linhasDTO.getId());
//                        linhasClass.setNomeLinhas(linhasDTO.getNomeLinhas());
//                        linhasClass.setCodLinhas(linhasDTO.getCodLinhas());
//                        produtos.setLinhas(linhasClass);
//
//                        produtos.setLinhas(linhasClass);
//                        resultadoLeitura.add(produtos);
//                        return iProdutosRepository.saveAll(resultadoLeitura);
//                    }
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        return resultadoLeitura;
//    }

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
