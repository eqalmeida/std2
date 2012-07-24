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
@Table(name = "boleto")
@NamedQueries({
    @NamedQuery(name = "Boleto.findAll", query = "SELECT b FROM Boleto b")})
public class Boleto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static Short ATIVO = 0;
    public static Short PAGO = 1;
    public static Short PAGO_PARCIAL = 2;
    public static Short CANCELADO = 3;
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "vencimento")
    @Temporal(TemporalType.DATE)
    private Date vencimento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "pedido_pag_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PedidoPag pedidoPag;

    @Column(name = "parcela_n")
    private short numParcela;
    
    @Column(name = "valorPago")
    private BigDecimal valorPago;
    
    @Column(name = "valorFaltante")
    private BigDecimal valorFaltante;

    @Column(name = "dataPag")
    @Temporal(TemporalType.DATE)
    private Date dataPag;
    
    @javax.persistence.Transient
    private Short situacao = null;
    

    public Boleto() {
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getValorFaltante() {
        return valorFaltante;
    }

    public void setValorFaltante(BigDecimal valorFaltante) {
        this.valorFaltante = valorFaltante;
    }

    public Date getDataPag() {
        return dataPag;
    }

    public void setDataPag(Date dataPag) {
        this.dataPag = dataPag;
    }

    
    public Boleto(Integer id) {
        this.id = id;
    }

    public Boleto(Integer id, Date vencimento, BigDecimal valor, short status) {
        this.id = id;
        this.vencimento = vencimento;
        this.valor = valor;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
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

    public PedidoPag getPedidoPag() {
        return pedidoPag;
    }

    public void setPedidoPag(PedidoPag pedidoPag) {
        this.pedidoPag = pedidoPag;
    }

    public short getNumParcela() {
        return numParcela;
    }

    public void setNumParcela(short numParcela) {
        this.numParcela = numParcela;
    }

    public Short getSituacao() {
        if (situacao == null) {
            if (this.status > 0) {
                situacao = 0;
            } else {

//                Calendar data = Calendar.getInstance();

                if (this.getVencimento().before( new Date() )) {
                    situacao = 1;
                }else{
                    situacao = 0;
                }

            }
        }
        return situacao;
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
        if (!(object instanceof Boleto)) {
            return false;
        }
        Boleto other = (Boleto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Boleto[ id=" + id + " ]";
    }
}
