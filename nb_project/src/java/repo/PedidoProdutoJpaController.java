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
import model.PedidoProduto;
import model.PedidoProdutoPK;
import model.Produto;
import repo.exceptions.NonexistentEntityException;
import repo.exceptions.PreexistingEntityException;

/**
 *
 * @author eqalmeida
 */
public class PedidoProdutoJpaController extends AbstractJpaController {

    public PedidoProdutoJpaController() {
    }

    public void create(PedidoProduto pedidoProduto) throws PreexistingEntityException, Exception {
        if (pedidoProduto.getPedidoProdutoPK() == null) {
            pedidoProduto.setPedidoProdutoPK(new PedidoProdutoPK());
        }
        pedidoProduto.getPedidoProdutoPK().setPedido(pedidoProduto.getPedido().getId());
        pedidoProduto.getPedidoProdutoPK().setProduto(pedidoProduto.getProduto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto produto = pedidoProduto.getProduto();
            if (produto != null) {
                produto = em.getReference(produto.getClass(), produto.getId());
                pedidoProduto.setProduto(produto);
            }
            em.persist(pedidoProduto);
            if (produto != null) {
                produto.getPedidoProdutoCollection().add(pedidoProduto);
                produto = em.merge(produto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPedidoProduto(pedidoProduto.getPedidoProdutoPK()) != null) {
                throw new PreexistingEntityException("PedidoProduto " + pedidoProduto + " already exists.", ex);
            }
            throw ex;
        }
    }

    public void edit(PedidoProduto pedidoProduto) throws NonexistentEntityException, Exception {
        pedidoProduto.getPedidoProdutoPK().setPedido(pedidoProduto.getPedido().getId());
        pedidoProduto.getPedidoProdutoPK().setProduto(pedidoProduto.getProduto().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PedidoProduto persistentPedidoProduto = em.find(PedidoProduto.class, pedidoProduto.getPedidoProdutoPK());
            Produto produtoOld = persistentPedidoProduto.getProduto();
            Produto produtoNew = pedidoProduto.getProduto();
            if (produtoNew != null) {
                produtoNew = em.getReference(produtoNew.getClass(), produtoNew.getId());
                pedidoProduto.setProduto(produtoNew);
            }
            pedidoProduto = em.merge(pedidoProduto);
            if (produtoOld != null && !produtoOld.equals(produtoNew)) {
                produtoOld.getPedidoProdutoCollection().remove(pedidoProduto);
                produtoOld = em.merge(produtoOld);
            }
            if (produtoNew != null && !produtoNew.equals(produtoOld)) {
                produtoNew.getPedidoProdutoCollection().add(pedidoProduto);
                produtoNew = em.merge(produtoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PedidoProdutoPK id = pedidoProduto.getPedidoProdutoPK();
                if (findPedidoProduto(id) == null) {
                    throw new NonexistentEntityException("The pedidoProduto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(PedidoProdutoPK id) throws NonexistentEntityException {
        EntityManager em = null;

        em = getEntityManager();
        em.getTransaction().begin();
        PedidoProduto pedidoProduto;
        try {
            pedidoProduto = em.getReference(PedidoProduto.class, id);
            pedidoProduto.getPedidoProdutoPK();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The pedidoProduto with id " + id + " no longer exists.", enfe);
        }
        Produto produto = pedidoProduto.getProduto();
        if (produto != null) {
            produto.getPedidoProdutoCollection().remove(pedidoProduto);
            produto = em.merge(produto);
        }
        em.remove(pedidoProduto);
        em.getTransaction().commit();
    }

    public List<PedidoProduto> findPedidoProdutoEntities(Integer pedidoId) {
        EntityManager em = getEntityManager();

        Query query = em.createQuery("SELECT p FROM PedidoProduto p WHERE p.pedido.id = :pedidoId");
        query.setParameter("pedidoId", pedidoId);
        return query.getResultList();
    }

    public List<PedidoProduto> findPedidoProdutoEntities() {
        return findPedidoProdutoEntities(true, -1, -1);
    }

    public List<PedidoProduto> findPedidoProdutoEntities(int maxResults, int firstResult) {
        return findPedidoProdutoEntities(false, maxResults, firstResult);
    }

    private List<PedidoProduto> findPedidoProdutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(PedidoProduto.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public PedidoProduto findPedidoProduto(PedidoProdutoPK id) {
        EntityManager em = getEntityManager();
        return em.find(PedidoProduto.class, id);
    }

    public int getPedidoProdutoCount() {
        EntityManager em = getEntityManager();
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<PedidoProduto> rt = cq.from(PedidoProduto.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
