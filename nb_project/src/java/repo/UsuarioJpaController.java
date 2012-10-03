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
import model.Usuario;
import repo.exceptions.NonexistentEntityException;

/**
 *
 * @author eqalmeida
 */
public class UsuarioJpaController extends AbstractJpaController {

    public UsuarioJpaController() {
    }

    private void verificaAdmin(Usuario usuario) throws Exception {
        if (usuario.isAdmin() && usuario.getAtivo()) {
            return;
        }

        List<Usuario> lista = findUsuarioEntities();

        for (Usuario u : lista) {
            if (u.isAdmin() && u.getAtivo() && (!u.getId().equals(usuario.getId()))) {
                return;
            }
        }

        throw new Exception("É necessário manter pelo menos um usuário como ADMIN");
    }

    private void verificaExcluir(Long usuarioId) throws Exception {

        Usuario usuario = findUsuario(usuarioId);

        List<Usuario> lista = findUsuarioEntities();

        for (Usuario u : lista) {
            if (u.isAdmin() && u.getAtivo() && (!u.getId().equals(usuario.getId()))) {
                return;
            }
        }

        throw new Exception("É necessário manter pelo menos um usuário como ADMIN");
    }

    public void create(Usuario usuario) {
        EntityManager em = null;
        em = getEntityManager();
        em.getTransaction().begin();
        em.persist(usuario);
        em.getTransaction().commit();
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        verificaAdmin(usuario);
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            usuario = em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = usuario.getId();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, Exception {

        verificaExcluir(id);

        EntityManager em = null;
        em = getEntityManager();

        em.getTransaction().begin();
        Usuario usuario;
        try {
            usuario = em.getReference(Usuario.class, id);
            usuario.getId();
        } catch (EntityNotFoundException enfe) {
            throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
        }
        em.remove(usuario);
        em.getTransaction().commit();
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Usuario.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Usuario findUsuario(Long id) {
        EntityManager em = getEntityManager();

        return em.find(Usuario.class, id);
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Usuario> rt = cq.from(Usuario.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
