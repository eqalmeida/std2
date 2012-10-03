/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.PagtoRecebido;
import model.Usuario;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class PagtoRecebidoJpaController extends AbstractJpaController {

    public void create(PagtoRecebido pagtoRecebido) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario recebUsuario = pagtoRecebido.getRecebUsuario();
            if (recebUsuario != null) {
                recebUsuario = em.getReference(recebUsuario.getClass(), recebUsuario.getId());
                pagtoRecebido.setRecebUsuario(recebUsuario);
            }
            em.persist(pagtoRecebido);
//            if (recebUsuario != null) {
//                recebUsuario.getPagtoRecebidoCollection().add(pagtoRecebido);
//                recebUsuario = em.merge(recebUsuario);
//            }
            em.getTransaction().commit();
        } finally {
        }
    }

    public void edit(PagtoRecebido pagtoRecebido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PagtoRecebido persistentPagtoRecebido = em.find(PagtoRecebido.class, pagtoRecebido.getId());
            Usuario recebUsuarioOld = persistentPagtoRecebido.getRecebUsuario();
            Usuario recebUsuarioNew = pagtoRecebido.getRecebUsuario();
            if (recebUsuarioNew != null) {
                recebUsuarioNew = em.getReference(recebUsuarioNew.getClass(), recebUsuarioNew.getId());
                pagtoRecebido.setRecebUsuario(recebUsuarioNew);
            }
            pagtoRecebido = em.merge(pagtoRecebido);
//            if (recebUsuarioOld != null && !recebUsuarioOld.equals(recebUsuarioNew)) {
//                recebUsuarioOld.getPagtoRecebidoCollection().remove(pagtoRecebido);
//                recebUsuarioOld = em.merge(recebUsuarioOld);
//            }
//            if (recebUsuarioNew != null && !recebUsuarioNew.equals(recebUsuarioOld)) {
//                recebUsuarioNew.getPagtoRecebidoCollection().add(pagtoRecebido);
//                recebUsuarioNew = em.merge(recebUsuarioNew);
//            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pagtoRecebido.getId();
                if (findPagtoRecebido(id) == null) {
                    throw new NonexistentEntityException("The pagtoRecebido with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PagtoRecebido pagtoRecebido;
            try {
                pagtoRecebido = em.getReference(PagtoRecebido.class, id);
                pagtoRecebido.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pagtoRecebido with id " + id + " no longer exists.", enfe);
            }
            Usuario recebUsuario = pagtoRecebido.getRecebUsuario();
//            if (recebUsuario != null) {
//                recebUsuario.getPagtoRecebidoCollection().remove(pagtoRecebido);
//                recebUsuario = em.merge(recebUsuario);
//            }
            em.remove(pagtoRecebido);
            em.getTransaction().commit();
        } finally {
        }
    }

    public List<PagtoRecebido> findPagtoRecebidoEntities() {
        return findPagtoRecebidoEntities(true, -1, -1);
    }

    public List<PagtoRecebido> findPagtoRecebidoEntities(int maxResults, int firstResult) {
        return findPagtoRecebidoEntities(false, maxResults, firstResult);
    }

    private List<PagtoRecebido> findPagtoRecebidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(PagtoRecebido.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PagtoRecebido findPagtoRecebido(Integer id) {
        EntityManager em = getEntityManager();
        return em.find(PagtoRecebido.class, id);
    }

    public int getPagtoRecebidoCount() {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<PagtoRecebido> rt = cq.from(PagtoRecebido.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
