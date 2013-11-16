package controller;

import static controller.ControllerBase.getEntityManager;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import model.Boleto;

/**
 *
 * @author eqalmeida
 */
@ManagedBean
@RequestScoped
public class PrintRelBoletos extends ControllerBase implements Serializable{

    private int month;
    private int year;
    private int type;
    
    public List<Boleto> getList(){
        if(type == 1){
            return listAllUpToDate();
        } else {
            return listFromMonth();
        }
    }
    
    /**
     * Lista de Boletos por mês.
     * @return 
     */
    private List<Boleto> listFromMonth(){
        Calendar inicio = Calendar.getInstance();
        Calendar fim = Calendar.getInstance();
        inicio.set(Calendar.DAY_OF_MONTH, 1);
        inicio.set(Calendar.MONTH, month);
        inicio.set(Calendar.YEAR, year);
        
        fim.setTime(inicio.getTime());
        
        // Avanca para o mes seguinte
        fim.add(Calendar.MONTH, 1);
        
        // Vai para o ultimo dia do mes.
        fim.add(Calendar.DATE, -1);

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT b FROM Boleto b "+
                "WHERE (b.vencimento BETWEEN :inicio AND :fim) " + 
                " AND b.status != " + Boleto.CANCELADO +
                " AND b.status != " + Boleto.PAGO +
                " ORDER BY b.vencimento ASC");
        
        query.setParameter("inicio", inicio, TemporalType.DATE);
        query.setParameter("fim", fim, TemporalType.DATE);
        return query.getResultList();
    }

    private List<Boleto> listAllUpToDate(){
        Calendar dataAtual = Calendar.getInstance();
        dataAtual.set(Calendar.HOUR_OF_DAY, 0);
        dataAtual.set(Calendar.MINUTE, 0);
        dataAtual.set(Calendar.SECOND, 0);

        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT b FROM Boleto b "+
                "WHERE (b.vencimento <= :dataAtual) " + 
                " AND b.status != " + Boleto.CANCELADO +
                " AND b.status != " + Boleto.PAGO +
                " ORDER BY b.pedidoPag.pedido.cliente.nome ASC");
        
        query.setParameter("dataAtual", dataAtual, TemporalType.DATE);
        Boleto p = new Boleto();
        //p.getPedidoPag().getPedido().getCliente().getId()
        return query.getResultList();
    }
    
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
}
