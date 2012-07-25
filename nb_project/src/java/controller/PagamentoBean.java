/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import model.PagtoRecebido;
import model.PedidoPag;
import repo.PedidoPagJpaController;
import util.Util;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "pagMB")
@ViewScoped
public class PagamentoBean extends ControllerBase {

    private PedidoPag pedidoPag = null;
    private Integer id = null;
    private PedidoPagJpaController pagService = null;
    private PagtoRecebido pagamento;

    @PostConstruct
    private void inicializa() {
        pagService = new PedidoPagJpaController();
    }

    /**
     * Creates a new instance of PagamentoBean
     */
    public PagamentoBean() {
    }

    public PedidoPag getPedidoPag() {
        if (pedidoPag == null) {
            try {
                pedidoPag = pagService.findPedidoPag(id);
            } catch (Exception ex) {
            }
        }
        return pedidoPag;
    }

    public void setPedidoPag(PedidoPag pedidoPag) {
        this.pedidoPag = pedidoPag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PagtoRecebido getPagamento() {
        /*
        if(pagamento == null){
            pagamento = new PagtoRecebido();
            pagamento.setPedidoPag(getPedidoPag());
            pagamento.setDataInformada(new Date());
            pagamento.calculaValorDevido();
        }
        */ 
        return pagamento;
    }

    public void setPagamento(PagtoRecebido pagamento) {
        this.pagamento = pagamento;
    }
    
    public void dataChanged(ValueChangeEvent ev){
/*        
        Date data = (Date) ev.getNewValue();
        
        pedidoPag = null;
        
        pagamento.setPedidoPag(getPedidoPag());
        
        pagamento.setDataInformada(data);
        pagamento.calculaValorDevido();
  
  */
    }

}
