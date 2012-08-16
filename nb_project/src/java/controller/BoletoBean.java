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
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import model.Boleto;
import model.BoletoLazyList;
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
        service = new BoletoJpaController();
    }

    public void verPedido(int pedidoId) {
        String red = "ShowPedido.jsf?pedidoId=" + pedidoId;
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(red);
        } catch (IOException ex) {
            addErrorMessage(ex.getMessage());
        }
    }

    public void updateDatas() {
        Calendar data = GregorianCalendar.getInstance();
        data.set(Calendar.YEAR, filtAno);
        data.set(Calendar.MONTH, filtMes);
        data.set(Calendar.DATE, 1);

        Date dateFrom, dateTo;

        dateFrom = data.getTime();

        data.add(Calendar.MONTH, 1);
        data.add(Calendar.DATE, -1);
        dateTo = data.getTime();


        BoletoLazyList b = (BoletoLazyList) getLazyList();

        b.setDateFrom(dateFrom);
        b.setDateTo(dateTo);

    }

    public LazyDataModel<Boleto> getLazyList() {
        if (lazyList == null) {
            lazyList = new BoletoLazyList(this.service);

            updateDatas();
        }
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

    public void mesChanged(ValueChangeEvent ev) {
        filtMes = (Short) ev.getNewValue();
        updateDatas();
    }

    public void anoChanged(ValueChangeEvent ev) {
        filtAno = (Integer) ev.getNewValue();
        updateDatas();
    }
}
