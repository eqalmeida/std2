/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import model.Cliente;
import model.ClienteLazyList;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import repo.ClienteJpaController;

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

    public Cliente getSelected() {
        if(selected == null){
            selected = new Cliente();
        }
        return selected;
    }

    public void setSelected(Cliente selected) {
        this.selected = selected;
    }


    public void gravar() {
        try {
            if (selected.getId() > 0) {
                getService().edit(selected);
            } else {
                getService().create(selected);
            }
            
            RequestContext.getCurrentInstance().execute("cliForm.hide()");
        }
        catch(Exception ex) {
            addMessage(ex.getMessage());
        }
    }


    public void onRowSelect(SelectEvent event) {
    
        selected = getService().findCliente(((Cliente) event.getObject()).getId()); 
    }

    public void novo() {
        selected = new Cliente();
    }
    
    public void exibirClientes(){
    }
    
    public void excluir() {
        try {
            getService().destroy(this.selected.getId());
            this.selected = new Cliente();
        } catch (Exception ex) {
            addMessage("Não foi possível excluir este cliente!");
        }
    }
    
    public void editar(){
        selected = getService().findCliente(selected.getId());
    }

    public ClienteBean() {
    }

    public ClienteJpaController getService() {
        if(service == null){
            service = new ClienteJpaController();
        }
        return service;
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
