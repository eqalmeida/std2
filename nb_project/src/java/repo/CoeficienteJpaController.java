/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Coeficiente;
import model.CoeficientePK;
import repo.exceptions.NonexistentEntityException;
import repo.exceptions.PreexistingEntityException;

/**
 *
 * @author eqalmeida
 */
public class CoeficienteJpaController extends AbstractJpaController  {

    public void create(Coeficiente coeficiente) throws PreexistingEntityException, Exception {
        if (coeficiente.getCoeficientePK() == null) {
            coeficiente.setCoeficientePK(new CoeficientePK());
        }
        coeficiente.getCoeficientePK().setTabelaFinanc(coeficiente.getTabelaFinanc().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(coeficiente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCoeficiente(coeficiente.getCoeficientePK()) != null) {
                throw new PreexistingEntityException("Coeficiente " + coeficiente + " already exists.", ex);
            }
            throw ex;
        }
    }

    public void edit(Coeficiente coeficiente) throws NonexistentEntityException, Exception {
        coeficiente.getCoeficientePK().setTabelaFinanc(coeficiente.getTabelaFinanc().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            coeficiente = em.merge(coeficiente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CoeficientePK id = coeficiente.getCoeficientePK();
                if (findCoeficiente(id) == null) {
                    throw new NonexistentEntityException("The coeficiente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(CoeficientePK id) throws NonexistentEntityException {
        EntityManager em = null;

        em = getEntityManager();
        em.getTransaction().begin();
        Coeficiente coeficiente;
        try {
            coeficiente = em.getReference(Coeficiente.class, id);
            coeficiente.getCoeficientePK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The coeficiente with id " + id + " no longer exists.", enfe);
        }
        em.remove(coeficiente);
        em.getTransaction().commit();

    }

    public List<Coeficiente> findCoeficienteEntities() {
        return findCoeficienteEntities(true, -1, -1);
    }

    public List<Coeficiente> findCoeficienteEntities(short tabelaId) {
        EntityManager em = getEntityManager();

        Query q = em.createQuery("SELECT c FROM Coeficiente c WHERE c.tabelaFinanc.id = :tabela_id").setParameter("tabela_id", tabelaId);
        return q.getResultList();
    }

    public List<Coeficiente> findCoeficienteEntities(int maxResults, int firstResult) {
        return findCoeficienteEntities(false, maxResults, firstResult);
    }

    private List<Coeficiente> findCoeficienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Coeficiente.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Coeficiente findCoeficiente(CoeficientePK id) {
        EntityManager em = getEntityManager();
        return em.find(Coeficiente.class, id);
    }

    public int getCoeficienteCount() {
        
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Coeficiente> rt = cq.from(Coeficiente.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
