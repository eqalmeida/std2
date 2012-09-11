/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
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

/**
 *
 * @author eqalmeida
 */
@Entity
@Table(name = "produto")
@NamedQueries({
    @NamedQuery(name = "Produto.findAll", query = "SELECT p FROM Produto p")})
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final short OUTRO = 0;
    public static final short VEICULO = 1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "descricao")
    private String descricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @Column(name = "tipo")
    private short tipo;
    @Column(name = "fabricante")
    private String fabricante;
    @Column(name = "modelo")
    private String modelo;
    @Column(name = "ano")
    private String ano;
    @Column(name = "cor")
    private String cor;
    @Column(name = "cilindradas")
    private Short cilindradas;
    @Column(name = "placa")
    private String placa;
    @Column(name = "chassi")
    private String chassi;
    @Column(name = "combustivel")
    private Short combustivel;
    @Column(name = "qtd_estoque")
    private Integer qtdEstoque;
    @Lob
    @Column(name = "obs")
    private String obs;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produto")
    private Collection<PedidoProduto> pedidoProdutoCollection;
    private static Map<Short, String> combustivelList = null;

    public static Map<Short, String> getCombustivelList() {

        if (combustivelList == null) {
            combustivelList = new LinkedHashMap<Short, String>();
            combustivelList.put((short) 1, "Gasolina");
            combustivelList.put((short) 2, "Álcool");
            combustivelList.put((short) 3, "Diesel");
            combustivelList.put((short) 4, "Flex");
            combustivelList.put((short) 5, "Gás");
            combustivelList.put((short) 6, "Outro");
        }

        return combustivelList;
    }
    
    public String getMarcaModelo(){
        return (this.fabricante + " " + this.modelo);
    }
    
    public String getValorStr(){
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(this.valor);
    }
            

    public String getCombustivelStr() {
        if (getCombustivelList().containsKey(this.combustivel)) {
            return (getCombustivelList().get(this.combustivel));
        }
        return null;
    }

    public Produto() {
        this.qtdEstoque = 1;
    }

    public Produto(Integer id) {
        this.id = id;
    }

    public Produto(Integer id, short tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDescricaoGeral() {
        String str = "";
        if (this.tipo == 1) {

            return (this.fabricante + " " + this.modelo + " - " + this.ano + " (" + this.placa + ")");
        } else {
            return this.descricao;
        }
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public short getTipo() {
        return tipo;
    }

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Short getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(Short cilindradas) {
        this.cilindradas = cilindradas;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        if (!placa.isEmpty()) {
            placa = placa.toUpperCase();
        }
        this.placa = placa;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        if (chassi != null) {
            if (chassi.length() == 0) {
                chassi = null;
            }
        }
        this.chassi = chassi;
    }

    public Short getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(Short combustivel) {
        this.combustivel = combustivel;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Collection<PedidoProduto> getPedidoProdutoCollection() {
        return pedidoProdutoCollection;
    }

    public void setPedidoProdutoCollection(Collection<PedidoProduto> pedidoProdutoCollection) {
        this.pedidoProdutoCollection = pedidoProdutoCollection;
    }

    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(Integer qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
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
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getDescricaoGeral();
    }
}
