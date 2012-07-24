/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import repo.ProdutoJpaController;

/**
 *
 * @author eqalmeida
 */
public class ProdutoLazyList extends LazyDataModel<Produto> {

    @Override
    public List<Produto> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {

        ProdutoJpaController ctl = new ProdutoJpaController();

        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            String ord = "";

            if (sortOrder == SortOrder.ASCENDING) {
                ord = "ASC";
            } else if (sortOrder == SortOrder.DESCENDING) {
                ord = "DESC";
            }

            ctl.setSortedField(sortField, ord);
        }


        List<Produto> lista = ctl.findProdutoEntities(maxPerPage, startingAt);

        setRowCount(ctl.getProdutoCount());

        setPageSize(maxPerPage);


        return (lista);
    }

    @Override
    public Object getRowKey(Produto produto) {
        return produto.getId();
    }

    @Override
    public Produto getRowData(String Id) {
        Produto p = null;
        try {
            Integer id = Integer.valueOf(Id);

            ProdutoJpaController ctl = new ProdutoJpaController();

            p = ctl.findProduto(id);
            
        } catch (Exception ex) {
        }
        return p;
    }
}
