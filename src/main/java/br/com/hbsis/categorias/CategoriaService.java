package br.com.hbsis.categorias;

import br.com.hbsis.fornecedor.FornecedorService;
import br.com.hbsis.fornecedor.FornecedoresDTO;
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
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoriaService.class);
    private final ICategoriaRepository iCategoriaRepository;
    private final FornecedorService fornecedorService;

    @Autowired
    public CategoriaService(ICategoriaRepository iCategoriaRepository, FornecedorService fornecedorService) {
        this.iCategoriaRepository = iCategoriaRepository;
        this.fornecedorService = fornecedorService;
    }

    //busca toda categoria
    public List<Categoria> findAll() {
        return iCategoriaRepository.findAll();
    }

    //Busca
    public List findAllByFornecedor_IdIs(Long id) {
        List<Categoria> categoriaOpcional = this.iCategoriaRepository.findAllByFornecedor_IdIs(id);

        if (!categoriaOpcional.isEmpty()) {
            return categoriaOpcional;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //busca a Categoria pelo Id, retorna ele como DTO
    public CategoriaDTO findById(Long id) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findById(id);

        if (categoriaOpcional.isPresent()) {
            return CategoriaDTO.of(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    public CategoriaDTO findByIdPost(Long id) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findById(id);

        if (categoriaOpcional.isPresent()) {
            return CategoriaDTO.ofPOST(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //busca a Categoria pelo Id, retorna categoria
    public Categoria findByCategoriaId(Long id) {
        Optional<Categoria> categoriaOptional = this.iCategoriaRepository.findById(id);

        if (categoriaOptional.isPresent()) {
            return categoriaOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //Busca pelo cod da categoria
    public Optional<Categoria> findByCodCategoriaOptinal(String cod) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findByCodCategoria(cod);

        if (categoriaOpcional.isPresent()) {
            return categoriaOpcional;
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", cod));
    }

    //Busca pelo cod da categoria
    public CategoriaDTO findByCodCategoria(String cod) {
        Optional<Categoria> categoriaOpcional = this.iCategoriaRepository.findByCodCategoria(cod);

        if (categoriaOpcional.isPresent()) {
            return CategoriaDTO.of(categoriaOpcional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", cod));
    }

    //Faz a exportaçao do banco em csv
    public void exportCSV(HttpServletResponse response) throws IOException, ParseException {

        //seta o nome do arq
        String categoriaCSV = "categoria.csv";

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
        String header = " Código da categoria ; Nome da categoria ; Razão social ; CNPJ";

        // escreve o cabeçario
        printWriter.println(header);
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
    public void importCSV(MultipartFile importCategoria) {

        String arquivo = "";
        String separator = ";";
        //achado na net
        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(importCategoria.getInputStream()))) {
            //para pular a primeira linha
            arquivo = leitor.readLine();
            //le as linhas
            while ((arquivo = leitor.readLine()) != null) {
                String[] categoriaCSV = arquivo.split(separator);
                //"\\D" so mantem os digitos de 0-9
                Optional<FornecedoresDTO> fornecedorOptional = Optional.ofNullable(fornecedorService.findByCnpj(
                        categoriaCSV[3].replaceAll("\\D", "")));
                Optional<Categoria> categoriaProdutoExisteOptional = this.iCategoriaRepository.findByCodCategoria(categoriaCSV[0]);

                //confere se existe se nao ele inseri
                if (!(categoriaProdutoExisteOptional.isPresent()) && fornecedorOptional.isPresent()) {
                    CategoriaDTO categoria = new CategoriaDTO();
                    categoria.setNomeCategoria(categoriaCSV[1]);
                    categoria.setCodigo(categoriaCSV[0].substring(7, 10));
                    FornecedoresDTO fornecedor = fornecedorService.findByCnpj(categoriaCSV[3].replaceAll("\\D", ""));
                    categoria.setIdCategoriaFornecedor(fornecedor.getId());
                    this.save(categoria);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Salva Categoria no Database
    public CategoriaDTO save(CategoriaDTO categoriaDTO) {
        this.validate(categoriaDTO);

        LOGGER.info("\"Salvando br.com.hbsis.Categoria");
        LOGGER.debug("br.com.hbsis.Categoria: {}", categoriaDTO);

        Categoria categoria = new Categoria();

        String cont = String.valueOf(categoriaDTO.getCodigo());

        for (; cont.length() < 3; ) {
            cont = "0" + cont;
        }

        categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
        categoria.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getIdCategoriaFornecedor()));
        categoria.setCodCategoria("CAT" + categoria.getFornecedor().getCnpj().substring(10, 14) + cont);
        System.out.println(categoria.getCodCategoria());

        Categoria save = this.iCategoriaRepository.save(categoria);
        //Retorna para o postman
        return CategoriaDTO.ofPOST(save);
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
    public CategoriaDTO update(CategoriaDTO categoriaDTO, Long id)  {
        Optional<Categoria> CategoriaExistencialOpcional = this.iCategoriaRepository.findById(id);
        if (CategoriaExistencialOpcional.isPresent()) {
            Categoria categoriaExistente = CategoriaExistencialOpcional.get();

            String cont = String.valueOf(categoriaDTO.getCodigo());
            for (; cont.length() < 3; ) {
                cont = "0" + cont;
            }

            LOGGER.info("Atualizando Categoria... id: [{}]", categoriaExistente.getId());
            LOGGER.debug("Payload: {}", categoriaDTO);
            LOGGER.debug("Categoria Existente: {}", categoriaExistente);

            categoriaExistente.setNomeCategoria(categoriaDTO.getNomeCategoria());
            categoriaExistente.setFornecedor(fornecedorService.findByFornecedorId(categoriaDTO.getIdCategoriaFornecedor()));
            categoriaExistente.setCodCategoria("CAT" + categoriaExistente.getFornecedor().getCnpj().substring(10, 14) + cont);

            this.iCategoriaRepository.save(categoriaExistente);

            return CategoriaDTO.ofPOST(categoriaExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    // deleta a categoria
    public void delete(Long id) {
        LOGGER.info("Executando delete para Categoria de ID: [{}]", id);

        this.iCategoriaRepository.deleteById(id);
    }
}
