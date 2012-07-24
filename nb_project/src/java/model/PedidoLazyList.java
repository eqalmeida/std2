/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import repo.PedidoJpaController;

/**
 *
 * @author eqalmeida
 */
public class PedidoLazyList extends LazyDataModel<Pedido> {

    private PedidoJpaController service = null;
    private Integer ano = null;
    private Integer mes = null;

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public PedidoLazyList(PedidoJpaController service) {
        this.service = service;
    }

    @Override
    public List<Pedido> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {

        if (sortOrder == SortOrder.UNSORTED || sortField == null) {
            sortField = "id";
            sortOrder = SortOrder.ASCENDING;
        }

        Calendar data = Calendar.getInstance();

        data.set(Calendar.MONTH, mes);
        data.set(Calendar.YEAR, ano);

        List<Pedido> lista = service.findPedidoEntities(maxPerPage, startingAt, data, sortOrder, sortField);

        setRowCount( service.getPedidoCount(data) );
        setPageSize(maxPerPage);

        return (lista);
    }

    @Override
    public Object getRowKey(Pedido pedido) {
        return pedido.getId();
    }

    @Override
    public Pedido getRowData(String Id) {
        Pedido p = null;
        try {
            Integer id = Integer.valueOf(Id);

            p = service.findPedido(id);

        } catch (Exception ex) {
        }
        return p;
    }
}
