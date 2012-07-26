/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    public static Short ATIVO = 0;
    public static Short PAGO = 1;
    public static Short PAGO_PARCIAL = 2;
    public static Short CANCELADO = 3;
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
    @Column(name = "valor_faltante")
    private BigDecimal valorFaltante;
    @Column(name = "data_pag")
    @Temporal(TemporalType.DATE)
    private Date dataPag;
    @javax.persistence.Transient
    private Short situacao = null;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "boleto_id")
    private Collection<PagtoRecebido> pagamentos;

    public Boleto() {
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

    public Collection<PagtoRecebido> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(Collection<PagtoRecebido> pagamentos) {
        this.pagamentos = pagamentos;
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
        long d = (c.getTimeInMillis() / (24 * 60 * 60 * 1000));
        return d;
    }

    public boolean isAtrasado(Date data) {

        if (status == Boleto.CANCELADO || status == Boleto.PAGO) {
            return false;
        }

        if (status == PAGO_PARCIAL) {
            return true;
        }

        long diasDataPag = getDias(data);
        long diasVencimento = getDias(vencimento);
        long atraso = diasDataPag - diasVencimento;

        if (atraso >= 0) {
            return true;
        }

        return false;

    }

    public boolean regPagamento(BigDecimal valor, Date dataInf, Usuario usuario) throws Exception {

        BigDecimal val = getValorDevido(dataInf);

        long diasHoje = getDias(new Date());
        long diasPag = getDias(dataInf);

        if (diasPag > diasHoje) {
            throw new Exception("Data de Pagamento Inválida");
        }

        if (valor.compareTo(val) > 0) {
            throw new Exception("Valor de Pagamento inválido");
        }

        PagtoRecebido pag = new PagtoRecebido();
        pag.setBoleto(this);
        pag.setData(new Date());
        pag.setDataInformada(dataInf);
        pag.setValorDevido(val);
        pag.setValor(valor);
        pag.setRecebUsuario(usuario);

        BigDecimal temp = getJuros();

        if (temp == null) {
            temp = BigDecimal.ZERO;
        }

        temp = temp.add(getJuros(dataInf));

        setJuros(temp);

        temp = getMulta();

        if (temp == null) {
            temp = BigDecimal.ZERO;
        }

        temp = temp.add(getMulta(dataInf));

        setMulta(temp);

        if (status == ATIVO) {

            // Calcula o Valor total devido
            temp = getValor().add(getJuros()).add(getMulta());

            setValorFaltante(temp.subtract(valor));
            if (getValorFaltante().longValue() > 0.0) {
                setStatus(PAGO_PARCIAL);
            } else {
                setStatus(PAGO);
            }
        } else if (status == PAGO_PARCIAL) {

            // Calcula o Valor total devido
            temp = getValorFaltante().add(getJuros()).add(getMulta());

            // Salva o novo valor faltante
            setValorFaltante(temp.subtract(valor));

            if (getValorFaltante().longValue() > 0.0) {
                setStatus(PAGO_PARCIAL);
            } else {
                setStatus(PAGO);
            }
        }

        setDataPag(dataInf);

        if (getPagamentos() == null) {
            setPagamentos(new ArrayList<PagtoRecebido>());
        }

        getPagamentos().add(pag);

        return true;
    }

    public BigDecimal getValorDevido(Date data) {

        BigDecimal val = BigDecimal.ZERO;

        if (status == ATIVO) {
            val = getValor();
        } else if (status == PAGO_PARCIAL) {
            val = getValorFaltante();
        }

        val = val.add(getJuros(data)).add(getMulta(data));

        return val;
    }

    private BigDecimal getJuros(Date data) {

        long diasDataPag = getDias(data);

        if (status == ATIVO) {

            long diasVencimento = getDias(vencimento);
            long atraso = diasDataPag - diasVencimento;


            if (atraso > 0) {
                BigDecimal temp = getValor().multiply(new BigDecimal(atraso)).divide(new BigDecimal(100.0)).setScale(2, RoundingMode.DOWN);
                return (temp);

            } else {
                return (BigDecimal.ZERO);
            }
        } else if (status == PAGO_PARCIAL) {

            long diasVencimento = getDias(dataPag);
            long diasVencimentoPrincipal = getDias(vencimento);
            
            long atraso;

            if (diasVencimentoPrincipal > diasVencimento) {
                atraso = diasDataPag - diasVencimentoPrincipal;
            } else {
                atraso = diasDataPag - diasVencimento;
            }


            if (atraso > 0) {
                BigDecimal temp = getValorFaltante().multiply(new BigDecimal(atraso)).divide(new BigDecimal(100.0)).setScale(2, RoundingMode.DOWN);
                return (temp);
            } else {
                return (BigDecimal.ZERO);
            }
        }
        return (BigDecimal.ZERO);
    }

    private BigDecimal getMulta(Date data) {

        long diasDataPag = getDias(data);

        if (status == ATIVO) {

            long diasVencimento = getDias(vencimento);
            long atraso = diasDataPag - diasVencimento;


            if (atraso > 0) {
                BigDecimal temp = getValor().multiply(new BigDecimal(10)).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);
                return (temp);

            } else {
                return (BigDecimal.ZERO);
            }

        }
        return (BigDecimal.ZERO);
    }

    public String getStatusStr() {
        String str[] = {
            "ATIVO",
            "PAGO",
            "PAGO PARCIAL",
            "CANCELADO",};
        return str[status];
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
