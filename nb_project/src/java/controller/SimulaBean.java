package controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Coeficiente;
import model.Produto;
import model.TabelaFinanc;
import model.TipoPagto;
import repo.CoeficienteJpaController;
import repo.ProdutoJpaController;
import repo.TabelaFinancJpaController;
import repo.TipoPagtoJpaController;
import util.Util;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "simulaMB")
@ViewScoped
public class SimulaBean extends ControllerBase implements Serializable{

    private List<Produto> produtos;
    private List<TipoPagto> tipoPagtos;
    private List<TabelaFinanc> tabelasFinanc;
    private int produtoId;
    private Short tipoId;
    private Short tabelaId;
    private BigDecimal valorVeiculo;
    private BigDecimal valorEntrada;
    private BigDecimal valorTac;
    private Produto veiculo;
    private List<String> parcelas;
    private TipoPagto tipoPagto;
    private boolean mostrar = false;

    /**
     * Creates a new instance of SimulaBean
     */
    public SimulaBean() {
    }

    public List<Produto> getProdutos() {
        produtos = new ProdutoJpaController().findVeiculosEmEstoque();
        return produtos;
    }

    public List<TipoPagto> getTipoPagtos() {
        tipoPagtos = new TipoPagtoJpaController().findTipoPagtoEntities();
        return tipoPagtos;
    }

    public List<TabelaFinanc> getTabelasFinanc() {
        tabelasFinanc = new ArrayList<TabelaFinanc>();
        if (tipoId != null) {
            tipoPagto = new TipoPagtoJpaController().findTipoPagto(tipoId);
            if (tipoPagto != null) {
                tabelasFinanc.addAll(
                        tipoPagto.getTabelasFinanc());
            }
        }
        return tabelasFinanc;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public Short getTipoId() {
        return tipoId;
    }

    public void setTipoId(Short tipoId) {
        this.tipoId = tipoId;
    }

    public Short getTabelaId() {
        return tabelaId;
    }

    public void setTabelaId(Short tabelaId) {
        this.tabelaId = tabelaId;
    }

    public BigDecimal getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(BigDecimal valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public BigDecimal getValorTac() {
        return valorTac;
    }

    public void setValorTac(BigDecimal valorTac) {
        this.valorTac = valorTac;
    }

    /**
     * Gerar Simulação
     */
    public void gerar() {
        TabelaFinanc tabela;

        mostrar = false;


        parcelas = new ArrayList<String>();
        veiculo = new ProdutoJpaController().findProduto(produtoId);
        if (veiculo == null) {
            return;
        }

        tipoPagto = new TipoPagtoJpaController().findTipoPagto(tipoId);
        if (tipoPagto == null) {
            return;
        }

        tabela = new TabelaFinancJpaController(getEmf()).findTabelaFinanc(tabelaId);
        if (tabela == null) {
            return;
        }

        BigDecimal valorTotal = valorVeiculo.add(valorTac).subtract(valorEntrada);

        List<Coeficiente> coeficientes = new CoeficienteJpaController(getEmf()).findCoeficienteEntities(tabelaId);

        NumberFormat fm = NumberFormat.getCurrencyInstance();
        
        for (Coeficiente c : coeficientes) {
            String str = c.getCoeficientePK().getNumParcelas() + " X ";
            
            BigDecimal val = valorTotal.multiply(new BigDecimal(c.getCoeficiente()));
            
            str += fm.format(val);
            
            parcelas.add(str);
        }

        mostrar = true;

    }

    public Produto getVeiculo() {
        return veiculo;
    }

    public List<String> getParcelas() {
        return parcelas;
    }

    public boolean isMostrar() {
        return mostrar;
    }

    public BigDecimal getValorVeiculo() {
        return valorVeiculo;
    }

    public void veiculoChanged(){
        // Atualiza o valor
        veiculo = new ProdutoJpaController().findProduto(produtoId);
        if(veiculo != null){
            valorVeiculo = veiculo.getValor();
        }
    }
    
    public void setValorVeiculo(BigDecimal valorVeiculo) {
        this.valorVeiculo = valorVeiculo;
    }

    public TipoPagto getTipoPagto() {
        return tipoPagto;
    }

    public Date getData(){
        return new Date();
    }
}