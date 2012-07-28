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
    
    private ProdutoJpaController ctl = null;
    private int tipo = 0;

    public ProdutoLazyList(ProdutoJpaController ctl) {
        this.ctl = ctl;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    

    @Override
    public List<Produto> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, String> filters) {

        if (sortField != null && sortOrder != SortOrder.UNSORTED) {
            String ord = "";

            if (sortOrder == SortOrder.ASCENDING) {
                ord = "ASC";
            } else if (sortOrder == SortOrder.DESCENDING) {
                ord = "DESC";
            }

            ctl.setSortedField(sortField, ord);
        }


        List<Produto> lista = ctl.findProdutoEntities(maxPerPage, startingAt, tipo);

        setRowCount(ctl.getProdutoCount(tipo));

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

            p = ctl.findProduto(id);
            
        } catch (Exception ex) {
        }
        return p;
    }
}
