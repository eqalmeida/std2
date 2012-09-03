/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;
import model.Coeficiente;
import model.CoeficientePK;
import model.TabelaFinanc;
import org.primefaces.context.RequestContext;
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
    private List<TabelaFinanc> tabelas = null;
    private TabelaFinancJpaController service = null;
    private CoeficientePK coefIdAnt = null;

    /**
     * Creates a new instance of ProdutoBean
     */
    public TabelaFinancBean() {
    }

    @PostConstruct
    private void init() {
        service = new TabelaFinancJpaController(getEmf());
    }

    public List<TabelaFinanc> getTabelas() {
        tabelas = service.findTabelaFinancEntities();
        return tabelas;
    }

    public void setTabelas(List<TabelaFinanc> tabelas) {
        this.tabelas = tabelas;
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

    public void gravar() {
        try {

            if (selected.getId() > 0) {
                service.edit(selected);
            } else {
                selected.setId(null);
                service.create(selected);
            }
            RequestContext.getCurrentInstance().execute("tblDlg.hide()");
            addMessage("Gravado com sucesso!");

        } catch (Exception ex) {
            addErrorMessage(ex.getMessage());
        }
    }

    public void novo() {
        selected = new TabelaFinanc();
        RequestContext.getCurrentInstance().execute("tblDlg.show()");
    }

    public void exibir() {
    }

    public void excluirTabela(Short id) {
        try {
            service.destroy(id);
            this.selected = new TabelaFinanc();
            RequestContext.getCurrentInstance().execute("tblDlg.hide()");
        } catch (Exception ex) {
            addErrorMessage("Não foi possível excluir!");

        }

    }

    public void editarTabela(Short id) {
        selected = service.findTabelaFinanc(id);
        RequestContext.getCurrentInstance().execute("tblDlg.show()");
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

    public void setSubItems(List<Coeficiente> subItems) {
        this.subItems = subItems;
    }

    public void novoCoef() {
        subItem = new Coeficiente();
        subItem.setCoeficientePK(new CoeficientePK());
        RequestContext.getCurrentInstance().execute("coefDlg.show()");
    }

    public void editarCoef() {
        coefIdAnt = new CoeficientePK();
        coefIdAnt.setNumParcelas(subItem.getCoeficientePK().getNumParcelas());
        coefIdAnt.setTabelaFinanc(subItem.getCoeficientePK().getTabelaFinanc());
        RequestContext.getCurrentInstance().execute("coefDlg.show()");
    }

    public void gravarCoef() {
        EntityManager em = getEmf().createEntityManager();
        try {
            verificaCoeficiente(subItem);

            em.getTransaction().begin();

            if (!(subItem.getCoeficientePK().getTabelaFinanc() > 0)) {
                // Novo
                subItem.setTabelaFinanc(selected);
                subItem.getCoeficientePK().setTabelaFinanc(selected.getId());
                em.persist(subItem);

            } else {
                /**
                 * Se é uma edição, apaga o coeficiente anterior
                 */
                System.out.println("Coef id :" + coefIdAnt.getNumParcelas() + " " + coefIdAnt.getTabelaFinanc());
                Coeficiente coefAnt = em.find(Coeficiente.class, coefIdAnt);
                em.remove(coefAnt);
                em.flush();

                em.persist(subItem);
            }

            em.getTransaction().commit();

            coefIdAnt = null;

            RequestContext.getCurrentInstance().execute("coefDlg.hide()");
            addMessage("Gravado com sucesso!");

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            addErrorMessage(ex.getMessage());
        } finally {
            em.close();
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

    void verificaCoeficiente(Coeficiente co) throws Exception {
        double min;
        min = (1.000 / (double) co.getCoeficientePK().getNumParcelas());
        if (min > co.getCoeficiente()) {
            throw new Exception("Erro!\nEste Coeficiente está abaixo do valor mínimo!");
        }
    }
}
