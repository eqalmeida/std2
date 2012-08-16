/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import model.Config;
import util.Util;

/**
 *
 * @author eqalmeida
 */
@ManagedBean
@ApplicationScoped
public class App {
    
    private List<SelectItem> meses = null;
    private Config config = null;

    /**
     * Creates a new instance of App
     */
    public App() {
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
    
    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }
    
    public List<SelectItem> getMeses() {
        
        if (meses == null) {
            
            meses = new ArrayList<SelectItem>();
            
            for (int i = 0; i < 12; i++) {
                
                SelectItem it;
                
                it = new SelectItem(i, Util.monthNames.get(i));
                meses.add(it);
            }
            
        }
        
        return meses;
    }
}
