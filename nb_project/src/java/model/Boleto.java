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
import java.util.HashMap;
import java.util.Map;
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
import javax.persistence.Transient;
import util.Util;

/**
 *
 * @author eqalmeida
 */
@Entity
@Table(name = "boleto")
@NamedQueries({
    @NamedQuery(name = "Boleto.findAll", query = "SELECT b FROM Boleto b")})
public class Boleto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // Opções de Status
    public static final Short ATIVO         = 0;
    public static final Short PAGO          = 1;
    public static final Short PAGO_PARCIAL  = 2;
    public static final Short CANCELADO     = 3;
    public static final Short PARADO        = 4;
    
    public static final Map<Short,String> listStatus = new HashMap<Short, String>();
    static{
        listStatus.put(ATIVO, "ATIVO");
        listStatus.put(PAGO, "PAGO");
        listStatus.put(PAGO_PARCIAL, "PAGO PARCIAL");
        listStatus.put(CANCELADO, "CANCELADO");
        listStatus.put(PARADO, "PARADO");
    }
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "vencimento")
    @Temporal(TemporalType.DATE)
    private Date vencimento;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "valor")
    private BigDecimal valor;
    @Basic(optional = false)
    @Column(name = "status")
    private short status;
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cliente cliente;
    @JoinColumn(name = "pedido_pag_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PedidoPag pedidoPag;
    @Column(name = "parcela_n")
    private short numParcela;
    @Column(name = "juros")
    private BigDecimal juros;
    @Column(name = "multa")
    private BigDecimal multa;
    @Column(name = "valor_pago")
    private BigDecimal valorPago;
    @Column(name = "valor_faltante")
    private BigDecimal valorFaltante;
    @Column(name = "data_pag")
    @Temporal(TemporalType.DATE)
    private Date dataPag;
    @javax.persistence.Transient
    private Short situacao = null;
    
    @Transient
    private BigDecimal valorComMulta;

    public Boleto() {
    }

    public BigDecimal getValorComMulta() {
        if(this.pedidoPag != null){
            double fator = (this.pedidoPag.getMultaPercent()/100.0) + 1.0;
            valorComMulta = this.valor.multiply(new BigDecimal(fator));
        }
        return valorComMulta;
    }
    
    

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public BigDecimal getJuros() {
        return juros;
    }

    public void setJuros(BigDecimal juros) {
        this.juros = juros;
    }

    public BigDecimal getMulta() {
        return multa;
    }

    public void setMulta(BigDecimal multa) {
        this.multa = multa;
    }

    public BigDecimal getValorFaltante() {
        return valorFaltante;
    }

    public void setValorFaltante(BigDecimal valorFaltante) {
        this.valorFaltante = valorFaltante;
    }

    public Date getDataPag() {
        return dataPag;
    }

    public void setDataPag(Date dataPag) {
        this.dataPag = dataPag;
    }

    public Boleto(Integer id) {
        this.id = id;
    }

    public Boleto(Integer id, Date vencimento, BigDecimal valor, short status) {
        this.id = id;
        this.vencimento = vencimento;
        this.valor = valor;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public PedidoPag getPedidoPag() {
        return pedidoPag;
    }

    public void setPedidoPag(PedidoPag pedidoPag) {
        this.pedidoPag = pedidoPag;
    }

    public short getNumParcela() {
        return numParcela;
    }

    public void setNumParcela(short numParcela) {
        this.numParcela = numParcela;
    }

    public Short getSituacao() {
        if (situacao == null) {
            if (this.status > 0) {
                situacao = 0;
            } else {

                if (this.getVencimento().before(new Date())) {
                    situacao = 1;
                } else {
                    situacao = 0;
                }

            }
        }
        return situacao;
    }

    /**
     * Converte a Data em um número que representa os dias
     *
     */
    private long getDias(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long d = (c.getTimeInMillis() / (24 * 60 * 60 * 1000));
        return d;
    }

    public boolean isAtrasado() {
        return (this.isAtrasado(new Date()));
    }
    
    public boolean isAtrasado(Date data) {

        if (status == Boleto.CANCELADO || status == Boleto.PAGO) {
            return false;
        }

        long diasDataPag = getDias(data);
        long diasVencimento = getDias(vencimento);
        
        long atraso;
        if(status == ATIVO || status == Boleto.PARADO){
            atraso = diasDataPag - diasVencimento;
        }else{
            long atual = getDias(getDataPag());
            
            if(atual > diasVencimento){
                atraso = diasDataPag - atual;
            }else{
                atraso = diasDataPag - diasVencimento;
            }
        }
        
        if (atraso >= 0) {
            return true;
        }

        return false;

    }
    
    public BigDecimal getValorAtual(){
        if(this.getStatus() == ATIVO){
            return (getValor());
        }
        else if(this.getStatus() == PAGO_PARCIAL){
            return (getValorFaltante());
        }
        else{
            return (BigDecimal.ZERO);
        }
    }
    
    /**
     * Retorna o valor devido na Data informada
     * @param dataInf Data informada
     * @return Valor
     */
    public BigDecimal getValorAtualComTaxas(Date dataInf, double desconto){
        
        BigDecimal jurosAtual = getJuros(dataInf, desconto);
        BigDecimal multaAtual = getMulta(dataInf, desconto);
        
        return(getValorAtual().add(jurosAtual).add(multaAtual));
    }
    
    public BigDecimal getValorDevido(){
        if(this.status == CANCELADO || this.status == PAGO){
            return BigDecimal.ZERO;
        }
        else if(this.status == ATIVO){
            return this.valor;
        }
        else if(this.status == PAGO_PARCIAL){
            return this.valorFaltante;
        }else{
            return this.valor;
        }
    }

    public BigDecimal getValorDevido(Date data, double desconto) {

        BigDecimal val = BigDecimal.ZERO;

        if (status == ATIVO) {
            val = getValor();
        } else if (status == PAGO_PARCIAL) {
            val = getValorFaltante();
        }

        val = val.add(getJuros(data, desconto)).add(getMulta(data, desconto));

        return val;
    }

    public BigDecimal getJuros(Date data, double desconto) {
        
        Double j = this.getPedidoPag().getJurosDiario();
        
        if(j == null || j == 0.0){
            return (BigDecimal.ZERO);
        }

        long diasDataPag = getDias(data);

        if (status == ATIVO) {

            long diasVencimento = getDias(vencimento);
            long atraso = diasDataPag - diasVencimento;


            if (atraso > 0) {
                BigDecimal temp = getValor().multiply(new BigDecimal(atraso)).multiply(new BigDecimal(j)).divide(new BigDecimal(100.0)).setScale(2, RoundingMode.DOWN);
                temp = Util.valComDesconto(temp, desconto);
                return (temp);

            } else {
                return (BigDecimal.ZERO);
            }
        } else if (status == PAGO_PARCIAL) {

            long diasUltPag = getDias(dataPag);
            long diasVencimentoPrincipal = getDias(vencimento);
            
            if(diasVencimentoPrincipal >= diasDataPag){
                return BigDecimal.ZERO;
            }

            if(diasUltPag >= diasDataPag){
                return BigDecimal.ZERO;
            }

            long atraso;

            if (diasVencimentoPrincipal > diasUltPag) {
                atraso = diasDataPag - diasVencimentoPrincipal;
            } else {
                atraso = diasDataPag - diasUltPag;
            }


            if (atraso > 0) {
                BigDecimal temp = getValorFaltante().multiply(new BigDecimal(atraso)).multiply(new BigDecimal(j)).divide(new BigDecimal(100.0)).setScale(2, RoundingMode.DOWN);
                temp = Util.valComDesconto(temp, desconto);
                return (temp);
            } else {
                return (BigDecimal.ZERO);
            }
        }
        return (BigDecimal.ZERO);
    }

    public BigDecimal getMulta(Date data, double desconto) {
        
        Double m = this.getPedidoPag().getMultaPercent();
        BigDecimal mVal = this.getPedidoPag().getMultaVal();
        
        if(m == null){
            m = 0.0;
        }
        
        if(mVal == null){
            mVal = BigDecimal.ZERO;
        }
        
        if(m == 0.0 && mVal.doubleValue() == 0.0){
            return (BigDecimal.ZERO);
        }

        long diasDataPag = getDias(data);

        if (status == ATIVO) {

            long diasVencimento = getDias(vencimento);
            long atraso = diasDataPag - diasVencimento;


            if (atraso > 0) {
                BigDecimal temp = getValor().multiply(new BigDecimal(m)).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
                temp = temp.add(mVal);
                temp = Util.valComDesconto(temp, desconto);
                return (temp);

            } else {
                return (BigDecimal.ZERO);
            }

        }
        return (BigDecimal.ZERO);
    }

    public String getStatusStr() {
        return listStatus.get(this.status);
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
        if (!(object instanceof Boleto)) {
            return false;
        }
        Boleto other = (Boleto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Boleto[ id=" + id + " ]";
    }
}
