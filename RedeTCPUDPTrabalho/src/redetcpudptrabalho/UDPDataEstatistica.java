/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redetcpudptrabalho;

import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author FREE
 */
public class UDPDataEstatistica {
    double tempoSegundos;
    double retransmissaoPorcentagem;
    
    public UDPDataEstatistica(double tempoSegundos,double retransmissaoP){
        this.tempoSegundos = tempoSegundos;
        this.retransmissaoPorcentagem = retransmissaoP;
    }
    
    public static double[] gerarMedidas(ArrayList<UDPDataEstatistica> udps){
        double mediaTempoSegundos = 0.00;
        double mediaRetransmisaso = 0.00;
        double desvioPadraoTempo = 0.00;
        double desvioPadraoRetransmissao = 0.00;
        for (UDPDataEstatistica udp : udps){
            mediaTempoSegundos = mediaTempoSegundos + udp.tempoSegundos;
            mediaRetransmisaso = mediaRetransmisaso + udp.retransmissaoPorcentagem;
        }
        int len = udps.size();
        mediaTempoSegundos = mediaTempoSegundos/len;
        mediaRetransmisaso = mediaRetransmisaso/len;
        for (UDPDataEstatistica udp : udps){ //soma do quadrado das diferencas
            desvioPadraoTempo = Math.pow(mediaTempoSegundos-udp.tempoSegundos,2);
            desvioPadraoRetransmissao = Math.pow(mediaRetransmisaso-udp.retransmissaoPorcentagem,2);
        }
        //media do quadrado das diferencas seguido da raiz quadrado do mesmo
        desvioPadraoTempo = desvioPadraoTempo/len;
        desvioPadraoRetransmissao = desvioPadraoRetransmissao/len;
        desvioPadraoTempo = Math.sqrt(desvioPadraoTempo);
        desvioPadraoRetransmissao = Math.sqrt(desvioPadraoRetransmissao);
        double[] retorno = new double[4];
        retorno[0] = mediaTempoSegundos;
        retorno[1] = mediaRetransmisaso;
        retorno[2] = desvioPadraoTempo;
        retorno[3] = desvioPadraoRetransmissao;
        return(retorno);
    }
}
