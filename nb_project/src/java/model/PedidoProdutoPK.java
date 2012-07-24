/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author eqalmeida
 */
@Embeddable
public class PedidoProdutoPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "produto_id")
    private int produto;
    @Basic(optional = false)
    @Column(name = "pedido_id")
    private int pedido;

    public PedidoProdutoPK() {
    }

    public PedidoProdutoPK(int produtoId, int pedidoId) {
        this.produto = produtoId;
        this.pedido = pedidoId;
    }

    public int getProduto() {
        return produto;
    }

    public void setProduto(int produto) {
        this.produto = produto;
    }

    public int getPedido() {
        return pedido;
    }

    public void setPedido(int pedido) {
        this.pedido = pedido;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) produto;
        hash += (int) pedido;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoProdutoPK)) {
            return false;
        }
        PedidoProdutoPK other = (PedidoProdutoPK) object;
        if (this.produto != other.produto) {
            return false;
        }
        if (this.pedido != other.pedido) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PedidoProdutoPK[ produtoId=" + produto + ", pedidoId=" + pedido + " ]";
    }
    
}
