/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import model.PedidoPag;

/**
 *
 * @author eqalmeida
 */
public class PedidoPagJpaController extends AbstractJpaController<PedidoPag> {

    public PedidoPagJpaController() {
        super(PedidoPag.class);
    }

    public List<PedidoPag> findPedidoPagEntities(Integer pedidoId) {
        EntityManager em = getEntityManager();

        Query q = em.createQuery("SELECT p FROM PedidoPag p WHERE p.pedido.id = :pedidoId");
        q.setParameter("pedidoId", pedidoId);
        return q.getResultList();
    }

    private List<PedidoPag> findPedidoPagEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(PedidoPag.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PedidoPag findPedidoPag(Integer id) {
        EntityManager em = getEntityManager();

        return em.find(PedidoPag.class, id);
    }
}
