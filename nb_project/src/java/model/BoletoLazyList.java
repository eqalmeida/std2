/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import repo.BoletoJpaController;

/**
 *
 * @author eqalmeida
 */
public class BoletoLazyList extends LazyDataModel<Boleto> {

    private BoletoJpaController service = null;
    private Date dateTo = null;
    private Date dateFrom = null;
    private boolean atrasados = false;

    public BoletoLazyList(BoletoJpaController service) {
        this.service = service;
    }

    @Override
    public List<Boleto> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {


        service.setSortedField("vencimento", "ASC");

        List<Boleto> lista;

        if (atrasados == false) {
            service.setDatas(dateTo, dateFrom);
            lista = service.findBoletoEntities(maxPerPage, startingAt);
            setRowCount(service.getBoletoCount());

        }
        else{
            lista = service.findBoletosAtrasados(maxPerPage, startingAt);
            setRowCount(service.getBoletosAtrasadosCount());
        }

        setPageSize(maxPerPage);

        return (lista);
    }

    @Override
    public Object getRowKey(Boleto boleto) {
        return boleto.getId();
    }

    @Override
    public Boleto getRowData(String Id) {
        Boleto b = null;
        try {
            Integer id = Integer.valueOf(Id);

            b = service.find(id);

        } catch (Exception ex) {
        }
        return b;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setAtrasados(boolean atrasados) {
        this.atrasados = atrasados;
    }
}
