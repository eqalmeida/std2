/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import model.Produto;
import model.ProdutoLazyList;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import repo.ProdutoJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean
@ViewScoped
public class ProdutoBean extends ControllerBase implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final short VEICULO = 1;
    public static final short OUTROS = 0;
    private final String CLASS_NAME = "Produto";
    private final String CLASS_NAME_P = "Produtos";
    private Produto selected = null;
    private static List<SelectItem> tipoCombustivel = null;
    private List<Produto> todosProdutos;
    private int tipoProduto = 1;
    private boolean emEstoque = true;
    private LazyDataModel<Produto> lazyList = null;
    ProdutoJpaController ctl = null;

    /**
     * Creates a new instance of ProdutoBean
     */
    public ProdutoBean() {
    }

    @PostConstruct
    private void init() {
        ctl = new ProdutoJpaController();

        tipoProduto = 1;

        lazyList = new ProdutoLazyList(ctl);

        ProdutoLazyList pLazy = (ProdutoLazyList) lazyList;

        pLazy.getCtl().setfTipo(tipoProduto);
        pLazy.getCtl().setfEstoque(1);
    }

    public boolean getEmEstoque() {
        return emEstoque;
    }

    public void setEmEstoque(boolean emEstoque) {
        this.emEstoque = emEstoque;
    }

    public LazyDataModel<Produto> getLazyList() {

        if (lazyList == null) {
            lazyList = new ProdutoLazyList(ctl);
        }

        return lazyList;
    }

    public Produto getSelected() {
        return selected;
    }

    public void setSelected(Produto selected) {
        this.selected = selected;
    }

    public List<Produto> getVeiculos() {
        todosProdutos = ctl.findProdutoEntities(1);
        return todosProdutos;
    }

    public List<Produto> getOutrosProdutos() {
        todosProdutos = ctl.findProdutoEntities(0);
        return todosProdutos;
    }

    public void inserir() {

        if (!verificaLogin("")) {
            addErrorMessage("Falha de Login");
            return;
        }

        try {

            if (selected.getId() > 0) {
                ctl.edit(selected);
            } else {
                ctl.create(selected);
            }

            RequestContext.getCurrentInstance().execute("dialogNewCar.hide()");

        } catch (Exception ex) {
            addMessage(ex.getMessage());
        }
    }

    public void salvar() {
        if (!verificaLogin("")) {
            addErrorMessage("Falha de Login");
            return;
        }

        try {
            ctl.edit(selected);
        } catch (Exception ex) {
            addMessage(ex.getMessage());
        }
    }

    public void onRowSelect(SelectEvent event) {
        selected = ctl.find(((Produto) event.getObject()).getId());
    }

    public void novoProduto(Short tipo) {
        if (!verificaLogin("")) {
            addErrorMessage("Falha de Login");
            return;
        }

        selected = new Produto();
        selected.setTipo(tipo);
        RequestContext.getCurrentInstance().execute("dialogNewCar.show()");
    }

    public void exibir() {
    }

    public void excluirProduto(int id) {
        if (!verificaLogin("")) {
            addErrorMessage("Falha de Login");
            return;
        }

        if (!getUsuarioLogado().isAdmin()) {
            addErrorMessage("Acesso negado");
            return;
        }


        try {
            ctl.destroy(id);
            this.selected = new Produto();
            RequestContext.getCurrentInstance().execute("dialogNewCar.hide()");
        } catch (Exception ex) {
            addMessage("Não foi possível excluir!");

        }

    }

    public void editar() {
        if (!verificaLogin("")) {
            addErrorMessage("Falha de Login");
            return;
        }

        if (this.selected.getId() != null) {
            selected = ctl.find(this.selected.getId());
            RequestContext.getCurrentInstance().execute("dialogNewCar.show()");
        }
    }

    public String getCLASS_NAME() {
        return CLASS_NAME;
    }

    public String getCLASS_NAME_P() {
        return CLASS_NAME_P;
    }

    public List<SelectItem> getTipoCombustivel() {

        /*
         * Cria a lista somente uma vez
         */
        if (tipoCombustivel == null) {

            tipoCombustivel = new ArrayList<SelectItem>();

            SelectItem it;

            Map<Short, String> lista = Produto.getCombustivelList();

            for (Short i : lista.keySet()) {
                it = new SelectItem(i, lista.get(i));
                tipoCombustivel.add(it);

            }
        }
        return tipoCombustivel;
    }

    public int getTipoProduto() {
        return tipoProduto;
    }

    public void emEstoqueChanged() {

        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formTable:tbl");
        dataTable.setFirst(0);

        if (emEstoque) {
            ((ProdutoLazyList) lazyList).getCtl().setfEstoque(1);
        } else {
            ((ProdutoLazyList) lazyList).getCtl().setfEstoque(null);
        }
        ((ProdutoLazyList) lazyList).getCtl().setfTipo(tipoProduto);
    }

    public void tipoChanged(ValueChangeEvent ev) {
        tipoProduto = (Integer) ev.getNewValue();
        ((ProdutoLazyList) lazyList).getCtl().setfTipo(tipoProduto);
    }

    public void setTipoProduto(int tipoProduto) {

        ProdutoLazyList l = (ProdutoLazyList) getLazyList();
        l.getCtl().setfTipo(tipoProduto);

        selected = null;
        this.tipoProduto = tipoProduto;
    }

    public void metodoValueChangeListener(ValueChangeEvent e) {
        e.getNewValue();
    }

    public void addToPedido(int id) {

        if (!verificaLogin("")) {
            addErrorMessage("Falha de Login");
            return;
        }


        try {

            FacesContext context = FacesContext.getCurrentInstance();

            PedidoBean pedidoMB = context.getApplication().evaluateExpressionGet(context, "#{pedidoMB}", PedidoBean.class);

            selected = ctl.find(id);

            pedidoMB.addProduto(selected);

            addMessage("Produto adicionado!");
        } catch (Exception ex) {
            addErrorMessage("Não foi possível adicionar!\n" + ex.getMessage());
        }
    }

    public void editarProduto(int id) {
        if (!verificaLogin("")) {
            addErrorMessage("Falha de Login");
            return;
        }

        selected = ctl.find(id);
        RequestContext.getCurrentInstance().execute("dialogNewCar.show()");
    }

    public void imprimirVeiculos() {
        showPopup("PrintVeiculos.jsf");
    }
    /*
     public void imprimir() throws JRException, IOException{
     ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
     List<Produto> veiculos = ctl.findVeiculosEmEstoque();
     JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(veiculos);
     String fileName = context.getRealPath("/reports/estoque.jasper");
     JasperPrint jasperPrint = JasperFillManager.fillReport(fileName, new HashMap(), beanCollectionDataSource);
     HttpServletResponse response = (HttpServletResponse)context.getResponse();
     response.addHeader("content-disposition", "attachment; filename=produtos.pdf");
     ServletOutputStream outputStream = response.getOutputStream();
     JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        
     }*/
}
