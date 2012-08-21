/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import util.IPagtoService;
import util.PagtoServiceFactory;
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
    private BigDecimal totalJuros = null;
    private BigDecimal totalMulta = null;
    private BigDecimal totalParcela = null;
    private BigDecimal taxas = null;
    private BigDecimal descontoVal = null;
    private double desconto = 0.0;

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

        boolean first;

        if (pedidoPag == null) {
            first = true;
        } else {
            first = false;
        }
//        if (pedidoPag == null) {

        try {
            pedidoPag = pagService.findPedidoPag(pagId);
            if (first) {
                novoPagamento();
                updateTotais(data);
            }
        } catch (Exception ex) {
        }
//        }
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

        updateTotais(data);

    }

    private void updateTotais(Date d) {

        totalJuros = BigDecimal.ZERO;
        totalMulta = BigDecimal.ZERO;
        totalParcela = BigDecimal.ZERO;

        for (Boleto b : getPedidoPag().getParcelas()) {

            if (b.isAtrasado(d)) {
                totalJuros = totalJuros.add(b.getJuros(d));
                totalMulta = totalMulta.add(b.getMulta(d));
                totalParcela = totalParcela.add(b.getValor());
            }

        }
        
        if(!getUsuarioLogado().isAdmin()){
            desconto = 0.0;
        }

        taxas = totalJuros.add(totalMulta);
        descontoVal = taxas.multiply(new BigDecimal(desconto)).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
        valorDevido = taxas.add(totalParcela).subtract(descontoVal);

    }

    public void novoPagamento() {

        Usuario user = getUsuarioLogado();

        if (user == null) {
            addErrorMessage("Falha de Login!");
            return;
        }


        data = new Date();
        updateTotais(data);
        valorRecebido = null;
        desconto = 0.0;
    }

    public void editarDesconto() {
        if (getUsuarioLogado().isAdmin()) {
            RequestContext.getCurrentInstance().execute("descDlg.show()");
        }
    }

    public void gravaDesconto() {
        if (!getUsuarioLogado().isAdmin()) {
            desconto = 0.0;
        }
        updateTotais(data);
        RequestContext.getCurrentInstance().execute("descDlg.hide()");
    }

    public void registrar() {

        Usuario user = getUsuarioLogado();

        IPagtoService pService = PagtoServiceFactory.getPagtoService();

        if (user == null) {
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
                //sobra = b.regPagamento(sobra, data);
                pService.setBoleto(b);
                sobra = pService.regPagto(sobra, data);
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

            novoPagamento();

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

    public BigDecimal getTotalJuros() {
        return totalJuros;
    }

    public BigDecimal getTotalMulta() {
        return totalMulta;
    }

    public BigDecimal getTotalParcela() {
        return totalParcela;
    }

    public BigDecimal getTaxas() {
        return taxas;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getDescontoVal() {
        return descontoVal;
    }

}
