/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.Pedido;
import model.PedidoProduto;
import model.Produto;
import repo.PedidoJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "printAutoMB")
@RequestScoped
public class PrintAutorizacaoBean extends ControllerBase implements Serializable{

    private Integer pedidoId;
    private Pedido pedido;
    private String texto;
    private PedidoJpaController service = null;
    
    @PostConstruct
    private void init(){
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
        if(pedidoId != null){
            pedido = service.findPedido(pedidoId);
            
            texto = "<img src=\"/std_loja/resources/img/logo_rogerinho_pq.jpeg\" />";
            texto += "<h1>AUTORIZAÇÃO</h1>";
            texto += ("Eu, "+pedido.getCliente().getNome() + " RG: <b>" + pedido.getCliente().getCpf()+"</b>");
            
        }
        return pedido;
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }
    
    public void mostrarTexto(){
        System.out.println(texto);
    }
}
