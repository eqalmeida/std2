/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import facade.TipoPagtoFacace;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.TabelaFinanc;
import model.TipoPagto;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
import repo.TabelaFinancJpaController;


/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "tipoPagtoBean")
@ViewScoped
public class TipoPagtoBean extends ControllerBase {

    private TipoPagto selected = null;
    private List<TipoPagto> lista = null;
    private DualListModel<TabelaFinanc> tabelas;
    private static List<TabelaFinanc> tabelasFinanc;
    private List<TabelaFinanc> tabelasSelecionadas;
    private TipoPagtoFacace service = null;

    /**
     * Creates a new instance of ProdutoBean
     */
    public TipoPagtoBean() {
    }

    public TipoPagto getSelected() {
        return selected;
    }

    public void setSelected(TipoPagto selected) {
        this.selected = selected;
    }

    public List<TipoPagto> getLista() {
        lista = service.findAll();
        return lista;
    }

    public void setLista(List<TipoPagto> lista) {
        this.lista = lista;
    }

    public void gravar() {
        try {
            if (selected.getId() == null || selected.getId() == 0) {
                service.create(selected);
            } else {
                service.edit(selected);
            }
            RequestContext.getCurrentInstance().execute("tipoDlg.hide()");
        } catch (Exception ex) {
            addErrorMessage(ex.getMessage());
        }
    }

    public void onRowSelect(SelectEvent event) {

        selected = service.find(((TipoPagto) event.getObject()).getId());

        if (selected.getId() != null) {

            tabelasSelecionadas = new ArrayList<TabelaFinanc>(selected.getTabelasFinanc());

            TabelaFinancJpaController ctl2 = new TabelaFinancJpaController();
            List<TabelaFinanc> c = ctl2.findTabelaFinancEntities();

            tabelasFinanc = new ArrayList<TabelaFinanc>();
            for (TabelaFinanc t : c) {
                if (!tabelasSelecionadas.contains(t)) {
                    tabelasFinanc.add(t);
                }
            }

            tabelas = new DualListModel<TabelaFinanc>(tabelasFinanc, tabelasSelecionadas);
        }

    }

    public void novo() {
        selected = new TipoPagto();
        RequestContext.getCurrentInstance().execute("tipoDlg.show()");
    }

    public void excluir(Short id) throws Exception {
        try {
            selected = service.find(id);
            service.remove(selected);
            this.selected = new TipoPagto();
        } catch (Exception ex) {
            addErrorMessage("Não foi possível excluir!");
        }

    }

    public void editar(Short id) {
        selected = service.find(id);
        RequestContext.getCurrentInstance().execute("tipoDlg.show()");
    }

    public void cancela() {
        RequestContext.getCurrentInstance().execute("tipoDlg.hide()");
    }

    public void setTabelas(DualListModel<TabelaFinanc> tabelas) {
        this.tabelas = tabelas;
    }

    public List<TabelaFinanc> getTabelasSelecionadas() {
        return tabelasSelecionadas;
    }

    public void setTabelasSelecionadas(List<TabelaFinanc> tabelasSelecionadas) {
        this.tabelasSelecionadas = tabelasSelecionadas;
    }

    public DualListModel<TabelaFinanc> getTabelas() {
        return tabelas;
    }

    /**
     * Salva a referencia cruzada no banco
     */
    public void onTransfer() {

        TabelaFinancJpaController ctl2 = new TabelaFinancJpaController();

        try {

            // Carrega o item do banco
            selected = service.find(selected.getId());


            // Limpa o conteúdo antigo
            selected.getTabelasFinanc().clear();
            service.edit(selected);


            for (Iterator it = tabelas.getTarget().iterator(); it.hasNext();) {
                TabelaFinanc item;
                item = ctl2.findTabelaFinanc(Short.parseShort(it.next().toString()));

                selected.getTabelasFinanc().add(item);
            }

            service.edit(selected);
        } catch (Exception ex) {
            addMessage("Não foi possível Salvar!" + ex.getMessage());
        }

    }

    @PostConstruct
    public void init() {
        service = new TipoPagtoFacace();
        TabelaFinancJpaController ctl = new TabelaFinancJpaController();
        tabelasFinanc = ctl.findTabelaFinancEntities();

        tabelas = new DualListModel<TabelaFinanc>();

        selected = new TipoPagto();
    }
}
