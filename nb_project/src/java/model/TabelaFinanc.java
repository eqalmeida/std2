/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author eqalmeida
 */
@Entity
@Table(name = "tabela_financ")
@NamedQueries({
    @NamedQuery(name = "TabelaFinanc.findAll", query = "SELECT t FROM TabelaFinanc t")})
public class TabelaFinanc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Column(name = "descricao")
    private String descricao;
    

    @Column(name="multa_percent")
    private Double multaPercent;

    @Column(name="multa_val")
    private BigDecimal multaVal;

    @Column(name="juros_diario")
    private Double jurosDiario;
    
    @Column(name="desconto_maximo")
    private Double descontoMaximo;
    

    public TabelaFinanc() {
    }

    public TabelaFinanc(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Double getDescontoMaximo() {
        return descontoMaximo;
    }

    public void setDescontoMaximo(Double descontoMaximo) {
        this.descontoMaximo = descontoMaximo;
    }
    
    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TabelaFinanc)) {
            return false;
        }
        TabelaFinanc other = (TabelaFinanc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
