/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package repo;

import javax.persistence.Query;
import model.PedidoProduto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import model.Produto;
import repo.exceptions.IllegalOrphanException;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class ProdutoJpaController extends AbstractJpaController<Produto> {

    private String sortedField = null;
    private String order = null;
    private Integer fTipo = null;
    private Integer fEstoque = null;
    private String fPlaca = null;

    public void setfPlaca(String fPlaca) {
        this.fPlaca = fPlaca;
    }
    
    

    public void setfTipo(Integer fTipo) {
        this.fTipo = fTipo;
    }

    public void setfEstoque(Integer fEstoque) {
        this.fEstoque = fEstoque;
    }

    public void setSortedField(String sortedField, String order) {
        this.sortedField = sortedField;
        this.order = order;
    }

    public ProdutoJpaController() {
        super(Produto.class);
    }

    @Override
    public void create(Produto produto) {
        if (produto.getPedidoProdutoCollection() == null) {
            produto.setPedidoProdutoCollection(new ArrayList<PedidoProduto>());
        }
        EntityManager em = null;

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
    }

    @Override
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
                if (super.find(id) == null) {
                    throw new NonexistentEntityException("The produto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        super.remove(super.find(id));
    }

    public List<Produto> findProdutoEntities(int tipo) {
        return findProdutoEntities(true, -1, -1, tipo);
    }

    public List<Produto> findProdutoEntities(int maxResults, int firstResult, int tipo) {
        return findProdutoEntities(false, maxResults, firstResult, tipo);
    }

    public List<Produto> findProdutosByQuery(String query) {
        EntityManager em = getEntityManager();

        return em.createQuery(query).getResultList();

    }

    public List<Produto> findVeiculosEmEstoque() {
        return findProdutosByQuery("SELECT p FROM Produto p WHERE p.tipo = 1 and p.qtdEstoque != 0 ORDER BY p.fabricante, p.modelo");
    }

    private String getWhere() {

        String where = "";

        if (fEstoque == null && fTipo == null) {
            return where;
        }

        where = " WHERE 1=1";

        if (fTipo != null) {
            where += (" and p.tipo = " + fTipo);

            if(fPlaca != null && fTipo == 1){
                where += (" and p.placa LIKE '"+ this.fPlaca +"%'");
            }
            
            if(fTipo==0){
                this.setfPlaca(null);
            }

        }

        if (fEstoque != null) {
            where += (" and p.qtdEstoque != 0");
        }
        
        return where;
    }

    public List<Produto> findProdutoEntities(int maxResults, int firstResult) {

        String query = "SELECT p FROM Produto p";

        query += getWhere();

        if (sortedField != null) {
            query += (" ORDER BY p." + sortedField + " " + order);
        }

        EntityManager em = getEntityManager();

        Query q = em.createQuery(query);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public int getProdutoCount() {

        String query = "SELECT COUNT(p) FROM Produto p";

        query += getWhere();

        EntityManager em = getEntityManager();

        Query q = em.createQuery(query);
        return ((Long) q.getSingleResult()).intValue();
    }

    private List<Produto> findProdutoEntities(boolean all, int maxResults, int firstResult, int tipo) {
        EntityManager em = getEntityManager();

        String query = "SELECT p FROM Produto p WHERE p.tipo = :tipo";

        if (sortedField != null) {
            query += (" ORDER BY p." + sortedField + " " + order);
        }



        Query q = em.createQuery(query).setParameter("tipo", tipo);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public int getProdutoCount(int tipo) {
        EntityManager em = getEntityManager();

        Query q = em.createQuery("SELECT COUNT(p) FROM Produto p WHERE p.tipo = :tipo").setParameter("tipo", tipo);
        return ((Long) q.getSingleResult()).intValue();
    }
}
