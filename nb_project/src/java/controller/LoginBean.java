/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.ViewHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Usuario;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "loginMB")
@SessionScoped
public class LoginBean extends ControllerBase {

    private Usuario usuario = null;
    private String login;
    private String senha;
    private String url;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
    }

    public Usuario getUsuario() {

        return usuario;
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
                usuario = u;
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
        usuario = null;
        login = "";
    }

    public String getVerificaLogin() {
        return "";
    }

    public String getUrl() {
        return url;
    }
}
