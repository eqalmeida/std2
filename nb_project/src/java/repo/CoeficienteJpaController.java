/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Coeficiente;
import model.CoeficientePK;

/**
 *
 * @author eqalmeida
 */
public class CoeficienteJpaController extends AbstractJpaController<Coeficiente> {

    public CoeficienteJpaController() {
        super(Coeficiente.class);
    }

    public List<Coeficiente> findCoeficienteEntities(short tabelaId) {
        EntityManager em = getEntityManager();

        Query q = em.createQuery("SELECT c FROM Coeficiente c WHERE c.tabelaFinanc.id = :tabela_id").setParameter("tabela_id", tabelaId);
        return q.getResultList();
    }

    public Coeficiente findCoeficiente(CoeficientePK id) {
        //EntityManager em = getEntityManager();
        //return em.find(Coeficiente.class, id);
        return super.find(id);
    }
}
