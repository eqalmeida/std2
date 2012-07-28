/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author eqalmeida
 */
@Entity
@Table(name = "tipo_pagto")
@NamedQueries({
    @NamedQuery(name = "TipoPagto.findAll", query = "SELECT t FROM TipoPagto t")})
public class TipoPagto implements Serializable {
    @JoinTable(name = "tipo_pagto_tabelas", joinColumns = {
        @JoinColumn(name = "tipo_pagto_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "tabela_financ_id", referencedColumnName = "id")})
    @ManyToMany
    private Collection<TabelaFinanc> tabelasFinanc;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "descricao")
    private String descricao;
    @Basic(optional = false)
    @Column(name = "gera_boleto")
    private boolean geraBoleto;

    @Column(name="multa_percent")
    private Double multaAtraso;

    @Column(name="juros_diario_percent")
    private Double jurosDiario;
    
    public TipoPagto() {
        this.geraBoleto = true;
    }

    public TipoPagto(Short id) {
        this.id = id;
    }

    public TipoPagto(Short id, String descricao, boolean geraBoleto) {
        this.id = id;
        this.descricao = descricao;
        this.geraBoleto = geraBoleto;
    }

    public Double getMultaAtraso() {
        return multaAtraso;
    }

    public void setMultaAtraso(Double multaAtraso) {
        this.multaAtraso = multaAtraso;
    }

    public Double getJurosDiario() {
        return jurosDiario;
    }

    public void setJurosDiario(Double jurosDiario) {
        this.jurosDiario = jurosDiario;
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

    public boolean getGeraBoleto() {
        return geraBoleto;
    }

    public void setGeraBoleto(boolean geraBoleto) {
        this.geraBoleto = geraBoleto;
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
        if (!(object instanceof TipoPagto)) {
            return false;
        }
        TipoPagto other = (TipoPagto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public Collection<TabelaFinanc> getTabelasFinanc() {
        return tabelasFinanc;
    }

    public void setTabelasFinanc(Collection<TabelaFinanc> tabelasFinanc) {
        this.tabelasFinanc = tabelasFinanc;
    }
    
}
