/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.math.BigDecimal;
import java.util.Date;
import model.Boleto;

/**
 *
 * @author eqalmeida
 */
public interface IPagtoService {
    public void regPagto(Boleto boleto, BigDecimal valorRecebido, Date dataInf);
}
