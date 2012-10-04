/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.Pedido;
import repo.ConfigJpaController;
import repo.PedidoJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "printAutoMB")
@RequestScoped
public class PrintAutorizacaoBean extends ControllerBase implements Serializable {

    private Integer pedidoId;
    private Pedido pedido;
    private String texto;
    private PedidoJpaController service = null;

    @PostConstruct
    private void init() {
        service = new PedidoJpaController();


    }

    public PrintAutorizacaoBean() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Pedido getPedido() {
        if (pedidoId != null) {
        }
        return pedido;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public void mostrarTexto() {
        pedido = service.find(pedidoId);

        if (pedido == null) {
            addErrorMessage("Pedido não encontrado");
            return;
        }

        if (pedido.getVeiculo() == null) {
            addErrorMessage("Pedido não possui veículo");
            return;
        }

        ConfigJpaController configService = new ConfigJpaController();

        texto = configService.findConfig().getCartaAutoriz();

        texto = texto.replaceAll("NomeCliente", pedido.getCliente().getNome());

        texto = texto.replaceAll("RGCliente", pedido.getCliente().getRg());

        texto = texto.replaceAll("CPFCliente", pedido.getCliente().getCpf());

        texto = texto.replaceAll("PlacaVeiculo", pedido.getVeiculo().getProduto().getPlaca());

        texto = texto.replaceAll("CorVeiculo", pedido.getVeiculo().getProduto().getCor());

        texto = texto.replaceAll("CombustivelVeiculo", pedido.getVeiculo().getProduto().getCombustivelStr());

        texto = texto.replaceAll("ChassiVeiculo", pedido.getVeiculo().getProduto().getChassi());

        texto = texto.replaceAll("AnoVeiculo", pedido.getVeiculo().getProduto().getAno());


        texto = texto.replaceAll("DataAtual", getDataAtual());

        texto = texto.replaceAll("ModeloVeiculo", pedido.getVeiculo().getProduto().getFabricante() + " " + pedido.getVeiculo().getProduto().getModelo());

    }

    private String getDataAtual() {
        Date data = new Date();
        SimpleDateFormat df = new SimpleDateFormat("d 'de' MMMMM 'de' yyyy");
        return df.format(data);
    }
}
