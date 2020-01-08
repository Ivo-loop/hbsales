package br.com.hbsis.linhas;

import br.com.hbsis.ExportImport.ExportCSV;
import br.com.hbsis.ExportImport.ImportCSV;
import br.com.hbsis.categorias.Categoria;
import br.com.hbsis.categorias.CategoriaDTO;
import br.com.hbsis.categorias.CategoriaService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Service
public class LinhasService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinhasService.class);
    private final ILinhasRepository iLinhasRepository;
    private final CategoriaService categoriaService;
    private final LinhasFind linhasFind;
    private final ExportCSV exportCSV;
    private final ImportCSV importCSV;

    @Autowired
    public LinhasService(ILinhasRepository iLinhasRepository, CategoriaService categoriaService, LinhasFind linhasFind, ExportCSV exportCSV, ImportCSV importCSV) {
        this.iLinhasRepository = iLinhasRepository;
        this.categoriaService = categoriaService;
        this.linhasFind = linhasFind;
        this.exportCSV = exportCSV;
        this.importCSV = importCSV;
    }

    //puxa o linhas pelo Id dela
    public Linhas findById(Long id) {
        return linhasFind.findById(id);
    }

    //puxa a linha pelo Id dele, retorna como DTO
    LinhasDTO findByIdDTO(Long id) {
        return linhasFind.findByIdDTO(id);
    }

    //puxa as linhas pelo cod dela
    public Optional<Linhas> findByCodLinhasOptional(String cod) {
        return linhasFind.findByCodLinhasOptional(cod);
    }

    //puxa as linhas pelo cod dela, retorna com DTO
    public LinhasDTO findByCodLinhasDTO(String cod) {
        return linhasFind.findByCodLinhasDTO(cod);
    }

    //Busca tudo
    public List<Linhas> findAll() {
        return linhasFind.findAll();
    }

    //Faz a exportacao do banco
    void exportCSV(HttpServletResponse response) throws IOException {

        String header = " Código da Linha ; Nome da Linha ; Código da Categoria ; Nome da Categoria";
        exportCSV.writerHeader(response, header, "Linhas");
        // escreve o cabeçario
        PrintWriter printWriter = response.getWriter();
        for (Linhas linhas : iLinhasRepository.findAll()) {
            String nome = linhas.getNomeLinhas();
            String cod = linhas.getCodLinhas();
            String codCategoria = linhas.getCategoria().getCodCategoria();
            String nomeCategoria = linhas.getCategoria().getNomeCategoria();
            //escreve os dados
            printWriter.println(cod + ";" + nome + ";" + codCategoria + ";" + nomeCategoria);
        }
        printWriter.close();
    }

    //Faz a importacao do banco
    void importCSV(MultipartFile importCategoria) {
        String[][] CSV = importCSV.leitorCSV(importCategoria);

        for (String[] campo : CSV) {

            if (campo[0] != null) {
                String codLinha = campo[0];
                String nomelinha = campo[1];
                String codCat = campo[2];
                Optional<CategoriaDTO> cateogriaOptional = Optional.ofNullable(categoriaService.findByCodCategoria(codCat));
                Optional<Linhas> linhaExisteOptional = this.iLinhasRepository.findByCodLinhas(codLinha);

                //confere se existe se nao ele inseri
                if (!(linhaExisteOptional.isPresent()) && cateogriaOptional.isPresent()) {
                    LinhasDTO linhasDTO = new LinhasDTO();
                    linhasDTO.setNomeLinhas(nomelinha);
                    linhasDTO.setCodLinhas(codLinha);
                    CategoriaDTO categoria = categoriaService.findByCodCategoria(codCat);
                    linhasDTO.setidLinhasCategoria(categoria.getId());
                    this.save(linhasDTO);
                }
            }
        }
    }

    // salva
    public LinhasDTO save(LinhasDTO linhasDTO) {
        this.validate(linhasDTO);

        LOGGER.info("Salvando br.com.hbsis.Linhas");
        LOGGER.debug("br.com.hbsis.Linhas: {}", linhasDTO);

        StringBuilder cont = new StringBuilder(String.valueOf(linhasDTO.getCodLinhas()));

        for (; cont.length() < 10; ) {
            cont.insert(0, "0");
        }

        Linhas linhas = new Linhas();
        linhas.setNomeLinhas(linhasDTO.getNomeLinhas());
        linhas.setCodLinhas(cont.toString().toUpperCase());
        Long id = linhasDTO.getidLinhasCategoria();
        Categoria byCategoriaId = categoriaService.findByCategoriaId(id);
        linhas.setCategoria(byCategoriaId);

        linhas = this.iLinhasRepository.save(linhas);

        //Retorna para o postman
        return LinhasDTO.of(linhas);
    }

    //valida
    private void validate(LinhasDTO linhasDTO) {
        LOGGER.info("Validando Linhas");

        if (linhasDTO == null) {
            throw new IllegalArgumentException("LinhasDTO não deve ser nulo");
        }
        if (StringUtils.isEmpty(linhasDTO.getNomeLinhas())) {
            throw new IllegalArgumentException("Nome da Linhas não deve ser nula/vazia");
        }
        if (StringUtils.isEmpty(linhasDTO.getCodLinhas())) {
            throw new IllegalArgumentException("Cod não deve ser nulo/vazio");
        }
        if (linhasDTO.getidLinhasCategoria() == null) {
            throw new IllegalArgumentException("Id Categoria nao deve ser nula/vazia");
        }
        if (linhasDTO.getCodLinhas().length() > 10) {
            throw new IllegalArgumentException("Cod maior que 10");
        }
    }

    // altera
    public LinhasDTO update(LinhasDTO linhasDTO, Long id) {
        Optional<Linhas> LinhasExistenteOptional = this.iLinhasRepository.findById(id);
        this.validate(linhasDTO);
        if (LinhasExistenteOptional.isPresent()) {
            Linhas LinhasExistente = LinhasExistenteOptional.get();

            LOGGER.info("Atualizando Linhas... id: [{}]", LinhasExistente.getId());
            LOGGER.debug("Payload: {}", linhasDTO);
            LOGGER.debug("linhas Existente: {}", LinhasExistente);

            LinhasExistente.setNomeLinhas(linhasDTO.getNomeLinhas());
            LinhasExistente.setCodLinhas(linhasDTO.getCodLinhas());
            LinhasExistente.setCategoria(categoriaService.findByCategoriaId(id));

            LinhasExistente = this.iLinhasRepository.save(LinhasExistente);

            return LinhasDTO.of(LinhasExistente);
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //deleta
    public void delete(Long id) {
        LOGGER.info("Executando delete para linhas de ID: [{}]", id);

        this.iLinhasRepository.deleteById(id);
    }
}