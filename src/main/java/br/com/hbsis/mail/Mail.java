package br.com.hbsis.mail;

import br.com.hbsis.pedido.Pedido;
import br.com.hbsis.pedido.itens.ItemBusca;
import br.com.hbsis.pedido.itens.Itens;
import br.com.hbsis.vendas.VendasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mail {


    @Autowired
    private final JavaMailSender mailSender;
    private final VendasService vendasService;
    private final ItemBusca itemBusca;

    public Mail(JavaMailSender mailSender, VendasService vendasService, ItemBusca itemBusca) {
        this.mailSender = mailSender;
        this.vendasService = vendasService;
        this.itemBusca = itemBusca;
    }

    public void mailSave(Pedido pedido) {
        List<Itens> itens = itemBusca.findAllByPedido_IdIs(pedido.getId());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("O produto " + pedido.getFuncionario().getNomeFuncionario() + " foi comprado");
        message.setText("Olá "
                + (pedido.getFuncionario().getNomeFuncionario()) + "\r\n"
                + "Você fez a compra de : " + "\r\n"
                + itensPego(itens)
                + "(Código do pedido; " + pedido.getCodPedido() + "    Data prevista para retirada do produto : ("
                + vendasService.dates(pedido.getFornecedor().getId(), pedido.getDia()).getDiaRetirada() + ")" + "\r\n"
                + "HBSIS - Soluções em TI" + "\r\n"
                + "Rua Theodoro Holtrup, 982 - Vila Nova, Blumenau - SC"
                + "(47) 2123-5400");
        message.setTo("dannatomio@gmail.com");
        message.setFrom("ivopaulo.puehler@gmail.com");

        System.out.println("enviando email");
        try {
            mailSender.send(message);
            System.out.println("email enviado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList itensPego(List<Itens> itens) {
        ArrayList<String> mensagem = new ArrayList<>();
        for (Itens item : itens) {
            mensagem.add("" + item.getAmount() + " " + item.getProdutos().getNomeProduto() + "\r\n");
        }
        return mensagem;
    }
}

