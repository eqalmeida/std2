/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author eqalmeida
 */
@Entity
@Table(name = "pedido_produtos")
//@AssociationOverrides(
//        {@AssociationOverride(name="pedidoProdutoPK.produto", joinColumns= @JoinColumn(name="produto_id")),
        
//        }
//        )
@NamedQueries({
    @NamedQuery(name = "PedidoProduto.findAll", query = "SELECT p FROM PedidoProduto p")})
public class PedidoProduto implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PedidoProdutoPK pedidoProdutoPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @Column(name = "qtd")
    private short qtd;
    @JoinColumn(name = "pedido_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "produto_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Produto produto;
    @Column(name="desconto")
    private Double desconto;

    public PedidoProduto() {
    }

    public PedidoProduto(PedidoProdutoPK pedidoProdutoPK) {
        this.pedidoProdutoPK = pedidoProdutoPK;
    }

    public PedidoProduto(PedidoProdutoPK pedidoProdutoPK, BigDecimal valor, short qtd) {
        this.pedidoProdutoPK = pedidoProdutoPK;
        this.valor = valor;
        this.qtd = qtd;
    }

    public PedidoProduto(int produtoId, int pedidoId) {
        this.pedidoProdutoPK = new PedidoProdutoPK(produtoId, pedidoId);
    }

    public PedidoProdutoPK getPedidoProdutoPK() {
        if(pedidoProdutoPK == null){
            pedidoProdutoPK = new PedidoProdutoPK();
        }
        return pedidoProdutoPK;
    }

    public void setPedidoProdutoPK(PedidoProdutoPK pedidoProdutoPK) {
        this.pedidoProdutoPK = pedidoProdutoPK;
    }

    public BigDecimal getValor() {
        return valor;
    }
    
    public BigDecimal getValorTotal(){
        
        if(this.valor == null || this.qtd == 0){
            return BigDecimal.ZERO;
        }
        
        BigDecimal val = this.getValor();
        
        val = val.multiply(new BigDecimal(this.qtd));
        
        if(this.desconto != null){
            
            BigDecimal desc = val.multiply( new BigDecimal(this.desconto) );
            
            desc = desc.divide(new BigDecimal(100.0));
            
            val = val.subtract(desc);
        }
        
        return val.setScale(2);
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public short getQtd() {
        return qtd;
    }

    public void setQtd(short qtd) {
        this.qtd = qtd;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
//        this.getPedidoProdutoPK().setPedido(pedido.getId());
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
  //      this.getPedidoProdutoPK().setProduto(produto.getId());
        this.produto = produto;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pedidoProdutoPK != null ? pedidoProdutoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PedidoProduto)) {
            return false;
        }
        PedidoProduto other = (PedidoProduto) object;
        if ((this.pedidoProdutoPK == null && other.pedidoProdutoPK != null) || (this.pedidoProdutoPK != null && !this.pedidoProdutoPK.equals(other.pedidoProdutoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PedidoProduto[ pedidoProdutoPK=" + pedidoProdutoPK + " ]";
    }
    
}
