/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.TimeZone;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;

/**
 *
 * @author eqalmeida
 */
@ManagedBean
@ApplicationScoped
public class App {

    /**
     * Creates a new instance of App
     */
    public App() {
    }

    public TimeZone getTimeZone() {
        return TimeZone.getDefault();
    }
}
