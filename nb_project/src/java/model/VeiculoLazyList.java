package model;

import repo.ProdutoJpaController;

public class VeiculoLazyList extends AbstractProdutoLazyList {

    public VeiculoLazyList(ProdutoJpaController ctl) {
        super(ctl, Produto.VEICULO);
        ctl.setfTipo((int)Produto.VEICULO);
    }
}
