package br.com.hbsis.Tools.mail;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.vendas.VendasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class Mail {

    private static final Logger LOGGER = LoggerFactory.getLogger(Mail.class);
    private final JavaMailSender mailSender;
    private final VendasService vendasService;

    public Mail(JavaMailSender mailSender, VendasService vendasService) {
        this.mailSender = mailSender;
        this.vendasService = vendasService;
    }

    public void mailSave(Pedido pedido) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Confirmaçao de compra");

        message.setText("Olá "
                + (pedido.getFuncionario().getNomeFuncionario()) + "\r\n"
                + "(Código do pedido; " + pedido.getCodPedido() + "    Data prevista para retirada do produto : ("
                + vendasService.dates(pedido.getFornecedor().getId()).getDiaRetirada() + ")" + "\r\n"
                + "HBSIS - Soluções em TI" + "\r\n"
                + "Rua Theodoro Holtrup, 982 - Vila Nova, Blumenau - SC"
                + "(47) 2123-5400");

        message.setTo(pedido.getFuncionario().getEmail());
        message.setFrom("ivopaulo.puehler@gmail.com");

        LOGGER.info("Enviando mail");
        try {
            mailSender.send(message);
            LOGGER.info("Enviado");
        } catch (Exception e) {
            LOGGER.debug("email deu ruim", e);
        }
    }
}

