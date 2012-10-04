/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import repo.ClienteJpaController;

/**
 *
 * @author eqalmeida
 */
public class ClienteLazyList extends LazyDataModel<Cliente> {
    
    private ClienteJpaController service = null;

    @Override
    public List<Cliente> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {

        getService().setFilterNome(null);

        for(String filterP : filters.keySet()){
            if(filterP.equalsIgnoreCase("nome")){
                getService().setFilterNome(filters.get(filterP));
                break;
            }
        }
        
        
        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            String ord = "";

            if (sortOrder == SortOrder.ASCENDING) {
                ord = "ASC";
            } else if (sortOrder == SortOrder.DESCENDING) {
                ord = "DESC";
            }

            getService().setSortedField(sortField, ord);
        }


        List<Cliente> lista = getService().findClienteEntities(maxPerPage, startingAt);

        setRowCount(getService().getClienteCount());

        setPageSize(maxPerPage);


        return (lista);
    }

    @Override
    public Object getRowKey(Cliente cliente) {
        return cliente.getId();
    }

    @Override
    public Cliente getRowData(String Id) {
        Cliente c = null;
        try {
            Integer id = Integer.valueOf(Id);


            c = getService().find(id);
            
        } catch (Exception ex) {
        }
        return c;
    }

    public ClienteJpaController getService() {
        if(service == null){
            service = new ClienteJpaController();
        }
        return service;
    }
    
    
}
