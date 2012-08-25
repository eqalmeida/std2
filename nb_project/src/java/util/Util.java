/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eqalmeida
 */
public class Util {
    
    public static final Map<Integer, String> monthNames ;
    static {
        monthNames = new HashMap<Integer, String>();
        monthNames.put(0, "Janeiro");
        monthNames.put(1, "Fevereiro");
        monthNames.put(2, "Março");
        monthNames.put(3, "Abril");
        monthNames.put(4, "Maio");
        monthNames.put(5, "Junho");
        monthNames.put(6, "Julho");
        monthNames.put(7, "Agosto");
        monthNames.put(8, "Setembro");
        monthNames.put(9, "Outubro");
        monthNames.put(10, "Novembro");
        monthNames.put(11, "Dezembro");
    }

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
    
    
    /**
     * Calcula o valor com desconto
     * @param val
     * @param desc
     * @return 
     */
    public static BigDecimal valComDesconto(BigDecimal val, double desc){
        
        if(desc == 0.0){
            return val;
        }
        
        BigDecimal descVal = val.multiply(new BigDecimal(desc)).divide(new BigDecimal(100)); 
        
        return(val.subtract(descVal));
    }
    
}
