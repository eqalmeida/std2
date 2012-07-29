/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import model.Boleto;
import model.Cliente;
import model.Coeficiente;
import model.CoeficientePK;
import model.Pedido;
import model.PedidoPag;
import model.PedidoProduto;
import model.PedidoProdutoPK;
import model.Produto;
import model.TabelaFinanc;
import model.TipoPagto;
import model.Usuario;
import org.primefaces.context.RequestContext;
import repo.CoeficienteJpaController;
import repo.PedidoJpaController;
import repo.TabelaFinancJpaController;
import repo.TipoPagtoJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "pedidoMB")
@SessionScoped
public class PedidoBean extends ControllerBase {

    private Pedido pedido = null;
//    private Produto produto = null;
    private PedidoProduto itemSelected = null;
    private List<PedidoProduto> items = null;
    private List<PedidoPag> pagamentos = null;
    private PedidoPag pagamento = null;
    private List<TipoPagto> tipoPagtoList = null;
    private List<TabelaFinanc> tabelaFinancList = null;
    private List<SelectItem> parcelaList = null;
    private Integer tipoPagtoSelected = null;
    private Integer tabelaId = 0;
    private Integer numParcelas;
    private BigDecimal valorParcela;

    /**
     * Creates a new instance of PedidoBean
     */
    public PedidoBean() {
    }

    public PedidoProduto getItemSelected() {
        if (itemSelected == null) {
            itemSelected = new PedidoProduto();
        }
        return itemSelected;
    }

    public void setItemSelected(PedidoProduto itemSelected) {
        this.itemSelected = itemSelected;
    }

