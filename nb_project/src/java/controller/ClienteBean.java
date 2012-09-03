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
    private boolean showForm = false;

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
            
            showForm = false;
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

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void novoCliente() {
        selected = new Cliente();
        showForm = true;
    }
    
    public void exibirClientes(){
    }
    
    public void excluirCliente(int id) {
        
        if(!getUsuarioLogado().isAdmin()){
            addErrorMessage("Acesso Negado!");
            return;
        }
        
        try {
            service.destroy(id);
            this.selected = new Cliente();
        } catch (Exception ex) {
            addErrorMessage("Não foi possível excluir este cliente!");
        }
    }
    
    public void editarCliente(int id){
        selected = service.findCliente(id);
        pedidos = null;
        
        if(selected != null){
            pedidos = new ClienteJpaController().findPedidos(selected);
        }
        showForm = true;
    }
    
    public void listarClientes(){
        showForm = false;
        pedidos = null;
    }

    public ClienteBean() {
    }


    /*
     * Adiciona cliente ao pedido na session
     */
    public void addToPedido(int id){
        try {
            selected = service.findCliente(id);
            
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

    public boolean isShowForm() {
        return showForm;
    }
    
    
}
