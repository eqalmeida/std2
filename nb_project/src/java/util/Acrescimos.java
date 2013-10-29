package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import model.Boleto;
import model.PedidoPag;

/**
 * Esta classe calcula os acrescimos de uma ou mais parcelas.
 *
 * @author eqalmeida
 */
public class Acrescimos {

    private final Date dataPagamento;
    private BigDecimal totalValorParcelas = BigDecimal.ZERO;
    private BigDecimal totalMultas = BigDecimal.ZERO;
    private BigDecimal totalJuros = BigDecimal.ZERO;
    private double desconto = 0.0;

    /**
     * Construtor
     *
     * @param dataPagamento A data do pagamento realizado.
     */
    public Acrescimos(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Acrescimos(Date dataPagamento, Boleto boleto) {
        this.dataPagamento = dataPagamento;

        if(boleto.getPedidoPag() == null){
            throw new RuntimeException("Boleto sem pedido!");
        }        
        addicionaParcela(boleto);
    }

    public Acrescimos(Date dataPagamento, PedidoPag pag) {
        this.dataPagamento = dataPagamento;

        if(pag == null){
            throw new RuntimeException("Boleto sem pedido!");
        }        
        
        long pagDays = dateToDays(dataPagamento);

        for(Boleto b : pag.getParcelas()){
            long parcelaDays = dateToDays(b.getVencimentoAtual());
            if(parcelaDays >= pagDays){
                addicionaParcela(b);
            }
        }
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        if(desconto < 0.0 || desconto > 100.0){
            throw new RuntimeException("Valor de desconto inválido!");
        }
        this.desconto = desconto;
    }
    
    

    //TODO: Tratar a cobraça ou não da multa no caso de parcela pago parcial.
    /**
     * Adiciona parcela no calculo de Acrescimos.
     *
     * @param boleto
     */
    public final void addicionaParcela(Boleto boleto) {

        // Verifica se este boleto já está pago ou cancelado.
        if (boleto.getStatus() == Boleto.PAGO
                || boleto.getStatus() == Boleto.CANCELADO) {
            return;
        }
        
        Date vencimentoReal = boleto.getVencimentoAtual();

        // Calcula quantos dias de atraso.
        long diasDeAtraso = daysDiff(vencimentoReal, dataPagamento);

        if (diasDeAtraso > 0) {

            double val = boleto.getValorAtual().doubleValue();
            double m = boleto.getPedidoPag().getMultaPercent();
            double multaVal = 0.0;

            //
            // Este trecho garante que a multa seja cobrada uma única vez.
            //
            if (!(boleto.getStatus() == Boleto.PAGO_PARCIAL && boleto.isPagoEmAtraso())) {

                multaVal = boleto.getPedidoPag().getMultaVal().doubleValue();

                if (m > 0.0) {
                    // Calcula a multa por porcentagem.
                    double temp = (val * m) / 100.0;

                    multaVal += temp;
                }

                // Soma as multas ao valor da parcela.
                val += multaVal;
            }
            
            // Soma a multa ao total
            totalMultas = totalMultas.add(new BigDecimal(multaVal));

            double taxaJuros = boleto.getPedidoPag().getJurosDiario();
            double jurosVal = 0.0;

            if (taxaJuros > 0.0) {
                // Calcula o atraso de um dia.
                jurosVal = (val * taxaJuros) / 100.0;
                // Calcula o atraso pelo numero de dias.
                jurosVal *= diasDeAtraso;
            }

            // Soma o valor de juros acumulado
            totalJuros = totalJuros.add(new BigDecimal(jurosVal));
        }

        // Soma o valor da parcela
        totalValorParcelas = totalValorParcelas.add(boleto.getValorAtual());

        // Coloca os valores na escala correta.
        totalMultas.setScale(2, RoundingMode.FLOOR);
        totalJuros.setScale(2, RoundingMode.FLOOR);
        totalValorParcelas.setScale(2, RoundingMode.FLOOR);
    }
    
    public BigDecimal getDescontoVal(){
        return(getTaxasSemDesconto().subtract(getTaxasComDesconto()));
    }

    public BigDecimal getTotalDevido() {
        BigDecimal temp = getTaxasComDesconto().add(totalValorParcelas);
        temp.setScale(2, RoundingMode.FLOOR);
        return (temp);
    }
    
    public BigDecimal getTaxasSemDesconto(){
        return totalJuros.add(totalMultas);
    }

    public BigDecimal getTaxasComDesconto(){
        BigDecimal taxas = getTaxasSemDesconto();
        double descontos = (taxas.doubleValue() * desconto)/ 100.0;
        BigDecimal val = taxas.subtract(new BigDecimal(descontos));
        val.setScale(2, RoundingMode.FLOOR);
        return val;
    }

    public BigDecimal getTotalValorParcelas() {
        return totalValorParcelas;
    }

    public BigDecimal getTotalMultas() {
        return (totalMultas);
    }

    public BigDecimal getTotalJuros() {
        return (totalJuros);
    }

    /**
     * Converte uma data em um número que representa o dia.
     *
     * @param d Data
     * @return Numero representando o dia.
     */
    public static long dateToDays(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (c.getTimeInMillis() / (24 * 60 * 60 * 1000));
    }

    /**
     * Obtem a diferença entre duas data em numero de dias.
     *
     * @param from Data inicial
     * @param to Data final
     * @return diferença
     */
    public static long daysDiff(Date from, Date to) {

        long daysFrom = dateToDays(from);
        long daysTo = dateToDays(to);
        return (daysTo - daysFrom);
    }
}
