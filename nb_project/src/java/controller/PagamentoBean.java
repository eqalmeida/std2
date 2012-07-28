/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.persistence.EntityManager;
import model.Boleto;
import model.PagtoRecebido;
import model.PedidoPag;
import model.Usuario;
import org.primefaces.context.RequestContext;
import repo.PedidoPagJpaController;
import util.Util;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "pagMB")
@ViewScoped
public class PagamentoBean extends ControllerBase implements Serializable {

    private PedidoPag pedidoPag = null;
    private Integer pagId = null;
    private PedidoPagJpaController pagService = null;
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

    public void dataChanged(ValueChangeEvent ev) {

        data = (Date) ev.getNewValue();

        valorDevido = pedidoPag.getValorDevidoAtual(data);
    }

    public void novoPagamento() {
        
        Usuario user = getUsuarioLogado();
        
        if(user == null){
            addErrorMessage("Falha de Login!");
            return;
        }


        data = new Date();
        valorDevido = pedidoPag.getValorDevidoAtual(data);
        valorRecebido = null;
        RequestContext.getCurrentInstance().execute("pagDialog.show()");

    }

    public void registrar() {

        Usuario user = getUsuarioLogado();
        
        if(user == null){
            addErrorMessage("Falha de Login!");
            return;
        }

        EntityManager em = ControllerBase.getEmf().createEntityManager();

        try {

            /*
             * Soma as Parcelas não Pagas
             */
            BigDecimal soma = BigDecimal.ZERO;
            for (Boleto b : pedidoPag.getParcelas()) {
                soma = soma.add(b.getValorAtualComTaxas(data));
            }

            if (valorRecebido.doubleValue() > soma.doubleValue()) {
                throw new Exception("O valor informado é superior ao devido!");
            }

            /*
             * Verifica se não existe pagamento anterior
             */
            long dataInfL = Util.dateToLong(data);
            long hoje = Util.dateToLong(new Date());

            if (dataInfL > hoje) {
                throw new Exception("Data inválida (futura)!");
            }

            for (PagtoRecebido pr : pedidoPag.getPagamentos()) {
                long dataL = Util.dateToLong(pr.getDataInformada());
                if (dataL > dataInfL) {
                    throw new Exception("Já existe um pagamento registrado em data posterior!");
                }
            }

            em.getTransaction().begin();

            Usuario usuario = getUsuarioLogado();

            /*
             * Registra os pagamentos por Boleto
             */
            BigDecimal sobra = valorRecebido;
            for (Boleto b : pedidoPag.getParcelas()) {
                sobra = b.regPagamento(sobra, data);
                em.merge(b);

                if (sobra.doubleValue() <= 0.00) {
                    break;
                }
            }

            PagtoRecebido pagamento = new PagtoRecebido();
            pagamento.setData(new Date());
            pagamento.setDataInformada(data);
            pagamento.setPedidoPag(pedidoPag);
            pagamento.setRecebUsuario(usuario);
            pagamento.setValor(valorRecebido);

            em.persist(pagamento);

            if (pedidoPag.getPagamentos() == null) {
                pedidoPag.setPagamentos(new ArrayList<PagtoRecebido>());
            }

            pedidoPag.getPagamentos().add(pagamento);

            em.merge(pedidoPag);

            em.getTransaction().commit();

            RequestContext.getCurrentInstance().execute("pagDialog.hide()");

            addMessage("Pagamento Registrado");
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            addErrorMessage(ex.getMessage());
        } finally {
            pedidoPag = null;
            em.close();
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
