package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.PagtoRecebido;
import repo.PagtoRecebidoJpaController;

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
        pag = ctl.find(pagId);
        return pag;
    }

    public void printRecibo(int id) {
        showPopup("PrintRecibo.jsf?pagId=" + id);
//        RequestContext.getCurrentInstance().execute("jan=window.open (\"PrintRecibo.jsf?pagId="+id+"\",\"mywindow\",\"height=600, width=800\");");

    }
}