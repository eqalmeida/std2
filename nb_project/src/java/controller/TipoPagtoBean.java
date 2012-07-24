/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import model.TabelaFinanc;
import model.TipoPagto;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DualListModel;
import repo.TabelaFinancJpaController;
import repo.TipoPagtoJpaController;

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
    private TipoPagtoJpaController service = null;

    /**
     * Creates a new instance of ProdutoBean
     */
    public TipoPagtoBean() {

        TabelaFinancJpaController ctl = new TabelaFinancJpaController(getEmf());
        tabelasFinanc = ctl.findTabelaFinancEntities();

        tabelas = new DualListModel<TabelaFinanc>();

        selected = new TipoPagto();
    }

    public TipoPagto getSelected() {
        return selected;
    }

    public void setSelected(TipoPagto selected) {
        this.selected = selected;
    }

    public List<TipoPagto> getLista() {
        lista = getService().findTipoPagtoEntities();
        return lista;
    }

    public void setLista(List<TipoPagto> lista) {
        this.lista = lista;
    }

    public void inserir() {
        try {
            selected.setId(null);
            getService().create(selected);
        } catch (Exception ex) {
            addMessage(ex.getMessage());
        }
    }

    public void salvar() {
        try {
            getService().edit(selected);
        } catch (Exception ex) {
            addMessage(ex.getMessage());
        }
    }

    public void onRowSelect(SelectEvent event) {

        selected = getService().findTipoPagto(((TipoPagto) event.getObject()).getId());

        if (selected.getId() != null) {

            tabelasSelecionadas = new ArrayList<TabelaFinanc>(selected.getTabelasFinanc());

            TabelaFinancJpaController ctl2 = new TabelaFinancJpaController(getEmf());
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
    }

    public void excluir() {
        try {
            getService().destroy(this.selected.getId());
            this.selected = new TipoPagto();
        } catch (Exception ex) {
            addMessage("Não foi possível excluir!");

        }

    }

    public void editar() {
        if (this.selected.getId() != null) {
            selected = getService().findTipoPagto(this.selected.getId());
        }
    }

    public void cancela() {
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

        TabelaFinancJpaController ctl2 = new TabelaFinancJpaController(getEmf());

        try {

            // Carrega o item do banco
            selected = getService().findTipoPagto(selected.getId());


            // Limpa o conteúdo antigo
            selected.getTabelasFinanc().clear();
            getService().edit(selected);


            for (Iterator it = tabelas.getTarget().iterator(); it.hasNext();) {
                TabelaFinanc item;
                item = ctl2.findTabelaFinanc(Short.parseShort(it.next().toString()));

                selected.getTabelasFinanc().add(item);
            }

            getService().edit(selected);
        } catch (Exception ex) {
            addMessage("Não foi possível Salvar!" + ex.getMessage());
        }

    }

    public TipoPagtoJpaController getService() {
        if(service == null){
            service = new TipoPagtoJpaController();
        }
        return service;
    }
    
    
}
