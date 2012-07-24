/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.TabelaFinanc;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class TabelaFinancJpaController implements Serializable {

    public TabelaFinancJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TabelaFinanc tabelaFinanc) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tabelaFinanc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TabelaFinanc tabelaFinanc) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tabelaFinanc = em.merge(tabelaFinanc);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = tabelaFinanc.getId();
                if (findTabelaFinanc(id) == null) {
                    throw new NonexistentEntityException("The tabelaFinanc with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TabelaFinanc tabelaFinanc;
            try {
                tabelaFinanc = em.getReference(TabelaFinanc.class, id);
                tabelaFinanc.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tabelaFinanc with id " + id + " no longer exists.", enfe);
            }
            em.remove(tabelaFinanc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TabelaFinanc> findTabelaFinancEntities() {
        return findTabelaFinancEntities(true, -1, -1);
    }

    public List<TabelaFinanc> findTabelaFinancEntities(int maxResults, int firstResult) {
        return findTabelaFinancEntities(false, maxResults, firstResult);
    }

    private List<TabelaFinanc> findTabelaFinancEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(TabelaFinanc.class));
//            Query q = em.createQuery(cq);
            Query q = em.createQuery("SELECT t FROM TabelaFinanc t ORDER BY t.id");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TabelaFinanc findTabelaFinanc(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TabelaFinanc.class, id);
        } finally {
            em.close();
        }
    }

    public int getTabelaFinancCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TabelaFinanc> rt = cq.from(TabelaFinanc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
