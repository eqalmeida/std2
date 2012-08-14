/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import model.Cliente;
import model.ClienteLazyList;
import model.Pedido;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import repo.ClienteJpaController;
import repo.PedidoJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name="clienteMB")
@javax.faces.bean.ViewScoped
public class ClienteBean extends ControllerBase implements Serializable {

    private Cliente selected = null;
    private ClienteJpaController service = null;
    private LazyDataModel<Cliente> lazyList = null;
    private List<Pedido> pedidos = null;

    public Cliente getSelected() {
        return selected;
    }

    public void setSelected(Cliente selected) {
        this.selected = selected;
    }

    @PostConstruct
    private void init(){
            service = new ClienteJpaController();
    }

    public void gravar() {
        try {
            if (selected.getId() > 0) {
                service.edit(selected);
            } else {
                service.create(selected);
            }
            
            RequestContext.getCurrentInstance().execute("cliForm.hide()");
        }
        catch(Exception ex) {
            addMessage(ex.getMessage());
        }
    }

    public void verPedido(Integer id){

            String red = "ShowPedido.jsf?pedidoId=" +id;
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(red);
            } catch (IOException ex) {
                addErrorMessage(ex.getMessage());
            }

    }

    

    public void onRowSelect() {
    
        pedidos = null;
        
        if(selected != null){
            pedidos = new ClienteJpaController().findPedidos(selected);
        }
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void novo() {
        selected = new Cliente();
    }
    
    public void exibirClientes(){
    }
    
    public void excluir() {
        try {
            service.destroy(this.selected.getId());
            this.selected = new Cliente();
        } catch (Exception ex) {
            addMessage("Não foi possível excluir este cliente!");
        }
    }
    
    public void editar(){
        selected = service.findCliente(selected.getId());
    }

    public ClienteBean() {
    }


    /*
     * Adiciona cliente ao pedido na session
     */
    public void addToPedido(){
        try {
            
            FacesContext context = FacesContext.getCurrentInstance();
            
            PedidoBean pedidoMB = context.getApplication().evaluateExpressionGet(context, "#{pedidoMB}", PedidoBean.class);
            
            pedidoMB.setCliente(selected);
            
            addMessage("Cliente selecionado!");
        } catch (Exception ex) {
            addMessage("Não foi possível selecionar!\n" + ex.getMessage());
        }
    }

    public LazyDataModel<Cliente> getLazyList() {
        if(lazyList == null) {
            lazyList = new ClienteLazyList();
        }
        return lazyList;
    }

    public void setLazyList(LazyDataModel<Cliente> lazyList) {
        this.lazyList = lazyList;
    }
    
    
}
