/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import model.Boleto;
import model.BoletoLazyList;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import repo.BoletoJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "boletoMB")
@ViewScoped
public class BoletoBean extends ControllerBase {

    private LazyDataModel<Boleto> lazyList = null;
    private Boleto selected = null;
    private Short filtMes = null;
    private Integer filtAno = null;
    private BoletoJpaController service = null;
    private int tipoRel;

    /**
     * Creates a new instance of BoletoBean
     */
    public BoletoBean() {
        Calendar data = GregorianCalendar.getInstance();
        filtAno = data.get(Calendar.YEAR);
        filtMes = (short) (data.get(Calendar.MONTH));
    }

    @PostConstruct
    private void init() {
        tipoRel = 0;
        service = new BoletoJpaController();
        lazyList = new BoletoLazyList(this.service);
        aplicaFiltro();
    }

    private void resetTable() {
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent(":formTable:tbl");
        dataTable.setFirst(0);
    }

    public int getTipoRel() {
        return tipoRel;
    }

    public void setTipoRel(int tipoRel) {
        this.tipoRel = tipoRel;
    }

    public void aplicaFiltro() {

        BoletoLazyList b = (BoletoLazyList) getLazyList();

        if (tipoRel == 1) {
            b.setAtrasados(true);
        } else {
            
            b.setAtrasados(false);
            
            Calendar data = GregorianCalendar.getInstance();
            data.set(Calendar.YEAR, filtAno);
            data.set(Calendar.MONTH, filtMes);
            data.set(Calendar.DATE, 1);

            Date dateFrom, dateTo;

            dateFrom = data.getTime();

            data.add(Calendar.MONTH, 1);
            data.add(Calendar.DATE, -1);
            dateTo = data.getTime();



            b.setDateFrom(dateFrom);
            b.setDateTo(dateTo);
        }
        resetTable();
    }

    public void verPedido(int pedidoId) {
        String red = "ShowPedido.jsf?pedidoId=" + pedidoId;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(red);
        } catch (IOException ex) {
            addErrorMessage(ex.getMessage());
        }
    }

    public LazyDataModel<Boleto> getLazyList() {

        return lazyList;
    }

    public BigDecimal getTotalMes() {
        return (service.getValorTotal());
    }

    public void setLazyList(LazyDataModel<Boleto> lazyList) {
        this.lazyList = lazyList;
    }

    public Boleto getSelected() {
        if (selected == null) {
            selected = new Boleto();
        }
        return selected;
    }

    public void setSelected(Boleto selected) {
        this.selected = selected;
    }

    public Short getFiltMes() {
        return filtMes;
    }

    public void setFiltMes(Short filtMes) {
        this.filtMes = filtMes;
    }

    public Integer getFiltAno() {
        return filtAno;
    }

    public void setFiltAno(Integer filtAno) {
        this.filtAno = filtAno;
    }

    public List<Object[]> getReportList() {
        return service.findBoletoReport();
    }

    public String getStatusToStr(Short num) {
        return Boleto.listStatus.get(num);
    }
}
