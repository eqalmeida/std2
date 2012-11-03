package model;

import repo.ProdutoJpaController;

public class ProdutoLazyList extends AbstractProdutoLazyList {

    public ProdutoLazyList(ProdutoJpaController ctl) {
        super(ctl, Produto.OUTRO);
        ctl.setfTipo((int)Produto.OUTRO);
    }
    
}
