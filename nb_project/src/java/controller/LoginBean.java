package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import model.Usuario;
import org.hibernate.exception.spi.Configurable;

/**
 *
 * @author eqalmeida
 */
@ManagedBean(name = "loginMB")
@SessionScoped
public class LoginBean extends ControllerBase {

    private Long usuarioId = null;
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

        EntityManager em = ControllerBase.getEntityManager();

        Usuario u = em.find(Usuario.class, usuarioId);
        if (u != null) {
            if (u.getAtivo() == false) {
                return null;
            }
        }
        return u;

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

        EntityManager em = ControllerBase.getEntityManager();



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
                
            
                if (this.url.contains("login")) {
                    this.url = "/index.jsf";
                }
                FacesContext fc = FacesContext.getCurrentInstance();
                try {
                    fc.getExternalContext().redirect(url);
                } catch (IOException ex) {
                    Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            } else {
                addErrorMessage("Usuário desativado");
            }
        } else {
            addErrorMessage("Senha inválida!");
        }

    }

    public boolean isLogedin() {
        return (this.usuarioId != null);
    }

    public void logoff() {
        usuarioId = null;
        login = "";
    }

    public String getVerificaLogin() {
        return "";
    }

    public void checkLogin(ComponentSystemEvent evt) {
        if (!isLogedin()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            this.url = getURL();
            handler.performNavigation("login?faces-redirect=true");
        }
    }

    public void checkLoginPageError(ComponentSystemEvent evt) {
        if (isLogedin()) {
            FacesContext fc = FacesContext.getCurrentInstance();
            ConfigurableNavigationHandler handler = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
            handler.performNavigation("index?faces-redirect=true");
        }
    }

    private String getURL() {
        Enumeration<String> lParameters;
        String sParameter;
        StringBuilder sbURL = new StringBuilder();
        Object oRequest = FacesContext.getCurrentInstance().getExternalContext().getRequest();

        try {
            if (oRequest instanceof HttpServletRequest) {
                sbURL.append(((HttpServletRequest) oRequest).getRequestURL().toString());

                lParameters = ((HttpServletRequest) oRequest).getParameterNames();

                if (lParameters.hasMoreElements()) {
                    if (!sbURL.toString().contains("?")) {
                        sbURL.append("?");
                    } else {
                        sbURL.append("&");
                    }
                }

                while (lParameters.hasMoreElements()) {
                    sParameter = lParameters.nextElement();

                    sbURL.append(sParameter);
                    sbURL.append("=");
                    sbURL.append(URLEncoder.encode(((HttpServletRequest) oRequest).getParameter(sParameter), "UTF-8"));

                    if (lParameters.hasMoreElements()) {
                        sbURL.append("&");
                    }
                }
            }
        } catch (Exception e) {
            // Do nothing
        }

        return sbURL.toString();
    }
}
