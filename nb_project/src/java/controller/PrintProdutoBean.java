/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import model.Produto;
import repo.ProdutoJpaController;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "printProdutoMB")
@RequestScoped
public class PrintProdutoBean extends ControllerBase {

    List<Produto> veiculosEmEstoque;

    
    /**
     * Creates a new instance of PrintProdutoBean
     */
    public PrintProdutoBean() {
    }

    public List<Produto> getVeiculosEmEstoque() {
        veiculosEmEstoque = new ProdutoJpaController().findVeiculosEmEstoque();
        return veiculosEmEstoque;
    }
    
    
}
