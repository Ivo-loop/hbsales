package br.com.hbsis.fornecedor;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class FornecedorValidate {
    private final static Logger LOGGER = LoggerFactory.getLogger(Fornecedor.class);
    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private static int calcularDigito(String cnpj) {
        int soma = 0;
        for (int indice = cnpj.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(cnpj.substring(indice, indice + 1));
            soma += digito * FornecedorValidate.pesoCNPJ[(FornecedorValidate.pesoCNPJ.length - cnpj.length()) + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    private static boolean isValidCNPJ(String cnpj) {
        String cnpjValidate = cnpj.trim().substring(0,12);

        int digito1 = calcularDigito(cnpjValidate);
        int digito2 = calcularDigito(cnpjValidate + digito1);
        return !cnpj.equals(cnpj.substring(0, 12) + (digito1) + (digito2));
    }

    //valida as informacoes
    void validate(FornecedorDTO fornecedorDTO) {
        LOGGER.info("Validando Fornecedor");

        if (fornecedorDTO == null) {
            throw new IllegalArgumentException("fonecedoresDTO não deve ser nulo");
        }

        if (StringUtils.isEmpty(fornecedorDTO.getRazao())) {
            throw new IllegalArgumentException("Razao não deve ser nulo/vazio");
        }

        if (fornecedorDTO.getCnpj() == null) {
            throw new IllegalArgumentException("Cnpj não deve ser nula/vazia");
        }

        if (fornecedorDTO.getCnpj().length() != 14) {
            throw new IllegalArgumentException("Cnpj diferente que 14");
        }

        if (isValidCNPJ(fornecedorDTO.getCnpj())) {
            throw new IllegalArgumentException("CNPJ invalido, vou chamar a policia seu demonho");
        }

        if (StringUtils.isEmpty(fornecedorDTO.getEmail())) {
            throw new IllegalArgumentException("email não deve ser nula/vazia");
        }

        if (StringUtils.isEmpty(fornecedorDTO.getEndereco())) {
            throw new IllegalArgumentException("endereço não deve ser nulo/vazio");
        }

        if (StringUtils.isEmpty(fornecedorDTO.getNomeFan())) {
            throw new IllegalArgumentException("nome fantasia não deve ser nula/vazia");
        }

        if (fornecedorDTO.getTelefone() == null) {
            throw new IllegalArgumentException("telefone não deve ser nulo/vazio");
        }

        String cont2 = String.valueOf(fornecedorDTO.getTelefone());
        if (cont2.length() != 13) {
            throw new IllegalArgumentException("telefone diferente que 13 digitos, confira se possui DDD e DDI");
        }

        if (fornecedorDTO.getEmail().length() > 50) {
            throw new IllegalArgumentException("email muito grande sinto muito");
        }
    }
}
