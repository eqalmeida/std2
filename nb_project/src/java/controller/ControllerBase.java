/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import model.Usuario;
import org.primefaces.context.RequestContext;

/**
 *
 * @author eqalmeida
 */
public abstract class ControllerBase {

    private static EntityManager entityManager = null;
    
    public ControllerBase() {
    }
    //private static EntityManagerFactory emf = null;

    static public Usuario getUsuarioLogado() {
        FacesContext context = FacesContext.getCurrentInstance();

        Usuario u = context.getApplication().evaluateExpressionGet(context, "#{loginMB}", LoginBean.class).getUsuario();

        return u;
    }

    static void showDialog(String name) {
        RequestContext.getCurrentInstance().execute(name + ".show()");
    }

    static void showPopup(String url) {
        RequestContext.getCurrentInstance().execute("window.open ('" + url + "','mywindow','height=600, width=800');");
    }

    static void hideDialog(String name) {
        RequestContext.getCurrentInstance().execute(name + ".hide()");
    }

    static public boolean verificaLogin(String req) {


        Usuario u = getUsuarioLogado();

        if (u == null) {
            return false;
        }

        return true;

    }

    /*
     static public EntityManagerFactory getEmf() {
     if (emf == null) {
     emf = Persistence.createEntityManagerFactory("std_lojaPU");
     }
     return (emf);
     }
     */
    
    
    static public EntityManager getEntityManager() {
//        if(entityManager == null || (!entityManager.isOpen() )){
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ec.getRequest();
            entityManager = (EntityManager) request.getAttribute("entityManager");
//        }
        return entityManager;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void addErrorMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
