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

    @Override
    public List<Boleto> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {
/*
        getService().setFilterNome(null);

        for(String filterP : filters.keySet()){
            if(filterP.equalsIgnoreCase("nome")){
                getService().setFilterNome(filters.get(filterP));
                break;
            }
        }
*/     
        
        getService().setDatas(dateTo, dateFrom);
        
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            String ord = "";

            if (sortOrder == SortOrder.ASCENDING) {
                ord = "ASC";
            } else if (sortOrder == SortOrder.DESCENDING) {
                ord = "DESC";
            }

            getService().setSortedField(sortField, ord);
        }


        List<Boleto> lista = getService().findBoletoEntities(maxPerPage, startingAt);

        setRowCount(getService().getBoletoCount());

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

            b = getService().findBoleto(id);
            
        } catch (Exception ex) {
        }
        return b;
    }

    public BoletoJpaController getService() {
        if(service == null){
            service = new BoletoJpaController();
        }
        return service;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    
    
}
