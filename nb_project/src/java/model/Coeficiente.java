/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
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
@Table(name = "coeficiente")
@NamedQueries({
    @NamedQuery(name = "Coeficiente.findAll", query = "SELECT c FROM Coeficiente c")})
public class Coeficiente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CoeficientePK coeficientePK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coeficiente")
    private Float coeficiente;
    @JoinColumn(name = "tabela_financ_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private TabelaFinanc tabelaFinanc;

    public Coeficiente() {
    }

    public Coeficiente(CoeficientePK coeficientePK) {
        this.coeficientePK = coeficientePK;
    }

    public Coeficiente(short tabelaFinancId, short numParcelas) {
        this.coeficientePK = new CoeficientePK(tabelaFinancId, numParcelas);
    }

    public CoeficientePK getCoeficientePK() {
        return coeficientePK;
    }

    public void setCoeficientePK(CoeficientePK coeficientePK) {
        this.coeficientePK = coeficientePK;
    }

    public Float getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(Float coeficiente) {
        this.coeficiente = coeficiente;
    }

    public TabelaFinanc getTabelaFinanc() {
        return tabelaFinanc;
    }

    public void setTabelaFinanc(TabelaFinanc tabelaFinanc) {
        this.tabelaFinanc = tabelaFinanc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coeficientePK != null ? coeficientePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Coeficiente)) {
            return false;
        }
        Coeficiente other = (Coeficiente) object;
        if ((this.coeficientePK == null && other.coeficientePK != null) || (this.coeficientePK != null && !this.coeficientePK.equals(other.coeficientePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Coeficiente[ coeficientePK=" + coeficientePK + " ]";
    }
    
}
