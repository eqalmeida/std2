/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
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

    @PostConstruct
    private void init() {
        try {
            /**
             * A versão 1.0 do BD vai causar uma exeption neste procedimento.
             * Desta forma posso aplicar o patch do BD com a versão 1.
             */
            Config c = getConfig();
        } catch (Exception e) {

            /**
             * Aplica o Patch da versão 1.0 do BD.
             */
            EntityManager em = ControllerBase.getEntityManager();
            em.getTransaction().begin();
            em.createNativeQuery("ALTER TABLE `std_loja`.`config` ADD COLUMN `empresa_cnpj` VARCHAR(30) NULL  AFTER `carta_autoriz` , ADD COLUMN `dbversion` INT NULL  AFTER `empresa_cnpj` ;").executeUpdate();
            em.getTransaction().commit();
            Config c = getConfig();
            if (c.getDbversion() == null) {
                em.getTransaction().begin();
                c.setDbversion(1);
                em.merge(c);
                em.getTransaction().commit();
            }
        }

    }

    /**
     * Creates a new instance of App
     */
    public App() {
    }

    public Config getConfig() {
        EntityManager em = ControllerBase.getEntityManager();

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

        return config;
    }

    public Date getHoje() {
        return (new Date());
    }

    public String getIpAddr() {

        String ip = null;

        try {
            FacesContext context = null;
            HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
            ip = request.getRemoteAddr();
        } catch (Exception e) {
        }
        return ip;
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
