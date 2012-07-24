/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Coeficiente;
import model.CoeficientePK;
import model.TabelaFinanc;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import repo.CoeficienteJpaController;
import repo.TabelaFinancJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean
@ViewScoped
public class TabelaFinancBean extends ControllerBase {

    private TabelaFinanc selected = null;
    private Coeficiente subItem = new Coeficiente();
    private List<Coeficiente> subItems = null;
    private int tabelaPos = 0;
    

    /**
     * Creates a new instance of ProdutoBean
     */
    public TabelaFinancBean() {
        updateTabelaByPos();
    }

    public int getTabelaPos() {
        return tabelaPos;
    }

    private void updateTabelaByPos() {
        TabelaFinancJpaController ctl = new TabelaFinancJpaController(getEmf());
        selected = ctl.findTabelaFinancEntities(1, tabelaPos).get(0);
    }

    public TabelaFinanc getSelected() {
        return selected;
    }

    public void setSelected(TabelaFinanc selected) {
        this.selected = selected;
    }

    public Coeficiente getSubItem() {
        return subItem;
    }

    public void setSubItem(Coeficiente subItem) {
        this.subItem = subItem;
    }

    public void inserir() {
        TabelaFinancJpaController ctl = new TabelaFinancJpaController(getEmf());
        try {
            selected.setId(null);
            ctl.create(selected);
            tabelaPos = getMaxTabelas() - 1;
            updateTabelaByPos();
        } catch (Exception ex) {
            addMessage(ex.getMessage());
        }
    }

    public void salvar() {
        TabelaFinancJpaController ctl = new TabelaFinancJpaController(getEmf());
        try {
            ctl.edit(selected);
            updateTabelaByPos();
        } catch (Exception ex) {
            addMessage(ex.getMessage());
        }
    }

    public void onRowSelect(SelectEvent event) {

        TabelaFinancJpaController ctl = new TabelaFinancJpaController(getEmf());
        selected = ctl.findTabelaFinanc(((TabelaFinanc) event.getObject()).getId());
    }

    public void novo() {
        selected = new TabelaFinanc();
    }

    public void exibir() {
    }

    public void excluir() {
        TabelaFinancJpaController ctl = new TabelaFinancJpaController(getEmf());
        try {
            ctl.destroy(this.selected.getId());
            this.selected = new TabelaFinanc();
        } catch (Exception ex) {
            addMessage("Não foi possível excluir!");

        }

    }

    public void editar() {
        if (this.selected.getId() != null) {
            TabelaFinancJpaController ctl = new TabelaFinancJpaController(getEmf());
            selected = ctl.findTabelaFinanc(this.selected.getId());
        }
    }

    public List<Coeficiente> getSubItems() {
        CoeficienteJpaController ctl = new CoeficienteJpaController(getEmf());
        if (selected.getId() != null) {
            subItems = ctl.findCoeficienteEntities(selected.getId());
        } else {
            subItems = new ArrayList<Coeficiente>();
        }
        return subItems;
    }

    public void incPos() {
        int qtd = getMaxTabelas();

        if (tabelaPos < (qtd - 1)) {
            tabelaPos++;
            updateTabelaByPos();
        }
    }

    public void decPos() {
        if (tabelaPos > 0) {
            tabelaPos--;
            updateTabelaByPos();
        }
    }

    public void setSubItems(List<Coeficiente> subItems) {
        this.subItems = subItems;
    }
    
    

    public int getMaxTabelas() {
        TabelaFinancJpaController ctl = new TabelaFinancJpaController(getEmf());
        return ctl.getTabelaFinancCount();
    }

    public void cancela() {
        updateTabelaByPos();
    }

    public void novoCoef() {
        subItem = new Coeficiente();
        subItem.setCoeficientePK(new CoeficientePK());
    }

    public void inserirCoef() {
        CoeficienteJpaController ctl = new CoeficienteJpaController(getEmf());
        try {
            verificaCoeficiente(subItem);
            subItem.setTabelaFinanc(selected);
            ctl.create(subItem);
        } catch (Exception ex) {
            addMessage(ex.getMessage());
        }
    }
    
    public void excluirCoef() {
        CoeficienteJpaController ctl = new CoeficienteJpaController(getEmf());
        try {
            ctl.destroy(subItem.getCoeficientePK());
            subItem = new Coeficiente();
        } catch (Exception ex) {
            addMessage(ex.getMessage());
        }

    }
    
    public void salvarCoef() {
        CoeficienteJpaController ctl = new CoeficienteJpaController(getEmf());
        try {
            verificaCoeficiente(subItem);
            ctl.edit(subItem);
        } catch (Exception ex) {
            addMessage(ex.getMessage());
        }
    }
    
    void verificaCoeficiente(Coeficiente co) throws Exception{
        double min;
        min = (1.000/(double)co.getCoeficientePK().getNumParcelas());
        if(min > co.getCoeficiente()){
            throw new Exception("Erro!\nEste Coeficiente está abaixo do valor mínimo!");
        }
    }

    
}
