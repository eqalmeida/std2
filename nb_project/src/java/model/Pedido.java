/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eqalmeida
 */
@Entity
@Table(name = "pedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pedido.findAll", query = "SELECT p FROM Pedido p"),
    @NamedQuery(name = "Pedido.findById", query = "SELECT p FROM Pedido p WHERE p.id = :id"),
    @NamedQuery(name = "Pedido.findByData", query = "SELECT p FROM Pedido p WHERE p.data = :data"),
    @NamedQuery(name = "Pedido.findByValorTotal", query = "SELECT p FROM Pedido p WHERE p.valorTotal = :valorTotal"),
    @NamedQuery(name = "Pedido.findByDesconto", query = "SELECT p FROM Pedido p WHERE p.desconto = :desconto"),
    @NamedQuery(name = "Pedido.findByStatus", query = "SELECT p FROM Pedido p WHERE p.status = :status")})
public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor_total")
    private BigDecimal valorTotal;
    @Column(name = "desconto")
    private BigDecimal desconto;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario usuario;
    
    @OneToMany(cascade= CascadeType.ALL, fetch= FetchType.EAGER)
    @JoinColumn(name="pedido_id")
    private Collection<PedidoProduto> itens;
    
    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name="pedido_id")
    private Collection<PedidoPag> pagamentos;

    public Pedido() {
    }

    public Collection<PedidoPag> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(Collection<PedidoPag> pagamentos) {
        this.pagamentos = pagamentos;
    }

    
    public Collection<PedidoProduto> getItens() {
        return itens;
    }

    public void setItens(Collection<PedidoProduto> itens) {
        this.itens = itens;
    }


    public Pedido(Integer id) {
        this.id = id;
    }

    public Pedido(Integer id, BigDecimal valorTotal, short status) {
        this.id = id;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public PedidoProduto getVeiculo(){

        for(PedidoProduto it : itens){
            if(it.getProduto().getTipo() == Produto.VEICULO){
                
                return (it);
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedido)) {
            return false;
        }
        Pedido other = (Pedido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Pedido[ id=" + id + " ]";
    }
    
}
