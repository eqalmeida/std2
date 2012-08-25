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
public class PagtoService implements IPagtoService {

    private Boleto boleto = null;
    private double desconto = 0.0;
    
    
    
    @Override
    public BigDecimal regPagto(BigDecimal valorRecebido, Date dataInf) {

    
        BigDecimal valorAtualComTaxas = boleto.getValorAtualComTaxas(dataInf, desconto);
        BigDecimal sobra;
        BigDecimal valParcela;
        

        /*
         * Calcula o valor de sobra
         */
        if (valorRecebido.doubleValue() > valorAtualComTaxas.doubleValue()) {
            sobra = valorRecebido.subtract(valorAtualComTaxas);
            valParcela = valorAtualComTaxas;
        }
        else{
            sobra = BigDecimal.ZERO;
            valParcela = valorRecebido;
        }

        BigDecimal temp = boleto.getJuros();

        if (temp == null) {
            temp = BigDecimal.ZERO;
        }

        // Calcula Juros
        BigDecimal jurosAtual = boleto.getJuros(dataInf, desconto);
        
        boleto.setJuros(temp.add(jurosAtual));

        temp = boleto.getMulta();

        if (temp == null) {
            temp = BigDecimal.ZERO;
        }

        BigDecimal multaAtual = boleto.getMulta(dataInf, desconto);

        boleto.setMulta(temp.add(multaAtual));

        if (boleto.getStatus() == Boleto.ATIVO) {

            boleto.setValorPago(valParcela);

            boleto.setValorFaltante(valorAtualComTaxas.subtract(valParcela));

        } else if (boleto.getStatus() == Boleto.PAGO_PARCIAL) {

            // Salva o novo valor faltante
            boleto.setValorFaltante(valorAtualComTaxas.subtract(valParcela));

            temp = boleto.getValorPago();

            if (temp == null) {
                temp = BigDecimal.ZERO;
            }

            /**
             * O Valor Pago é somado ao valor anterior
             */
            boleto.setValorPago(valParcela.add(temp));

        }

        // Atualiza o Status
        if (boleto.getValorFaltante().doubleValue() > 0.0) {
            boleto.setStatus(Boleto.PAGO_PARCIAL);
        } else {
            boleto.setStatus(Boleto.PAGO);
        }

        boleto.setDataPag(dataInf);
        
        desconto = 0.0;

        return (sobra);
        
    }

    @Override
    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }

    @Override
    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }
}
