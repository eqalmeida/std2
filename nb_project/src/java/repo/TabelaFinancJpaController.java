/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.TabelaFinanc;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class TabelaFinancJpaController extends AbstractJpaController<TabelaFinanc> {

    public TabelaFinancJpaController() {
        super(TabelaFinanc.class);
    }

    public void destroy(Short id) throws NonexistentEntityException {
        super.remove(super.find(id));
    }

    public List<TabelaFinanc> findTabelaFinancEntities() {
        return findTabelaFinancEntities(true, -1, -1);
    }

    public List<TabelaFinanc> findTabelaFinancEntities(int maxResults, int firstResult) {
        return findTabelaFinancEntities(false, maxResults, firstResult);
    }

    private List<TabelaFinanc> findTabelaFinancEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(TabelaFinanc.class));
//            Query q = em.createQuery(cq);
        Query q = em.createQuery("SELECT t FROM TabelaFinanc t ORDER BY t.id");
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
}
