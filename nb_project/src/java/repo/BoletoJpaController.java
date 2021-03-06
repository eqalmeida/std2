/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import model.Boleto;

/**
 *
 * @author eqalmeida
 */
public class BoletoJpaController extends AbstractJpaController<Boleto> {

    private String sortedField = null;
    private String order = null;
    private String filter = "";
    private Date dateTo = null;
    private Date dateFrom = null;

    public BoletoJpaController() {
        super(Boleto.class);
    }

    public BigDecimal calculaJuros(Integer boletoId, Calendar dataPagamento) {

        BigDecimal valor;
        EntityManager em = getEntityManager();


        Boleto boleto = em.find(Boleto.class, boletoId);
        Calendar vencimento = Calendar.getInstance();
        vencimento.setTime(boleto.getVencimento());


        while (vencimento.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || vencimento.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            vencimento.add(Calendar.DATE, 1);
        }

        valor = boleto.getValor();

        if (vencimento.before(dataPagamento)) {

            //Calcula juros
            int dias = 0;

            // Calcula os dias de Atraso
            while (vencimento.before(dataPagamento)) {
                dias++;
                vencimento.add(Calendar.DATE, 1);
            }

            BigDecimal juros, multa;

            // Calcula o Juros de 1% ao dia
            juros = valor.multiply(new BigDecimal(dias));
            juros = juros.divide(new BigDecimal(100.0));

            // Calcula a multa de 10%
            multa = valor.multiply(new BigDecimal(10.0));
            multa = multa.divide(new BigDecimal(100.0));

            //Soma a multa e o Juros ao Valor
            valor = valor.add(juros);
            valor = valor.add(multa);

            // Acerta a escala dos centavos
            valor = valor.setScale(2, RoundingMode.DOWN);
        }
        return valor;
    }

    public void setDatas(Date dateTo, Date dateFrom) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void setSortedField(String sortedField, String order) {
        this.sortedField = sortedField;
        this.order = order;
    }

    public List<Boleto> findBoletoEntities(int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

        String query = ("SELECT b FROM Boleto b WHERE (b.vencimento BETWEEN :ini AND :fin) and b.status != " + Boleto.CANCELADO);

        if (sortedField != null) {
            query += (" ORDER BY b." + sortedField + " " + order);
        }


        Query q = em.createQuery(query);
        q.setParameter("ini", dateFrom, TemporalType.DATE);
        q.setParameter("fin", dateTo, TemporalType.DATE);

        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public List<Boleto> findBoletosAtrasados(int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

        String query = ("SELECT b FROM Boleto b"
                + " WHERE b.vencimento < :dataAtual "
                + " AND b.status != " + Boleto.CANCELADO
                + " AND b.status != " + Boleto.PAGO);

        if (sortedField != null) {
            query += (" ORDER BY b." + sortedField + " " + order);
        }


        Query q = em.createQuery(query);
        q.setParameter("dataAtual", new Date(), TemporalType.DATE);

        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public List<Object[]> findBoletoReport() {
        EntityManager em = getEntityManager();

        String query = ("SELECT b.status AS status, "
                + "SUM(b.valor) AS total, SUM(b.valorPago) AS valorPago, "
                + "SUM(b.valorFaltante) AS valorFaltante  "
                + "FROM Boleto b WHERE (b.vencimento BETWEEN :ini AND :fin) and b.status != :stat"
                + " GROUP BY b.status");



        Query q = em.createQuery(query);
        q.setParameter("ini", dateFrom, TemporalType.DATE);
        q.setParameter("fin", dateTo, TemporalType.DATE);
        q.setParameter("stat", Boleto.CANCELADO);
        return q.getResultList();
    }

    /*

     public List<Boleto> findBoletoEntities(int maxResults, int firstResult) {
     return findBoletoEntities(false, maxResults, firstResult);
     }
     */
    public int getBoletoCount() {
        EntityManager em = getEntityManager();

        String query = ("SELECT COUNT(b) FROM Boleto b WHERE (b.vencimento BETWEEN :ini AND :fin) and b.status != " + Boleto.CANCELADO);
        Query q = em.createQuery(query);
        q.setParameter("ini", dateFrom, TemporalType.DATE);
        q.setParameter("fin", dateTo, TemporalType.DATE);

        return ((Long) q.getSingleResult()).intValue();
    }

    public int getBoletosAtrasadosCount() {
        EntityManager em = getEntityManager();


        String query = ("SELECT COUNT(b) FROM Boleto b"
                + " WHERE b.vencimento < :dataAtual "
                + " AND b.status != " + Boleto.CANCELADO
                + " AND b.status != " + Boleto.PAGO);

        Query q = em.createQuery(query);
        q.setParameter("dataAtual", new Date(), TemporalType.DATE);

        return ((Long) q.getSingleResult()).intValue();
    }

    public BigDecimal getValorTotal() {
        EntityManager em = getEntityManager();

        String query = ("SELECT SUM(b.valor) FROM Boleto b WHERE (b.vencimento BETWEEN :ini AND :fin) AND b.status != " + Boleto.CANCELADO);
        Query q = em.createQuery(query);
        q.setParameter("ini", dateFrom, TemporalType.DATE);
        q.setParameter("fin", dateTo, TemporalType.DATE);

        return ((BigDecimal) q.getSingleResult());
    }
}
