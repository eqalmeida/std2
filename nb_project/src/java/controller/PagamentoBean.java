/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import model.Boleto;
import model.PagtoRecebido;
import model.PedidoPag;
import model.Usuario;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
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
    private Boleto boletoSel = null;
    private BigDecimal valorDevido = null;
    private Date data = null;
    private BigDecimal valorRecebido = null;

    @PostConstruct
    private void inicializa() {
        pagService = new PedidoPagJpaController();
    }

    /**
     * Creates a new instance of PagamentoBean
     */
    public PagamentoBean() {
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
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

    public void dataChanged(ValueChangeEvent ev) {

        data = (Date) ev.getNewValue();

        if (boletoSel != null) {
            valorDevido = boletoSel.getValorDevido(data);
        }

    }

    public Boleto getBoletoSel() {
        return boletoSel;
    }

    public void setBoletoSel(Boleto boletoSel) {
        this.boletoSel = boletoSel;
    }

    public void novoPagamento() {

        boletoSel = null;

        for (Boleto b : pedidoPag.getParcelas()) {
            if (b.getStatus() == Boleto.ATIVO || b.getStatus() == Boleto.PAGO_PARCIAL) {
                boletoSel = b;
                break;
            }
        }
        if (boletoSel != null) {
            data = new Date();
            valorDevido = boletoSel.getValorDevido(data);
            valorRecebido = null;
            RequestContext.getCurrentInstance().execute("pagDialog.show()");

        } else {
            addMessage("Nenhum boleto a ser pago!");
        }
    }

    public void registrar() {
        if (boletoSel != null) {

            EntityManager em = ControllerBase.getEmf().createEntityManager();

            try {
                Usuario usuario = getUsuarioLogado();
                boletoSel.regPagamento(valorRecebido, data, usuario);

                em.getTransaction().begin();
                em.merge(boletoSel);
                em.getTransaction().commit();

                pedidoPag = null;

                RequestContext.getCurrentInstance().execute("pagDialog.hide()");
                addMessage("Pagamento Registrado");
            } catch (Exception ex) {
                addErrorMessage(ex.getMessage());
            } finally {
                em.close();
            }
        }
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getValorDevido() {
        return valorDevido;
    }
}
