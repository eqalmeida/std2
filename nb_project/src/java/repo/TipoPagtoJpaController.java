/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import controller.ControllerBase;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.TipoPagto;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class TipoPagtoJpaController implements Serializable {

    public TipoPagtoJpaController() {
        this.emf = ControllerBase.getEmf();
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoPagto tipoPagto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tipoPagto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoPagto tipoPagto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tipoPagto = em.merge(tipoPagto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = tipoPagto.getId();
                if (findTipoPagto(id) == null) {
                    throw new NonexistentEntityException("The tipoPagto with id " + id + " no longer exists.");
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
            TipoPagto tipoPagto;
            try {
                tipoPagto = em.getReference(TipoPagto.class, id);
                tipoPagto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoPagto with id " + id + " no longer exists.", enfe);
            }
            em.remove(tipoPagto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoPagto> findTipoPagtoEntities() {
        return findTipoPagtoEntities(true, -1, -1);
    }

    public List<TipoPagto> findTipoPagtoEntities(int maxResults, int firstResult) {
        return findTipoPagtoEntities(false, maxResults, firstResult);
    }

    private List<TipoPagto> findTipoPagtoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPagto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TipoPagto findTipoPagto(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPagto.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoPagtoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPagto> rt = cq.from(TipoPagto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
