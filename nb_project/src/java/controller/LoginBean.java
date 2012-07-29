/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Usuario;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "loginMB")
@SessionScoped
public class LoginBean extends ControllerBase {

    private Short usuarioId = null;
    private String login;
    private String senha;
    private String url;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public Usuario getUsuario() {
        if (usuarioId == null || usuarioId == 0) {
            return null;
        }

        EntityManager em = ControllerBase.getEmf().createEntityManager();

        try {
            Usuario u = em.find(Usuario.class, usuarioId);
            if (u != null) {
                if (u.getAtivo() == false) {
                    return null;
                }
            }
            return u;
        } finally {
            em.close();
        }

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void login() {

        EntityManager em = ControllerBase.getEmf().createEntityManager();

        try {

            Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.login = :login");
            query.setParameter("login", login);

            if (query.getResultList().isEmpty()) {

                addErrorMessage("Login inválido!");
                return;
            }

            Usuario u = (Usuario) query.getResultList().get(0);

            if (u.getSenha().equals(senha)) {
                if (u.getAtivo()) {
                    usuarioId = u.getId();
                } else {
                    addErrorMessage("Usuário desativado");
                }
            } else {
                addErrorMessage("Senha inválida!");
            }

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void logoff() {
        usuarioId = null;
        login = "";
    }

    public String getVerificaLogin() {
        return "";
    }

    public String getUrl() {
        return url;
    }
}
