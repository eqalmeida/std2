/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eqalmeida
 */
@Entity
@Table(name = "cliente")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c")})
public class Cliente implements Serializable {
        @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    private Collection<Pedido> pedidoCollection;
        @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    private List<Pedido> pedidoList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Column(name = "tel_com")
    private String telCom;
    @Column(name = "tel_res")
    private String telRes;
    @Column(name = "tel_cel")
    private String telCel;
    @Column(name = "rg")
    private String rg;
    @Column(name = "cpf")
    private String cpf;
    @Column(name = "end_rua")
    private String endRua;
    @Column(name = "end_num")
    private String endNum;
    @Column(name = "end_compl")
    private String endCompl;
    @Column(name = "end_bairro")
    private String endBairro;
    @Column(name = "end_municipio")
    private String endMunicipio;
    @Column(name = "end_cep")
    private String endCep;
    @Lob
    @Column(name = "obs")
    private String obs;

    public Cliente() {
    }

    public Cliente(Integer id) {
        this.id = id;
    }

    public Cliente(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getEndereco() {
        
        String end = "";
        
        if(this.endRua != null && this.endRua.isEmpty() == false){
            end += this.endRua ;
        }
        
        if(this.endNum != null && this.endNum.isEmpty() == false){
            end += (", " + this.endNum ) ;
        }
        
        if(this.endCompl != null && this.endCompl.isEmpty() == false){
            end += (" - " + this.endCompl ) ;
        }
        
        if(this.endBairro != null && this.endBairro.isEmpty() == false){
            end += (" - " + this.endBairro ) ;
        }
        
        return (end);
    }
    
    public String getTelefones(){
        String tels = "";
        if(this.telRes != null && this.telRes.isEmpty() == false){
            tels += ("res.: " + this.telRes + "  ");
        }
        if(this.telCel != null && this.telCel.isEmpty() == false ){
            
            if(!tels.isEmpty()){
                tels += " / ";
            }
            
            tels += ("cel.: " + this.telCel + "  ");
        }
        if(this.telCom != null && this.telCom.isEmpty() == false){

            if(!tels.isEmpty()){
                tels += " / ";
            }
            
            tels += ("com.: " + this.telCom + "  ");
        }
        return tels;
    }
            

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelCom() {
        return telCom;
    }

    public void setTelCom(String telCom) {
        this.telCom = telCom;
    }

    public String getTelRes() {
        return telRes;
    }

    public void setTelRes(String telRes) {
        this.telRes = telRes;
    }

    public String getTelCel() {
        return telCel;
    }

    public void setTelCel(String telCel) {
        this.telCel = telCel;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        if(rg != null && rg.length() == 0){
            rg = null;
        }
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if(cpf != null && cpf.length() == 0){
            cpf = null;
        }
        this.cpf = cpf;
    }

    public String getEndRua() {
        return endRua;
    }

    public void setEndRua(String endRua) {
        this.endRua = endRua;
    }

    public String getEndNum() {
        return endNum;
    }

    public void setEndNum(String endNum) {
        this.endNum = endNum;
    }

    public String getEndCompl() {
        return endCompl;
    }

    public void setEndCompl(String endCompl) {
        this.endCompl = endCompl;
    }

    public String getEndBairro() {
        return endBairro;
    }

    public void setEndBairro(String endBairro) {
        this.endBairro = endBairro;
    }

    public String getEndMunicipio() {
        return endMunicipio;
    }

    public void setEndMunicipio(String endMunicipio) {
        this.endMunicipio = endMunicipio;
    }

    public String getEndCep() {
        return endCep;
    }

    public void setEndCep(String endCep) {
        this.endCep = endCep;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
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
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

    @XmlTransient
    public List<Pedido> getPedidoList() {
        return pedidoList;
    }

    public void setPedidoList(List<Pedido> pedidoList) {
        this.pedidoList = pedidoList;
    }

    @XmlTransient
    public Collection<Pedido> getPedidoCollection() {
        return pedidoCollection;
    }

    public void setPedidoCollection(Collection<Pedido> pedidoCollection) {
        this.pedidoCollection = pedidoCollection;
    }
}