    public void gravarItem() {

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(itemSelected)) {
                items.set(i, itemSelected);
                itemSelected = new PedidoProduto();
                RequestContext.getCurrentInstance().execute("itForm.hide()");
                break;
            }
        }

    }

    /**
     * Adiciona produto ao Pedido
     */
    public void addProduto(Produto p) throws Exception {

        itemSelected = new PedidoProduto();

        itemSelected.setProduto(p);

        itemSelected.setPedido(getPedido());

        PedidoProdutoPK pk = new PedidoProdutoPK(p.getId(), 0);

        itemSelected.setPedidoProdutoPK(pk);

        itemSelected.setQtd((short) 1);
        itemSelected.setValor(p.getValor());

        int id = getItems().indexOf(itemSelected);

        if (id != -1) {
            itemSelected.setQtd((short) (itemSelected.getQtd() + getItems().get(id).getQtd()));
            if (p.getQtdEstoque() != -1 && p.getQtdEstoque() < itemSelected.getQtd()) {
                throw new Exception("Produto não possui estoque");
            }
            getItems().remove(itemSelected);
        }

        if (p.getQtdEstoque() != -1 && p.getQtdEstoque() < itemSelected.getQtd()) {
            throw new Exception("Produto não possui estoque");
        }


        getItems().add(itemSelected);

        itemSelected = new PedidoProduto();
    }

    public Pedido getPedido() {
        if (pedido == null) {
            pedido = new Pedido();
        }
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public void setProduto(Produto produto) {
        this.getItemSelected().setProduto(produto);
    }

    public List<PedidoProduto> getItems() {
        if (items == null) {
            items = new ArrayList<PedidoProduto>();
        }
        return items;
    }

    public void setItems(List<PedidoProduto> items) {
        this.items = items;
    }

    public BigDecimal getValorTotalPedido() {

        BigDecimal valor = BigDecimal.ZERO;

        for (PedidoProduto p : getItems()) {

            valor = valor.add(p.getValorTotal());
        }
        return valor;
    }

    public void setCliente(Cliente c) {
        getPedido().setCliente(c);
    }

    public List<PedidoPag> getPagamentos() {
        if (pagamentos == null) {
            pagamentos = new ArrayList<PedidoPag>();
        }
        return pagamentos;
    }

    public void setPagamentos(List<PedidoPag> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public PedidoPag getPagamento() {
        if (pagamento == null) {
            pagamento = new PedidoPag();
        }
        return pagamento;
    }

    public void setPagamento(PedidoPag pagamento) {
        this.pagamento = pagamento;
    }

    private void populateTiposPag() {
        TipoPagtoJpaController c = new TipoPagtoJpaController();
        tipoPagtoList = c.findTipoPagtoEntities();
    }

    public void novoPag() {

        for (PedidoProduto p : items) {
            if (p.getValorTotal().compareTo(new BigDecimal(0.01)) < 0) {
                addErrorMessage("O produto " + p.getProduto().getDescricaoGeral() + " está com valor zero!");
                return;
            }
        }

        pagamento = new PedidoPag();

        tipoPagtoSelected = 0;
        tabelaFinancList = null;
        tabelaId = 0;
        parcelaList = null;
        numParcelas = 0;

        BigDecimal total = getValorFaltante();

        pagamento.setValor(total);

        int comp = total.compareTo(BigDecimal.ZERO);

        if (comp > 0) {
            populateTiposPag();

            RequestContext.getCurrentInstance().execute("pagForm.show()");
        } else if (comp == 0) {
            addMessage("O Pagamento já está definido!");
        } else {
            addMessage("Valor total do pedido inválido!");
        }

    }

    public BigDecimal getValorFaltante() {

        BigDecimal total = getValorTotalPedido();

        for (PedidoPag pg : pagamentos) {
            total = total.subtract(pg.getValor());
        }

        total = total.setScale(2);

        return total;
    }

    public void excluirPag() {
        int pagId = pagamento.getId();
        int size = getPagamentos().size();

        for (int i = 0; i < size; i++) {
            if (getPagamentos().get(i).getId() == pagId) {
                getPagamentos().remove(i);
                break;
            }
        }

        /*
         * Reenumera
         */
        PedidoPag pg;
        size--;
        for (int i = 0; i < size; i++) {
            pg = getPagamentos().get(i);
            pg.setId(i + 1);
            getPagamentos().set(i, pg);
        }
    }

    public void editarPag() {

        populateTiposPag();

        int pagId = pagamento.getId();

        for (PedidoPag p : getPagamentos()) {
            if (p.getId() == pagId) {
                pagamento = p;
                break;
            }
        }

        tipoPagtoSelected = pagamento.getTipoPagto().getId().intValue();
        tabelaId = pagamento.getTabelaFinanc().getId().intValue();
        numParcelas = (int) pagamento.getNumParcelas();
    }

    public void gravarPag() {

        TipoPagtoJpaController tpctl = new TipoPagtoJpaController();

        TipoPagto tipoPagto = tpctl.findTipoPagto(tipoPagtoSelected.shortValue());

        if (tipoPagto != null) {
            if (tipoPagto.getGeraBoleto()) {
                if (pagamento.getDataVenc() == null) {
                    addMessage("A data da primeira parcela deve ser definida!!!");
                    return;
                }
            }
        }

        pagamento.setTipoPagto(tipoPagto);

        TabelaFinancJpaController tfctl = new TabelaFinancJpaController(ControllerBase.getEmf());

        TabelaFinanc tabFinanc = tfctl.findTabelaFinanc(tabelaId.shortValue());

        pagamento.setTabelaFinanc(tabFinanc);

        if (tabFinanc != null) {
            pagamento.setNumParcelas(numParcelas.shortValue());
        }

        /*
         * Verifica a Data
         */

        if (pagamento.getId() > 0) {
            /*
             * Editando
             */

            int size = getPagamentos().size();

            for (int i = 0; i < size; i++) {
                if (getPagamentos().get(i).getId() == pagamento.getId()) {
                    getPagamentos().set(i, pagamento);
                    break;
                }
            }

        } else {

            BigDecimal faltante = getValorFaltante();

            int comp = faltante.compareTo(pagamento.getValor());

            if (comp < 0) {
                NumberFormat nf = NumberFormat.getCurrencyInstance();

                addMessage("O valor não pode ser maior que " + nf.format(faltante));
                return;
            }

            pagamento.setId(getPagamentos().size() + 1);

            getPagamentos().add(pagamento);
        }

        RequestContext.getCurrentInstance().execute("pagForm.hide()");

        pagamento = new PedidoPag();
    }

    public List<TipoPagto> getTipoPagtoList() {
        return tipoPagtoList;
    }

    public Integer getTipoPagtoSelected() {
        if (tipoPagtoSelected == null) {
            tipoPagtoSelected = 0;
        }
        return tipoPagtoSelected;
    }

    public void setTipoPagtoSelected(Integer tipoPagtoSelected) {
        this.tipoPagtoSelected = tipoPagtoSelected;
    }

    public List<TabelaFinanc> getTabelaFinancList() {

        return tabelaFinancList;
    }

    public List<SelectItem> getParcelaList() {

        return parcelaList;
    }

    public Integer getTabelaId() {
        return tabelaId;
    }

    public void setTabelaId(Integer tabelaId) {
        this.tabelaId = tabelaId;
    }

    public Integer getNumParcelas() {
        return numParcelas;
    }

    public void setNumParcelas(Integer numParcelas) {
        this.numParcelas = numParcelas;
    }

    public BigDecimal getValorParcela() {

        try {
            TabelaFinancJpaController tfctl = new TabelaFinancJpaController(ControllerBase.getEmf());
            TabelaFinanc tbf = tfctl.findTabelaFinanc(tabelaId.shortValue());
            CoeficienteJpaController cftl = new CoeficienteJpaController(ControllerBase.getEmf());
            CoeficientePK pk = new CoeficientePK(tabelaId.shortValue(), numParcelas.shortValue());
            Coeficiente coef = cftl.findCoeficiente(pk);
            valorParcela = pagamento.getValor().multiply(new BigDecimal(coef.getCoeficiente()));
        } catch (Exception ex) {
            valorParcela = BigDecimal.ZERO;
        }


        return valorParcela;
    }

    public void valorChanged(ValueChangeEvent ev) {
        this.pagamento.setValor((BigDecimal) ev.getNewValue());

        BigDecimal oldValue = (BigDecimal) ev.getOldValue();

        // Se mudou o valor...
        if (!(pagamento.getValor().equals(oldValue))) {
            populateParcelaList();
        }
    }

    public void tipoChanged(ValueChangeEvent ev) {
        tipoPagtoSelected = (Integer) ev.getNewValue();

        tabelaFinancList = new ArrayList<TabelaFinanc>();

        if (tipoPagtoSelected > 0) {

            TipoPagtoJpaController c = new TipoPagtoJpaController();
            TipoPagto tp = c.findTipoPagto(getTipoPagtoSelected().shortValue());

            if (tp.getGeraBoleto()) {
                if (pagamento.getDataVenc() == null) {
                    Calendar data = Calendar.getInstance();
                    data.add(Calendar.MONTH, 1);
                    pagamento.setDataVenc(data.getTime());
                }
            }

            if (tp != null) {

                tabelaFinancList = new ArrayList<TabelaFinanc>(tp.getTabelasFinanc());

                if (tabelaFinancList.size() == 1) {
                    tabelaId = (int) tabelaFinancList.get(0).getId();
                } else {
                    tabelaId = null;
                    numParcelas = null;
                }
            }

        }

        populateParcelaList();
    }

    private void populateParcelaList() {

        List<Coeficiente> lista;

        CoeficienteJpaController c = new CoeficienteJpaController(ControllerBase.getEmf());

        lista = c.findCoeficienteEntities(tabelaId.shortValue());

        parcelaList = new ArrayList<SelectItem>();

        for (Coeficiente co : lista) {

            Short nu = co.getCoeficientePK().getNumParcelas();

            BigDecimal vl = pagamento.getValor().multiply(new BigDecimal(co.getCoeficiente()));

            NumberFormat nf = NumberFormat.getCurrencyInstance();

            SelectItem it = new SelectItem(nu, nu.toString() + " x " + nf.format(vl));

            parcelaList.add(it);
        }

        if (parcelaList.size() == 1) {
            numParcelas = (int) lista.get(0).getCoeficientePK().getNumParcelas();
        } else {
            numParcelas = null;
        }

    }

    public void tabelaChanged(ValueChangeEvent ev) {

        tabelaId = (Integer) ev.getNewValue();

        populateParcelaList();

    }

    /**
     * Torna o pedido persistente no banco de dados
     */
    public void gravaPedido() {

        if (items.isEmpty()) {
            addErrorMessage("O pedido não possui produtos!");
            return;
        }

        if (pagamentos.isEmpty()) {
            addErrorMessage("O pedido deve possuir formas de pagamento!");
            return;
        }
        
        Usuario user = getUsuarioLogado();
        
        if(user == null){
            addErrorMessage("Falha de Login!");
            return;
        }

        PedidoJpaController pedidoService = new PedidoJpaController();

        EntityManager em = null;

        try {
            em = pedidoService.getEntityManager();
            em.getTransaction().begin();



            pedido.setValorTotal(getValorTotalPedido());
            pedido.setData(Calendar.getInstance().getTime());

            pedido.setUsuario(user);

            Cliente cliente = pedido.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getId());
                pedido.setCliente(cliente);
            }

            em.persist(pedido);
            em.flush();

            pedido = em.getReference(pedido.getClass(), pedido.getId());

            //        List<PedidoProduto> produtos = new ArrayList<PedidoProduto>();

            /**
             * Grava os ítens do pedido
             */
            for (PedidoProduto it : items) {

                if (it.getValorTotal() == BigDecimal.ZERO) {
                    throw new Exception("O pedido não pode conter ítem com valor zero.");
                }

//                it.setPedido(pedido);

                Produto produto = it.getProduto();
                if (produto != null) {
                    produto = em.getReference(produto.getClass(), produto.getId());

                    if (produto.getQtdEstoque() == 0) {
                        throw new Exception("O produto " + produto.getDescricaoGeral() + " não está disponível em estoque!");
                    }

                    it.setProduto(produto);

                    /**
                     * Atualiza estoque
                     */
                    if (produto.getQtdEstoque() > 0) {
                        produto.setQtdEstoque(produto.getQtdEstoque() - 1);
                        em.merge(produto);
                    }

                }

                PedidoProdutoPK pk = new PedidoProdutoPK(produto.getId(), pedido.getId());
                it.setPedidoProdutoPK(pk);

                em.persist(it);
                pedido.getItens().add(it);
                em.persist(pedido);

            }


            /**
             * Grava as formas de pagamento
             */
            for (PedidoPag pag : pagamentos) {

                TipoPagto tipoPagto = pag.getTipoPagto();
                if (tipoPagto != null) {
                    tipoPagto = em.getReference(tipoPagto.getClass(), tipoPagto.getId());
                    pag.setTipoPagto(tipoPagto);
                }

                TabelaFinanc tabela = pag.getTabelaFinanc();
                if (tabela != null) {
                    tabela = em.getReference(tabela.getClass(), tabela.getId());
                    pag.setTabelaFinanc(tabela);
                }
                
                pag.setMultaPercent(pag.getTabelaFinanc().getMultaPercent());
                pag.setMultaVal(pag.getTabelaFinanc().getMultaVal());
                pag.setJurosDiario(pag.getTabelaFinanc().getJurosDiario());

                pag.setPedido(pedido);
                pag.setId(null);
                em.persist(pag);
                pedido.getPagamentos().add(pag);
                em.persist(pedido);
                em.flush();

                if (pag.getTipoPagto() != null) {

                    if (pag.getTipoPagto().getGeraBoleto()) {

                        int i;

                        Calendar data = Calendar.getInstance();
                        data.setTime(pag.getDataVenc());

                        CoeficientePK cpk = new CoeficientePK(pag.getTabelaFinanc().getId(), pag.getNumParcelas());
                        Coeficiente coeficiente = em.find(Coeficiente.class, cpk);

                        if (coeficiente == null) {
                            throw new Exception("Coeficiente não cadastrado!");
                        }

                        BigDecimal valParcela = pag.getValor().multiply(new BigDecimal(coeficiente.getCoeficiente()));
                        valParcela = valParcela.setScale(2, RoundingMode.DOWN);

                        for (i = 0; i < pag.getNumParcelas(); i++) {

                            Boleto boleto = new Boleto();

                            boleto.setCliente(pedido.getCliente());
                            boleto.setPedidoPag(pag);
                            boleto.setValor(valParcela);
                            boleto.setVencimento(data.getTime());
                            boleto.setNumParcela((short) (i + 1));

                            em.persist(boleto);

                            //Flush para atualizar e manter a ordem correta das parcelas
                            em.flush();
                            
                            pag.getParcelas().add(boleto);

                            em.persist(pag);

                            data.add(Calendar.MONTH, 1);
                        }

                        em.flush();

                    }
                }
            }

            em.getTransaction().commit();

            String red = "ShowPedido.jsf?pedidoId=" + pedido.getId();

//            addMessage("Pedido Gravado com sucesso com num.: " + pedido.getId());

            pedido = new Pedido();
            items = null;
            pagamentos = null;

            FacesContext.getCurrentInstance().getExternalContext().redirect(red);

        } catch (Exception ex) {

            addErrorMessage(ex.getMessage());

            if (em != null) {
                em.getTransaction().rollback();
            }

        } finally {
            if (em != null) {
                em.close();
            }
        }

    }
}
