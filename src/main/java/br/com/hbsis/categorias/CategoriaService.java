package br.com.hbsis.categorias;

import br.com.hbsis.ExportImport.ExportCSV;
import br.com.hbsis.ExportImport.ImportCSV;
import br.com.hbsis.fornecedor.FornecedorDTO;
import br.com.hbsis.fornecedor.FornecedorService;
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
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);
    private final ICategoriaRepository iCategoriaRepository;
    private final FornecedorService fornecedorService;
    private final CategoriaFind categoriaFind;
    private final ExportCSV exportCSV;
    private final ImportCSV importCSV;
    private final AlterCod alterCod;

    @Autowired
    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService, ExportCSV exportCSV, CategoriaFind categoriaFind, ImportCSV importCSV, AlterCod alterCod) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;
        this.exportCSV = exportCSV;
        this.categoriaFind = categoriaFind;
        this.importCSV = importCSV;
        this.alterCod = alterCod;
    }

    //busca toda categoria
    public List<Categoria> findAll() {
        return categoriaFind.findAll();
    }

    //busca a Categoria pelo Id, retorna ele como DTO
    public CategoriaDTO findById(Long id) {
        return categoriaFind.findByIdDTO(id);
    }

    //busca a Categoria pelo Id, retorna categoria
    public Categoria findByCategoriaId(Long id) {
        return categoriaFind.findById(id);
    }

    //Busca pelo cod da categoria
    public Optional<Categoria> findByCodCategoriaOptinal(String cod) {
        return categoriaFind.findByCodCategoriaOptinal(cod);
    }

    //Busca pelo cod da categoria
    public CategoriaDTO findByCodCategoria(String cod) {
        return categoriaFind.findByCodCategoria(cod);
    }

    public Categoria setCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoria.setFornecedor(fornecedorService.findById(categoriaDTO.getIdCategoriaFornecedor()));
        categoria.setCodCategoria(alterCod.codCategoria(categoria, alterCod.number(categoriaDTO)));

        return categoria;
    }

    public Categoria setCategoria(CategoriaDTO categoriaDTO, Categoria categoria) {
        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoria.setFornecedor(fornecedorService.findById(categoriaDTO.getIdCategoriaFornecedor()));
        categoria.setCodCategoria(alterCod.codCategoria(categoria, alterCod.number(categoriaDTO)));

        return categoria;
    }

    //Faz a exportaçao do banco em csv
    void exportCSV(HttpServletResponse response) throws IOException, ParseException {

        //seta cabeça do csv
        String header = " Código da categoria ; Nome da categoria ; Razão social ; CNPJ";
        exportCSV.Export(response, header);

        PrintWriter printWriter = response.getWriter();
        for (Categoria categoria : iCategoriaRepository.findAll()) {
            String nome = categoria.getNomeCategoria();
            String cod = categoria.getCodCategoria();
            String razao = categoria.getFornecedor().getRazao();
            String cnpj = fornecedorService.getCnpjMask(categoria.getFornecedor().getCnpj());
            //escreve os dados
            printWriter.println(cod + ";" + nome + ";" + razao + ";" + cnpj);
        }
        printWriter.close();
    }

    //Faz a importacao do banco
    void importCSV(MultipartFile importCategoria) {
        String[][] CSV = importCSV.Import(importCategoria);
        for (String[] campo : CSV) {
            if (campo[0] != null) {
                String codCat = campo[0];
                String nomeCat = campo[1];
                String Cnpj = campo[3];

                Optional<FornecedorDTO> fornecedorOptional = Optional.ofNullable(fornecedorService.findByCnpj(
                        Cnpj.replaceAll("\\D", "")));
                Optional<Categoria> categoriaProdutoExisteOptional = this.iCategoriaRepository.findByCodCategoria(campo[0]);

                //confere se existe se nao ele inseri
                if (!(categoriaProdutoExisteOptional.isPresent()) && fornecedorOptional.isPresent()) {
                    CategoriaDTO categoriaDTO = new CategoriaDTO(
                            null,
                            nomeCat,
                            fornecedorOptional.get().getId(),
                            codCat.substring(7, 10)
                    );
                    this.save(categoriaDTO);
                }
            }
        }
    }


    //Salva Categoria no Database
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        this.validate(categoriaDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Categoria");
        LOGGER.debug("br.com.hbsis.Categoria: {}", categoriaDTO);

        Categoria categoria = this.setCategoria(categoriaDTO);

        Categoria save = this.iCategoriaRepository.save(categoria);
        //Retorna para o postman
        return CategoriaDTO.of(save);
    }

    // valida os dados
    private void validate(CategoriaDTO categoriaDTO) {
        LOGGER.info("Validando Categoria");

        if (categoriaDTO == null) {
            throw new IllegalArgumentException("CategoriaDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(categoriaDTO.getNomeCategoria())) {
            throw new IllegalArgumentException("Nome da categoria não deve ser nula/vazia");
        }
        if (categoriaDTO.getIdCategoriaFornecedor() == null) {
            throw new IllegalArgumentException("id do fornecedor nao pode ser nulo");
        }
        if (categoriaDTO.getCodigo() == null) {
            throw new IllegalArgumentException("Numero nao pode ser nulo");
        }
        if (StringUtils.isBlank(categoriaDTO.getCodigo())) {
            throw new IllegalArgumentException("Numero nao pode ser maior que 3");
        }
    }

    // altera as informaçoes da categoria
    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id) {
        Optional<Categoria> CategoriaExistencialOpcional = this.iCategoriaRepository.findById(id);
        if (CategoriaExistencialOpcional.isPresent()) {
            Categoria categoriaFind = CategoriaExistencialOpcional.get();

            LOGGER.info("Atualizando Categoria... id: [{}]", categoriaFind.getId());
            LOGGER.debug("Payload: {}", categoriaDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaFind);

            Categoria categoria = this.setCategoria(categoriaDTO, categoriaFind);

            this.iCategoriaRepository.save(categoria);

            return CategoriaDTO.of(categoria);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    // deleta a categoria
    public void delete(Long id) {
        LOGGER.info("Executando delete para Categoria de ID: [{}]", id);

        this.iCategoriaRepository.deleteById(id);
    }
}
