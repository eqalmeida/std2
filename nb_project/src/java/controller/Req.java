/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import model.Config;

/**
 *
 * @author eqalmeida
 */
@ManagedBean
@RequestScoped
public class Req {
    
    private Config config = null;

    /**
     * Creates a new instance of Req
     */
    public Req() {
    }
    
    public Config getConfig() {
        EntityManager em = ControllerBase.getEmf().createEntityManager();

        try {
            List<Config> confs = em.createQuery("SELECT c FROM Config c").getResultList();
            if (confs.isEmpty()) {
                em.getTransaction().begin();
                config = new Config();
                config.setBoletosPorPag((short) 4);
                em.persist(config);
                em.getTransaction().commit();
            } else {
                config = confs.get(0);
            }


        } finally {
            em.close();
        }
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
