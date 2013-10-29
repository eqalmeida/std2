/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.Acrescimos;

/**
 *
 * @author eqalmeida
 */
@Entity
@Table(name = "pedido_pag")
@NamedQueries({
    @NamedQuery(name = "PedidoPag.findAll", query = "SELECT p FROM PedidoPag p")})
public class PedidoPag implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @Column(name = "num_parcelas")
    private short numParcelas;
    @Basic(optional = false)
    @Column(name = "data_venc")
    @Temporal(TemporalType.DATE)
    private Date dataVenc;
    @Basic(optional = false)
    @Column(name = "frequencia")
    private short frequencia;
    
    
    @JoinColumn(name = "tabela_financ_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TabelaFinanc tabelaFinanc;
    @JoinColumn(name = "pedido_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Pedido pedido;
    @JoinColumn(name = "tipo_pagto_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoPagto tipoPagto;
    
    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name = "pedido_pag_id")
    @OrderBy(value="vencimento")
    private Collection<Boleto> parcelas;

    @Column(name="multa_percent")
    private Double multaPercent;

    @Column(name="multa_val")
    private BigDecimal multaVal;

    @Column(name="juros_diario")
    private Double jurosDiario;
    
    @javax.persistence.Transient
    private BigDecimal valorParcela;
    

    
    @OneToMany
    @JoinColumn(name="pedido_pag_id")
    @OrderBy(value="dataInformada")
    private Collection<PagtoRecebido> pagamentos;

    public Collection<Boleto> getParcelas() {
        return parcelas;
    }

    public void setParcelas(Collection<Boleto> parcelas) {
        this.parcelas = parcelas;
    }
    
    

    public PedidoPag() {
    }

    public PedidoPag(Integer id) {
        this.id = id;
    }

    public PedidoPag(Integer id, BigDecimal valor, short numParcelas, Date dataVenc, short frequencia) {
        this.id = id;
        this.valor = valor;
        this.numParcelas = numParcelas;
        this.dataVenc = dataVenc;
        this.frequencia = frequencia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public short getNumParcelas() {
        return numParcelas;
    }

    public void setNumParcelas(short numParcelas) {
        this.numParcelas = numParcelas;
    }

    public Date getDataVenc() {
        return dataVenc;
    }

    public void setDataVenc(Date dataVenc) {
        this.dataVenc = dataVenc;
    }

    public short getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(short frequencia) {
        this.frequencia = frequencia;
    }

    public TabelaFinanc getTabelaFinanc() {
        return tabelaFinanc;
    }

    public void setTabelaFinanc(TabelaFinanc tabelaFinanc) {
        this.tabelaFinanc = tabelaFinanc;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public TipoPagto getTipoPagto() {
        return tipoPagto;
    }

    public void setTipoPagto(TipoPagto tipoPagto) {
        this.tipoPagto = tipoPagto;
    }

    public Collection<PagtoRecebido> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(Collection<PagtoRecebido> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public Double getMultaPercent() {
        return multaPercent;
    }

    public void setMultaPercent(Double multaPercent) {
        this.multaPercent = multaPercent;
    }

    public BigDecimal getMultaVal() {
        return multaVal;
    }

    public void setMultaVal(BigDecimal multaVal) {
        this.multaVal = multaVal;
    }

    public Double getJurosDiario() {
        return jurosDiario;
    }

    public void setJurosDiario(Double jurosDiario) {
        this.jurosDiario = jurosDiario;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public BigDecimal getValorDevidoAtual(Date d){
        
        Acrescimos acrescimos = new Acrescimos(d);
        
        long pagDays = Acrescimos.dateToDays(d);

        //TODO: COrrigir o tratamento da data de vencimento quando pago parcial.
        for(Boleto b : this.getParcelas()){
            
            long parcelaDays = Acrescimos.dateToDays(b.getVencimentoAtual());
            
            if(parcelaDays >= pagDays){
                acrescimos.addicionaParcela(b);
            }
        }
        return(acrescimos.getTotalDevido());
    }

    public BigDecimal getTaxasAtual(Date d){
        
        Acrescimos acrescimos = new Acrescimos(d);
        for(Boleto b : this.getParcelas()){
            acrescimos.addicionaParcela(b);
        }
        return(acrescimos.getTotalJuros().add(acrescimos.getTotalMultas()));
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
        if (!(object instanceof PedidoPag)) {
            return false;
        }
        PedidoPag other = (PedidoPag) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.PedidoPag[ id=" + id + " ]";
    }
}
