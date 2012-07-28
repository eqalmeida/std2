/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.PedidoPag;
import repo.PedidoPagJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "printBoletoMB")
@ViewScoped
public class PrintBoletoBean extends ControllerBase implements Serializable {

    private PedidoPag pedidoPag = null;
    private Integer pagId = null;
    private PedidoPagJpaController pagService = null;

    @PostConstruct
    private void inicializa() {
        pagService = new PedidoPagJpaController();
    }

    /**
     * Creates a new instance of PagamentoBean
     */
    public PrintBoletoBean() {
    }

    public PedidoPag getPedidoPag() {

        if (pedidoPag == null) {

            try {
                pedidoPag = pagService.findPedidoPag(pagId);
            } catch (Exception ex) {
            }
        }
        return pedidoPag;
    }

    public void setPedidoPag(PedidoPag pedidoPag) {
        this.pedidoPag = pedidoPag;
    }

    public Integer getPagId() {
        return pagId;
    }

    public void setPagId(Integer pagId) {
        this.pagId = pagId;
    }

}
