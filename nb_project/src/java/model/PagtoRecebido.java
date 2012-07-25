/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import util.Util;

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
    @Column(name = "valor_devido")
    private BigDecimal valorDevido;
    @Column(name = "multa")
    private BigDecimal multa;
    @Column(name = "juros")
    private BigDecimal juros;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @JoinColumn(name = "receb_usuario_id", referencedColumnName = "id")
    @ManyToOne
    private Usuario recebUsuario;
    @JoinColumn(name = "boleto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Boleto boleto;
    
    public PagtoRecebido() {
    }
    
    public PagtoRecebido(Integer id) {
        this.id = id;
    }
    
    public PagtoRecebido(Integer id, BigDecimal valor, short status) {
        this.id = id;
        this.valor = valor;
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
    
    public Usuario getRecebUsuario() {
        return recebUsuario;
    }
    
    public void setRecebUsuario(Usuario recebUsuario) {
        this.recebUsuario = recebUsuario;
    }

    public Boleto getBoleto() {
        return boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }
    
    
    public Date getDataInformada() {
        return dataInformada;
    }
    
    public void setDataInformada(Date dataInformada) {
        this.dataInformada = dataInformada;
    }
    
    public BigDecimal getValorDevido() {
        return valorDevido;
    }
    
    public void setValorDevido(BigDecimal valorDevido) {
        this.valorDevido = valorDevido;
    }
    
    public BigDecimal getMulta() {
        return multa;
    }
    
    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }
    
    public BigDecimal getJuros() {
        return juros;
    }
    
    public void setJuros(BigDecimal juros) {
        this.juros = juros;
    }
    
    public BigDecimal getValorTotal() {
        if (valorDevido == null) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal val = valorDevido;
        
        if (juros != null) {
            val = val.add(juros);
        }
        
        if (multa != null) {
            val = val.add(multa);
        }
        
        return val;
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
