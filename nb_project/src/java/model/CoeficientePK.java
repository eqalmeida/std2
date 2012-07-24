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
public class CoeficientePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "tabela_financ_id")
    private short tabelaFinanc;
    @Basic(optional = false)
    @Column(name = "num_parcelas")
    private short numParcelas;

    public CoeficientePK() {
    }

    public CoeficientePK(short tabelaFinancId, short numParcelas) {
        this.tabelaFinanc = tabelaFinancId;
        this.numParcelas = numParcelas;
    }

    public short getTabelaFinanc() {
        return tabelaFinanc;
    }

    public void setTabelaFinanc(short tabelaFinanc) {
        this.tabelaFinanc = tabelaFinanc;
    }

    public short getNumParcelas() {
        return numParcelas;
    }

    public void setNumParcelas(short numParcelas) {
        this.numParcelas = numParcelas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) tabelaFinanc;
        hash += (int) numParcelas;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoeficientePK)) {
            return false;
        }
        CoeficientePK other = (CoeficientePK) object;
        if (this.tabelaFinanc != other.tabelaFinanc) {
            return false;
        }
        if (this.numParcelas != other.numParcelas) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CoeficientePK[ tabelaFinancId=" + tabelaFinanc + ", numParcelas=" + numParcelas + " ]";
    }
    
}
