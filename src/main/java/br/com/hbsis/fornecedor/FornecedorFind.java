package br.com.hbsis.fornecedor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
class FornecedorFind {
    private final IFornecedorRepository iFonecedoresRepository;

    public FornecedorFind(IFornecedorRepository iFonecedoresRepository) {
        this.iFonecedoresRepository = iFonecedoresRepository;
    }

    //puxa o fornecedor pelo Id dele
    Fornecedor findFornecedorById(Long id) {

        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional.get();
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa o fornecedor pelo Id dele, retorna ele como DTO
    FornecedorDTO findFornecedorDTOById(Long id) {

        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findById(id);

        if (fornecedorOptional.isPresent()) {
            return FornecedorDTO.of(fornecedorOptional.get());
        }
        throw new IllegalArgumentException(String.format("ID %s não existe", id));
    }

    //puxa o fornecedor pelo Cnpj dele
    FornecedorDTO findByCnpj(String cnpj) {

        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findByCnpj(cnpj);

        if (fornecedorOptional.isPresent()) {
            return FornecedorDTO.of(fornecedorOptional.get());
        }
        throw new IllegalArgumentException(String.format("Nao existe Fornecedor com o Cnpj: %s não existe", cnpj));
    }

    //puxa o fornecedor pelo Cnpj dele, retorna ele como Optional
    Optional<Fornecedor> findByCnpjOptional(String cnpj) {

        Optional<Fornecedor> fornecedorOptional = this.iFonecedoresRepository.findByCnpj(cnpj);

        if (fornecedorOptional.isPresent()) {
            return fornecedorOptional;
        }
        throw new IllegalArgumentException(String.format("Cnpj %s não existe", cnpj));
    }

    //busca tudo
    public List<Fornecedor> findAll() {
        return iFonecedoresRepository.findAll();
    }

}
