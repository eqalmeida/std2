/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author eqalmeida
 */
@Entity
@Table(name = "pagto_recebido")
@NamedQueries({
    @NamedQuery(name = "PagtoRecebido.findAll", query = "SELECT p FROM PagtoRecebido p")})
public class PagtoRecebido implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Column(name = "data_informada")
    @Temporal(TemporalType.DATE)
    private Date dataInformada;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor")
    private BigDecimal valor;
    @JoinColumn(name = "receb_usuario_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuario recebUsuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_pag_id", referencedColumnName = "id")
    private PedidoPag pedidoPag;
    
    public PagtoRecebido() {
    }
    
    public PagtoRecebido(Integer id) {
        this.id = id;
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
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    
    public Usuario getRecebUsuario() {
        return recebUsuario;
    }
    
    public void setRecebUsuario(Usuario recebUsuario) {
        this.recebUsuario = recebUsuario;
    }

    public PedidoPag getPedidoPag() {
        return pedidoPag;
    }

    public void setPedidoPag(PedidoPag pedidoPag) {
        this.pedidoPag = pedidoPag;
    }

    
    public Date getDataInformada() {
        return dataInformada;
    }
    
    public void setDataInformada(Date dataInformada) {
        this.dataInformada = dataInformada;
    }
    
    public void calculaValorDevido() {
/*        
        if (dataInformada == null) {
            return;
        }
        
        if (pedidoPag == null) {
            return;
        }
        
        valorDevido = BigDecimal.ZERO;
        juros = BigDecimal.ZERO;
        multa = BigDecimal.ZERO;
        
        for (Boleto boleto : pedidoPag.getParcelas()) {
            
            if(boleto.isAtrasado(dataInformada)){
                boleto.setDataPag(dataInformada);
                boleto.calculaJuros();
                valorDevido = valorDevido.add(boleto.getValor());
                juros = juros.add(boleto.getJuros());
                multa = multa.add(boleto.getMulta());
            }
            
        }
  */      
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
        if (!(object instanceof PagtoRecebido)) {
            return false;
        }
        PagtoRecebido other = (PagtoRecebido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "model.PagtoRecebido[ id=" + id + " ]";
    }
}
