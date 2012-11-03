package model;

import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import repo.ProdutoJpaController;

public abstract class AbstractProdutoLazyList extends LazyDataModel<Produto> {
    
    private ProdutoJpaController ctl = null;
    private Short tipo;

    public AbstractProdutoLazyList(ProdutoJpaController ctl, Short tipo) {
        this.ctl = ctl;
        this.tipo = tipo;
    }

    public ProdutoJpaController getCtl() {
        return ctl;
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
        
        if(filters.containsKey("placa") && this.tipo == 1){
            ctl.setfPlaca(filters.get("placa"));
            ctl.setSortedField("placa", "ASC");
            ctl.setfEstoque(null);
        }else{
            ctl.setfPlaca(null);
            
            if(filters.containsKey("placa")){
                filters.remove("placa");
            }
            
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

            p = ctl.find(id);
            
        } catch (Exception ex) {
        }
        return p;
    }
}
