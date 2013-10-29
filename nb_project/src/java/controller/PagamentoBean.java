/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import model.Boleto;
import model.PagtoRecebido;
import model.Pedido;
import model.PedidoPag;
import model.Usuario;
import org.primefaces.context.RequestContext;
import repo.PedidoPagJpaController;
import util.Acrescimos;
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
    private Map<Integer, Boolean> checked;

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

    public void dataChanged() {
        updateTotais(data);
    }

    /**
     * Atualiza os valores de Juros, Multa, etc
     * @param d Data do Pagamento
     */
    private void updateTotais(Date d) {

        // Instancia da classe de acrescimos.
        Acrescimos acrescimos = new Acrescimos(d);
        
        long dataPagDays = Acrescimos.dateToDays(d);

        checked = new HashMap<Integer, Boolean>();
        
        // Adiciona parcelas para tratar acrescimos.
        for (Boleto b : getPedidoPag().getParcelas()) {
        
            checked.put(b.getId(), Boolean.FALSE);
            
            if(b.getStatus() == Boleto.CANCELADO || b.getStatus() == Boleto.PAGO){
                continue;
            }
            
            Date vencimento = b.getVencimentoAtual();
                        
            long dataVencDays = Acrescimos.dateToDays(vencimento);
            
            if (dataVencDays <= dataPagDays) {
                acrescimos.addicionaParcela(b);
                checked.remove(b.getId());
                checked.put(b.getId(), Boolean.TRUE);
            }
        }

        if (!getUsuarioLogado().isAdmin()) {
            desconto = 0.0;
        }
        
        acrescimos.setDesconto(desconto);

        totalJuros = acrescimos.getTotalJuros();
        totalMulta = acrescimos.getTotalMultas();
        totalParcela = acrescimos.getTotalValorParcelas();

        taxas = totalJuros.add(totalMulta);
        descontoVal = acrescimos.getDescontoVal();
        valorDevido = acrescimos.getTotalDevido();
    }

    public Map<Integer, Boolean> getChecked() {
        return checked;
    }
    
    

    /**
     * Prepara para adicionar novo pagamento
     */
    public void novoPagamento() {

        Usuario user = getUsuarioLogado();

        if (user == null) {
            addErrorMessage("Falha de Login!");
            return;
        }


        data = Calendar.getInstance().getTime();

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

        EntityManager em = ControllerBase.getEntityManager();

        try {

            /*
             * Soma as Parcelas não Pagas
             */
            Acrescimos acrescimos = new Acrescimos(data, pedidoPag);
            
            acrescimos.setDesconto(desconto);
            
            if (valorRecebido.doubleValue() > acrescimos.getTotalDevido().doubleValue()) {
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

                if (b.getValorDevido().doubleValue() > 0.0) {
                    //sobra = b.regPagamento(sobra, data);
                    pService.setBoleto(b);
                    pService.setDesconto(desconto);

                    sobra = pService.regPagto(sobra, data);
                    em.merge(b);

                    if (sobra.doubleValue() <= 0.00) {
                        break;
                    }
                }
            }

            if (sobra.doubleValue() > 0.0) {
                throw new Exception("O valor informado é maior que o devido!");
            }


            PagtoRecebido pagamento = new PagtoRecebido();
            pagamento.setData(new Date());
            pagamento.setDataInformada(data);
            pagamento.setPedidoPag(pedidoPag);
            pagamento.setRecebUsuario(usuario);
            pagamento.setValor(valorRecebido);
            pagamento.setDesconto(desconto);

            em.persist(pagamento);

            if (pedidoPag.getPagamentos() == null) {
                pedidoPag.setPagamentos(new ArrayList<PagtoRecebido>());
            }

            pedidoPag.getPagamentos().add(pagamento);

            em.merge(pedidoPag);

            em.getTransaction().commit();

            Pedido pedido = em.getReference(Pedido.class, pedidoPag.getPedido().getId());

            boolean quitado = true;

            //
            // Verifica se todas as parcelas estão pagas
            //
            for (PedidoPag pag : pedido.getPagamentos()) {
                for (Boleto b : pag.getParcelas()) {
                    if (b.getStatus() != Boleto.PAGO) {
                        quitado = false;
                        break;
                    }
                }
            }

            //
            // Define o status do Pedido
            //
            if (quitado) {
                em.getTransaction().begin();
                pedido.setStatus(Boleto.PAGO);
                em.merge(pedido);
                em.getTransaction().commit();
            }

            novoPagamento();

            addMessage("Pagamento Registrado");
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            addErrorMessage(ex.getMessage());
        } finally {
            pedidoPag = null;
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
