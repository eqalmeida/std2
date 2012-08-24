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
    public BigDecimal regPagto(BigDecimal valorRecebido, Date dataInf);
    public void setBoleto(Boleto boleto);
    public void setDesconto(double desconto);
}
