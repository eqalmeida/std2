/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author eqalmeida
 */
public class Util {

    public static void debug(String msg) {
        System.out.println(new Date() + ": " + msg);
    }

    /**
     * Converte a data em um numero Long
     *
     * @param data
     * @return numero
     */
    static public long dateToLong(Date data) {
        Calendar c = Calendar.getInstance();
        c.setTime(data);
        long d = (c.getTimeInMillis() / (24 * 60 * 60 * 1000));
        return d;
    }
}
