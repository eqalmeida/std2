/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.PagtoRecebido;
import model.PedidoPag;
import org.primefaces.context.RequestContext;
import repo.PagtoRecebidoJpaController;
import repo.PedidoPagJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "printReciboMB")
@RequestScoped
public class PrintReciboBean extends ControllerBase {

    private PagtoRecebido pag;
    private int pagId;

    public PrintReciboBean() {
    }

    public int getPagId() {
        return pagId;
    }

    public void setPagId(int pagId) {
        this.pagId = pagId;
    }

    public PagtoRecebido getPag() {
        PagtoRecebidoJpaController ctl = new PagtoRecebidoJpaController();
        pag = ctl.findPagtoRecebido(pagId);
        return pag;
    }


    public void printRecibo(int id) {
        RequestContext.getCurrentInstance().execute("jan=window.open (\"PrintRecibo.jsf?pagId="+id+"\",\"mywindow\");");

    }
}