/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import model.Boleto;
import model.PagtoRecebido;
import model.Pedido;
import model.PedidoLazyList;
import model.PedidoPag;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import repo.BoletoJpaController;
import repo.PedidoJpaController;
import util.Util;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "showPedidoMB")
@ViewScoped
public class ShowPedidoBean extends ControllerBase {
    
    private Integer pedidoId;
    private Pedido pedido = null;
    private PedidoJpaController service = null;
    private LazyDataModel<Pedido> lista = null;
    private Integer ano = null;
    private Integer mes = null;
    private Boleto boleto = null;
    private PagtoRecebido pagamento = null;
    private PedidoPag pedidoPagSelected = null;
    private short status;
    private String justif;
    
    public short getStatus() {
        return status;
    }
    
    public void setStatus(short status) {
        this.status = status;
    }
    
    public String getJustif() {
        return justif;
    }
    
    public void setJustif(String justif) {
        this.justif = justif;
    }
    
    public void gravaStatus() {
        
        if(pedido != null){
        
        try {
            int id = pedido.getId();
            
            service.changeStatus(pedido, status, getUsuarioLogado(), justif);
            
            pedido = getService().findPedido(id);

//            pedido = null;
            RequestContext.getCurrentInstance().execute("statusDlg.hide()");
            
            addMessage("Status alterado");
            
        } catch (Exception e) {
            addErrorMessage(e.getMessage());
        }
        }
        
    }
    
    public Integer getAno() {
        return ano;
    }
    
    public void setAno(Integer ano) {
        this.ano = ano;
    }
    
    public Integer getMes() {
        return mes;
    }
    
    public String getMesStr(){
        return Util.monthNames.get(this.mes);
    }
    
    public void setMes(Integer mes) {
        this.mes = mes;
    }

    /**
     * Creates a new instance of ShowPedidoBean
     */
    public ShowPedidoBean() {
        Calendar data = Calendar.getInstance();
        ano = data.get(Calendar.YEAR);
        mes = data.get(Calendar.MONTH);
    }
    
    public PedidoJpaController getService() {
        if (service == null) {
            service = new PedidoJpaController();
        }
        return service;
    }
    
    public Integer getPedidoId() {
        return pedidoId;
    }
    
    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }
    
    public Pedido getPedido() {
        if (pedido == null) {
            if (pedidoId != null) {
                pedido = getService().findPedido(pedidoId);
            }
        }
        return pedido;
    }
    
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    public LazyDataModel<Pedido> getLista() {
        if (lista == null) {
            lista = new PedidoLazyList(getService());
            PedidoLazyList p = (PedidoLazyList) lista;
            p.setAno(ano);
            p.setMes(mes);
        }
        return lista;
    }
    
    public void setLista(LazyDataModel<Pedido> lista) {
        this.lista = lista;
    }
    
    public void mesChanged(ValueChangeEvent ev) {
        mes = (Integer) ev.getNewValue();
        PedidoLazyList p = (PedidoLazyList) getLista();
        p.setMes(mes);
    }
    
    public void anoChanged(ValueChangeEvent ev) {
        ano = (Integer) ev.getNewValue();
        PedidoLazyList p = (PedidoLazyList) getLista();
        p.setAno(ano);
    }
    
    public void verPedido() {
    }
    
    public void onRowSelected(SelectEvent ev) {
        Integer id = ((Pedido) ev.getObject()).getId();
        pedido = getService().findPedido(id);
        if (pedido != null) {
            addMessage("Detalhes de Pedido abaixo");
        }
    }
    
    public void regPag() {
        
        if (pedidoPagSelected == null) {
            addErrorMessage("Identificação inválida!");
            return;
        }
        
        String red = "RegPagamento.jsf?id=" + pedidoPagSelected.getId();
        pedidoPagSelected = null;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(red);
        } catch (IOException ex) {
            addErrorMessage(ex.getMessage());
        }
        
    }
    
    public void printBoletos() {
        
        if (pedidoPagSelected == null) {
            addErrorMessage("Identificação inválida!");
            return;
        }
        
        String red = "PrintBoleto.jsf?id=" + pedidoPagSelected.getId();
        pedidoPagSelected = null;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(red);
        } catch (IOException ex) {
            addErrorMessage(ex.getMessage());
        }
        
    }
    
    public void changeStatus(short status) {
        justif = "";
        this.status = status;
        RequestContext.getCurrentInstance().execute("statusDlg.show()");
    }
    
    public PagtoRecebido getPagamento() {
        return pagamento;
    }
    
    public void setPagamento(PagtoRecebido pagamento) {
        this.pagamento = pagamento;
    }
    
    public Boleto getBoleto() {
        return boleto;
    }
    
    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }
    
    public PedidoPag getPedidoPagSelected() {
        return pedidoPagSelected;
    }
    
    public void setPedidoPagSelected(PedidoPag pedidoPagSelected) {
        this.pedidoPagSelected = pedidoPagSelected;
    }
}
