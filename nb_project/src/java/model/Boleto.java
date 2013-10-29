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
import java.util.GregorianCalendar;
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
import util.Acrescimos;
import util.CurrencyWriter;
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
    
    public String getValorPorExtenso(){
        CurrencyWriter cw = new CurrencyWriter();
        return(cw.write(this.getValor()));
    }

    public int getVencimentoDia() {
        Calendar data = Calendar.getInstance();
        data.setTime(vencimento);
        return data.get(Calendar.DATE);
    }

    public String getVencimentoMes() {
        Calendar data = Calendar.getInstance();
        data.setTime(vencimento);
        int mes = data.get(Calendar.MONTH);
        return Util.monthNames.get(mes);
    }

    public int getVencimentoAno() {
        Calendar data = Calendar.getInstance();
        data.setTime(vencimento);
        return data.get(Calendar.YEAR);
    }
    
    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        valorPago.setScale(2, RoundingMode.FLOOR);
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
        valorFaltante.setScale(2, RoundingMode.FLOOR);
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
        valor.setScale(2, RoundingMode.FLOOR);
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

    public boolean isAtrasado() {
        return ( Acrescimos.daysDiff(new Date(), vencimento) < 0);
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
    
    public boolean isPagoEmAtraso(){
        if(status == PAGO_PARCIAL || status == PAGO){
            // Calcula o numero de dias de atraso.
            long diasAtraso = Acrescimos.daysDiff(getVencimento(), getDataPag());
            
            // Se foi pago em atraso, a data de pagamento passa a ser o vencimento.
            return(diasAtraso > 0);
        }
        return false;
    }
    
    public Date getVencimentoAtual(){
        if(status == PAGO_PARCIAL){
            // Se foi pago em atraso, a data de pagamento passa a ser o vencimento.
            if(isPagoEmAtraso()){
                return (getDataPag());
            }else{
                return(getVencimento());
            }
        } else {
            return (getVencimento());
        }
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
