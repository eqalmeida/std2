/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
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
@Table(name = "config")
@NamedQueries({
    @NamedQuery(name = "Config.findAll", query = "SELECT c FROM Config c")})
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "empresa_nome")
    private String empresaNome;
    @Column(name = "empresa_endereco")
    private String empresaEndereco;
    @Column(name = "empresa_telefones")
    private String empresaTelefones;
    @Column(name = "empresa_email")
    private String empresaEmail;
    @Column(name = "empresa_homepage")
    private String empresaHomepage;
    @Column(name = "boletos_por_pag")
    private Short boletosPorPag;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    
    @Column(name="carta_autoriz")
    private String cartaAutoriz;

    public Config() {
    }

    public String getCartaAutoriz() {
        return cartaAutoriz;
    }

    public void setCartaAutoriz(String cartaAutoriz) {
        this.cartaAutoriz = cartaAutoriz;
    }

    public Config(Short id) {
        this.id = id;
    }

    public String getEmpresaNome() {
        return empresaNome;
    }

    public void setEmpresaNome(String empresaNome) {
        this.empresaNome = empresaNome;
    }

    public String getEmpresaEndereco() {
        return empresaEndereco;
    }

    public void setEmpresaEndereco(String empresaEndereco) {
        this.empresaEndereco = empresaEndereco;
    }

    public String getEmpresaTelefones() {
        return empresaTelefones;
    }

    public void setEmpresaTelefones(String empresaTelefones) {
        this.empresaTelefones = empresaTelefones;
    }

    public String getEmpresaEmail() {
        return empresaEmail;
    }

    public void setEmpresaEmail(String empresaEmail) {
        this.empresaEmail = empresaEmail;
    }

    public String getEmpresaHomepage() {
        return empresaHomepage;
    }

    public void setEmpresaHomepage(String empresaHomepage) {
        this.empresaHomepage = empresaHomepage;
    }

    public Short getBoletosPorPag() {
        return boletosPorPag;
    }

    public void setBoletosPorPag(Short boletosPorPag) {
        this.boletosPorPag = boletosPorPag;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
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
        if (!(object instanceof Config)) {
            return false;
        }
        Config other = (Config) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Config[ id=" + id + " ]";
    }
    
}
