/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.PedidoProduto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Produto;
import repo.exceptions.IllegalOrphanException;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class ProdutoJpaController1 implements Serializable {

    public ProdutoJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produto produto) {
        if (produto.getPedidoProdutoCollection() == null) {
            produto.setPedidoProdutoCollection(new ArrayList<PedidoProduto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<PedidoProduto> attachedPedidoProdutoCollection = new ArrayList<PedidoProduto>();
            for (PedidoProduto pedidoProdutoCollectionPedidoProdutoToAttach : produto.getPedidoProdutoCollection()) {
                pedidoProdutoCollectionPedidoProdutoToAttach = em.getReference(pedidoProdutoCollectionPedidoProdutoToAttach.getClass(), pedidoProdutoCollectionPedidoProdutoToAttach.getPedidoProdutoPK());
                attachedPedidoProdutoCollection.add(pedidoProdutoCollectionPedidoProdutoToAttach);
            }
            produto.setPedidoProdutoCollection(attachedPedidoProdutoCollection);
            em.persist(produto);
            for (PedidoProduto pedidoProdutoCollectionPedidoProduto : produto.getPedidoProdutoCollection()) {
                Produto oldProdutoOfPedidoProdutoCollectionPedidoProduto = pedidoProdutoCollectionPedidoProduto.getProduto();
                pedidoProdutoCollectionPedidoProduto.setProduto(produto);
                pedidoProdutoCollectionPedidoProduto = em.merge(pedidoProdutoCollectionPedidoProduto);
                if (oldProdutoOfPedidoProdutoCollectionPedidoProduto != null) {
                    oldProdutoOfPedidoProdutoCollectionPedidoProduto.getPedidoProdutoCollection().remove(pedidoProdutoCollectionPedidoProduto);
                    oldProdutoOfPedidoProdutoCollectionPedidoProduto = em.merge(oldProdutoOfPedidoProdutoCollectionPedidoProduto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produto produto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto persistentProduto = em.find(Produto.class, produto.getId());
            Collection<PedidoProduto> pedidoProdutoCollectionOld = persistentProduto.getPedidoProdutoCollection();
            Collection<PedidoProduto> pedidoProdutoCollectionNew = produto.getPedidoProdutoCollection();
            List<String> illegalOrphanMessages = null;
            for (PedidoProduto pedidoProdutoCollectionOldPedidoProduto : pedidoProdutoCollectionOld) {
                if (!pedidoProdutoCollectionNew.contains(pedidoProdutoCollectionOldPedidoProduto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PedidoProduto " + pedidoProdutoCollectionOldPedidoProduto + " since its produto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<PedidoProduto> attachedPedidoProdutoCollectionNew = new ArrayList<PedidoProduto>();
            for (PedidoProduto pedidoProdutoCollectionNewPedidoProdutoToAttach : pedidoProdutoCollectionNew) {
                pedidoProdutoCollectionNewPedidoProdutoToAttach = em.getReference(pedidoProdutoCollectionNewPedidoProdutoToAttach.getClass(), pedidoProdutoCollectionNewPedidoProdutoToAttach.getPedidoProdutoPK());
                attachedPedidoProdutoCollectionNew.add(pedidoProdutoCollectionNewPedidoProdutoToAttach);
            }
            pedidoProdutoCollectionNew = attachedPedidoProdutoCollectionNew;
            produto.setPedidoProdutoCollection(pedidoProdutoCollectionNew);
            produto = em.merge(produto);
            for (PedidoProduto pedidoProdutoCollectionNewPedidoProduto : pedidoProdutoCollectionNew) {
                if (!pedidoProdutoCollectionOld.contains(pedidoProdutoCollectionNewPedidoProduto)) {
                    Produto oldProdutoOfPedidoProdutoCollectionNewPedidoProduto = pedidoProdutoCollectionNewPedidoProduto.getProduto();
                    pedidoProdutoCollectionNewPedidoProduto.setProduto(produto);
                    pedidoProdutoCollectionNewPedidoProduto = em.merge(pedidoProdutoCollectionNewPedidoProduto);
                    if (oldProdutoOfPedidoProdutoCollectionNewPedidoProduto != null && !oldProdutoOfPedidoProdutoCollectionNewPedidoProduto.equals(produto)) {
                        oldProdutoOfPedidoProdutoCollectionNewPedidoProduto.getPedidoProdutoCollection().remove(pedidoProdutoCollectionNewPedidoProduto);
                        oldProdutoOfPedidoProdutoCollectionNewPedidoProduto = em.merge(oldProdutoOfPedidoProdutoCollectionNewPedidoProduto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produto.getId();
                if (findProduto(id) == null) {
                    throw new NonexistentEntityException("The produto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto produto;
            try {
                produto = em.getReference(Produto.class, id);
                produto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PedidoProduto> pedidoProdutoCollectionOrphanCheck = produto.getPedidoProdutoCollection();
            for (PedidoProduto pedidoProdutoCollectionOrphanCheckPedidoProduto : pedidoProdutoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Produto (" + produto + ") cannot be destroyed since the PedidoProduto " + pedidoProdutoCollectionOrphanCheckPedidoProduto + " in its pedidoProdutoCollection field has a non-nullable produto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(produto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Produto> findProdutoEntities() {
        return findProdutoEntities(true, -1, -1);
    }

    public List<Produto> findProdutoEntities(int maxResults, int firstResult) {
        return findProdutoEntities(false, maxResults, firstResult);
    }

    private List<Produto> findProdutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produto.class));
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

    public Produto findProduto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Produto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
