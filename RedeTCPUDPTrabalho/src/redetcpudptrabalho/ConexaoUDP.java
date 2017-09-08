/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redetcpudptrabalho;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author FREE
 */
public class ConexaoUDP {
    //Cliente UDP
    /*
       BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
       DatagramSocket clientSocket = new DatagramSocket();
       InetAddress IPAddress = InetAddress.getByName("localhost");
       byte[] sendData = new byte[1024];
       byte[] receiveData = new byte[1024];
       String sentence = inFromUser.readLine();
       sendData = sentence.getBytes();
       DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
       clientSocket.send(sendPacket);
       DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
       clientSocket.receive(receivePacket);
       String modifiedSentence = new String(receivePacket.getData());
       System.out.println("FROM SERVER:" + modifiedSentence);
       clientSocket.close();
    */
    
    //Servidor UDP
    /*
        DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true)
        {
           DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
           serverSocket.receive(receivePacket);
           String sentence = new String( receivePacket.getData());
           System.out.println("RECEIVED: " + sentence);
           InetAddress IPAddress = receivePacket.getAddress();
           int port = receivePacket.getPort();
           String capitalizedSentence = sentence.toUpperCase();
           sendData = capitalizedSentence.getBytes();
           DatagramPacket sendPacket =
           new DatagramPacket(sendData, sendData.length, IPAddress, port);
           serverSocket.send(sendPacket);
        }
    */
    public void ouvirPedidos(File file){
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            DatagramSocket serverSocket = new DatagramSocket(6789);
            String cumprimento = "Okay";
            byte[] cumprimentoBytes = cumprimento.getBytes();
            byte[] pacoteCumprimento = new byte[1024];
            byte[] recebimentoCumprimento = new byte[8];
            byte[] retransmissaoBytes = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(recebimentoCumprimento, recebimentoCumprimento.length);
            DatagramPacket retransmissaoPacket = new DatagramPacket(retransmissaoBytes, retransmissaoBytes.length);
            while (true){
                //esperar uma solicitação
                serverSocket.receive(receivePacket);
                String msgRecebida = new String(receivePacket.getData());
                System.out.println("msg recebida do cliente = " + msgRecebida);
                if (msgRecebida.equals("SendData")){
                    //enviando tamanho do nome do arquivo
                    int tamNomeArquivo = file.getName().length();
                    ByteBuffer wrapped = ByteBuffer.allocate(4);
                    wrapped.putInt(tamNomeArquivo);
                    byte[] tamNome;
                    tamNome = wrapped.array();
                    byte[] nomeArquivo = file.getName().getBytes();
                    //send okay, tam nome do arquivo e nome do arquivo

                    //enviar para um ip solicitante
                    InetAddress ipSolicitante = receivePacket.getAddress();
                    for (int i=0;i<cumprimentoBytes.length;i++){
                        pacoteCumprimento[i] = cumprimentoBytes[i];
                    }
                    for (int i=0;i<tamNome.length;i++){
                        pacoteCumprimento[i+cumprimentoBytes.length] = tamNome[i];
                    }
                    for (int i=0;i<nomeArquivo.length;i++){
                        pacoteCumprimento[i+cumprimentoBytes.length+tamNome.length] = nomeArquivo[i];
                    }
                    DatagramPacket primeiroPacket = new DatagramPacket(pacoteCumprimento, pacoteCumprimento.length, ipSolicitante, 9876);
                    clientSocket.send(primeiroPacket);
                    int bufferMaxSize = 1024;
                    int quantosBuffers = 10;
                    
                    //comecar a enviar arquivo para ip solicitante(nova thread)
                    Thread t1 = new Thread(){
                        @Override
                        public void run(){
                            try{

                                //envio de dados
                                //
                                DatagramSocket threadServerSocket = new DatagramSocket(5678);
                                int bytesLidos = 0;
                                BufferedInputStream bInput = new BufferedInputStream(new FileInputStream(file));
                                int numeroDePacote = 0;
                                byte[][] bufferDePacote = new byte[10][1024];
                                int[] tamanhoReal = new int[10];
                                int contadorDeEnvio = 0;
                                threadServerSocket.setSoTimeout(1000);
                                int repeticoesFIN = 3;
                                boolean arquivoLido = false;
                                while (true){
                                    if (contadorDeEnvio<10 && (!arquivoLido)){
                                        bytesLidos = bInput.read(bufferDePacote[contadorDeEnvio],4,1020);
                                        if (bytesLidos>0){
                                            contadorDeEnvio = contadorDeEnvio+1;
                                            wrapped.clear();
                                            wrapped.putInt(numeroDePacote);
                                            byte[] idPacote;
                                            idPacote = wrapped.array();
                                            tamanhoReal[contadorDeEnvio-1] = bytesLidos;
                                            bufferDePacote[contadorDeEnvio-1][0] = idPacote[0];
                                            bufferDePacote[contadorDeEnvio-1][1] = idPacote[1];
                                            bufferDePacote[contadorDeEnvio-1][2] = idPacote[2];
                                            bufferDePacote[contadorDeEnvio-1][3] = idPacote[3];
                                            DatagramPacket dataPacket = new DatagramPacket(bufferDePacote[contadorDeEnvio-1], tamanhoReal[contadorDeEnvio-1]+4, ipSolicitante, 9876);
                                            clientSocket.send(dataPacket);
                                            System.out.println("enviando pacote : " + numeroDePacote+",count["+(contadorDeEnvio-1)+"] tam : " + bytesLidos);
                                            numeroDePacote = numeroDePacote + 1;
                                        }else{//bytes lidos <0, so eh prescisso confirmar o envio dos pacotes e terminar conexao
                                            arquivoLido = true;
                                        }
                                    }else{//espera resposta do cliente(se ele recebeu todos os pacotes ou prescissa retransmitir
                                        try{
                                            threadServerSocket.receive(retransmissaoPacket);
                                            byte[] codigo = new byte[3];//codigo ACK -> Sucesso nos pacotes ou NCK - erro seguido de lista de pacotes que prescissam retransmissao ou FIN - fim da conexao
                                            codigo[0] = retransmissaoBytes[0];
                                            codigo[1] = retransmissaoBytes[1];
                                            codigo[2] = retransmissaoBytes[2];
                                            String sCod = new String(codigo);
                                            if (sCod.equals("ACK")){//prescissa retransmitir proximos 10 pacotes
                                                contadorDeEnvio = 0;
                                            }else if(sCod.equals("NCK")){//prescissa retransmitir x pacotes
                                                byte[] bInt = new byte[4];
                                                System.arraycopy(retransmissaoBytes, 3, bInt, 0, 4);
                                                int numeroDePacotesPerdidos = ByteBuffer.wrap(bInt).getInt();
                                                System.out.println("Numero retransmitido : " + numeroDePacotesPerdidos);
                                                int[] indicesRetransmitidos = new int[numeroDePacotesPerdidos];
                                                for (int i=0;i<numeroDePacotesPerdidos;i++){
                                                    byte[] valorI = new byte[4];
                                                    System.out.println("lendo byte da pos inicial : " + (7+i*4));
                                                    System.arraycopy(retransmissaoBytes, 7+i*4, valorI, 0, 4);
                                                    indicesRetransmitidos[i] = ByteBuffer.wrap(valorI).getInt();
                                                }
                                                for (int i=0;i<indicesRetransmitidos.length;i++){
                                                    if (indicesRetransmitidos[i]<contadorDeEnvio){//retransmissao valida
                                                        byte[] pacote = bufferDePacote[indicesRetransmitidos[i]];
                                                        int tamanho = tamanhoReal[indicesRetransmitidos[i]];
                                                        System.out.println("prescissa retransmitir : "+indicesRetransmitidos[i]+",contadorDeEnvio="+contadorDeEnvio+"tamanho="+tamanho);
                                                        DatagramPacket dataPacket = new DatagramPacket(pacote, tamanho+4, ipSolicitante, 9876);
                                                        clientSocket.send(dataPacket);
                                                    }else{//retransmissao invalida
                                                        System.out.println("retransmissao invalida!="+indicesRetransmitidos[i]);
                                                        wrapped.clear();
                                                        wrapped.putInt(-1);
                                                        byte[] idPacote;
                                                        idPacote = wrapped.array();
                                                        System.arraycopy(idPacote, 0, bufferDePacote[contadorDeEnvio-1], 0, 4);
                                                        wrapped.clear();
                                                        wrapped.putInt(contadorDeEnvio-1);
                                                        idPacote = wrapped.array();
                                                        System.arraycopy(idPacote, 0, bufferDePacote[contadorDeEnvio-1], 4, 4);
                                                        DatagramPacket dataPacket = new DatagramPacket(bufferDePacote[contadorDeEnvio-1], bufferDePacote[contadorDeEnvio-1].length, ipSolicitante, 9876);
                                                        clientSocket.send(dataPacket);
                                                    }
                                                }
                                            }
                                            else if(sCod.equals("FIN")){//confirmacao de fim de transmissao
                                                System.out.println("FIN recebido!");
                                                break;
                                            }else{
                                                System.out.println("Codigo nao identificado = " + sCod);
                                            }
                                            if (arquivoLido){
                                                //enviar datagrama de fim de conexao
                                                wrapped.clear();
                                                wrapped.putInt(-1);
                                                byte[] idPacote;
                                                idPacote = wrapped.array();
                                                System.arraycopy(idPacote, 0, bufferDePacote[contadorDeEnvio-1], 0, 4);
                                                wrapped.clear();
                                                wrapped.putInt(-1);
                                                idPacote = wrapped.array();
                                                System.arraycopy(idPacote, 0, bufferDePacote[contadorDeEnvio-1], 4, 4);
                                                DatagramPacket dataPacket = new DatagramPacket(bufferDePacote[contadorDeEnvio-1], bufferDePacote[contadorDeEnvio-1].length, ipSolicitante, 9876);
                                                clientSocket.send(dataPacket);
                                                System.out.println("fim thread server!");
                                                break;
                                            }
                                        }catch(SocketTimeoutException e){
                                            //cliente desconectado?
                                            System.out.println("Thread Timeout");
                                            break;
                                        }
                                    }
                                }
                            }catch(Exception e){
                                System.out.println("erro thread!");
                                e.printStackTrace();
                            }
                        }
                    };
                    System.out.println("iniciando thread de envio!");
                    t1.start();
                }else{
                    System.out.println("diferente!");
                }
            }
        } catch(Exception e){
            
        }finally{
            
        }
    }
    
    public int solicitarPedido(String servidor,File destino,boolean interupcaoFlag){
        boolean flagDeEspera = false;
        /*
        Thread t1 = new Thread(){
            @Override public void run(){
                try{
                    for (int i=0;i<10;i++){
                        Thread.sleep(1000);
                        if (flagDeEspera){
                            
                        }
                    }
                }catch(Exception e){
                    
                }
            }
        }
        */
        BufferedOutputStream out=null;
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            DatagramSocket serverSocket = new DatagramSocket(9876);
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            String comprimento = "SendData";
            byte[] pacoteCumprimento = comprimento.getBytes();
            InetAddress ipServidor = InetAddress.getByName(servidor);
            DatagramPacket primeiroPacket = new DatagramPacket(pacoteCumprimento, pacoteCumprimento.length, ipServidor, 6789);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.send(primeiroPacket); 
            //enviou primeira msg, esperar resposta(até 1 segundos)
            serverSocket.setSoTimeout(1000);
            serverSocket.receive(receivePacket);
            String msgRecebida = new String(receivePacket.getData());//recebe okay junto com tamaho do arquivo e nome do arquivo
            byte[] codigo = new byte[4];//recebe okay
            for (int i=0;i<4;i++){
                codigo[i] = receiveData[i];
            }
            String sCodigo = new String(codigo);
            if (sCodigo.equals("Okay")){
                //leitura do tamanho do nome do arquivo
                byte[] tamNome = new byte[4];
                for (int i=0;i<4;i++){
                    tamNome[i] = receiveData[i+4];
                }
                int tamanhoNomeArquivo = java.nio.ByteBuffer.wrap(tamNome).getInt();
                //leitura de nome do arquivo
                byte[] nomeArquivo = new byte[tamanhoNomeArquivo];
                for (int i=0;i<tamanhoNomeArquivo;i++){
                    nomeArquivo[0+i] = receiveData[8+i];
                }
                String nome = new String(nomeArquivo);
                File newFile = new File(destino,nome);
                out = new BufferedOutputStream(new FileOutputStream(newFile));
                //
                boolean[] indicacaoDeRecebimento = new boolean[10];
                byte[][] bufferDeRecebimento = new byte[10][1024];
                int[] tamanhoReal = new int[10];
                
                for (int i=0;i<indicacaoDeRecebimento.length;i++){
                    indicacaoDeRecebimento[i] = false;
                }
                int maximo = 10;
                int numeroRecebido = 0;
                boolean estadoFinal = false;
                byte[] ackByte = new byte[1024];
                DatagramPacket pacoteDeAck = new DatagramPacket(ackByte, ackByte.length, ipServidor, 5678);
                byte[] ack = "ACK".getBytes();
                byte[] nck = "NCK".getBytes();
                byte[] fin = "FIN".getBytes();
                ByteBuffer wrapper = ByteBuffer.allocate(4);
                int contadorDeRetransmissoes = 0;
                int maxNumeroDeRetransmissaoDeAcks = 3;
                boolean nackMontado = false;
                serverSocket.setSoTimeout(100);
                int tamanhoRecebido = 0;
                int fimDeArquivoUltimoBuffer = 0;
                int tamanhoAcumulado = 0;
                while (true){ // recebe pacotes até receber pacote com numero -1
                    try{
                        serverSocket.receive(receivePacket);
                        tamanhoRecebido = receivePacket.getLength()-4;
                        contadorDeRetransmissoes = 0;
                        nackMontado = false;
                        byte[] idPacoteB = new byte[4];
                        System.arraycopy(receiveData, 0, idPacoteB, 0, 4);
                        int idPacote = java.nio.ByteBuffer.wrap(idPacoteB).getInt();
                        if (idPacote == -1){
                            System.arraycopy(receiveData,4, idPacoteB, 0, 4);
                            int numeroDePacotesRestantes = ByteBuffer.wrap(idPacoteB).getInt();
                            if (numeroDePacotesRestantes==-1){
                                System.out.println("fin");
                                out.flush();
                                System.arraycopy(fin, 0, ackByte, 0, ack.length);
                                clientSocket.send(pacoteDeAck);
                                break;
                            }else{ //retransmissao invalida
                                fimDeArquivoUltimoBuffer = numeroDePacotesRestantes;
                                System.out.println("Ja recebeu todos os pacotes necessarios");
                                if (numeroRecebido>=fimDeArquivoUltimoBuffer){
                                        //ja recebeu os ultimos pacotes, so prescissa escreve-los
                                        for (int i=0;i<indicacaoDeRecebimento.length;i++){
                                            indicacaoDeRecebimento[i] = false;
                                        }
                                        System.out.println("escrevendo ultimos "+(fimDeArquivoUltimoBuffer+1)+" pacotes.");
                                        for (int i=0;i<=fimDeArquivoUltimoBuffer;i++){
                                            out.write(bufferDeRecebimento[i],4,tamanhoReal[i]);
                                            tamanhoAcumulado = tamanhoAcumulado+tamanhoReal[i];
                                        }
                                        System.arraycopy(fin, 0, ackByte, 0, ack.length);
                                        clientSocket.send(pacoteDeAck);
                                        out.flush();
                                        break;
                                }else{
                                    System.out.println("fim="+fimDeArquivoUltimoBuffer+",buffer="+numeroRecebido);
                                }
                            }
                        }else{
                            int valorMovido = idPacote-maximo+10;
                            if (valorMovido>=10 || valorMovido<0){
                                System.out.println("id invalido "+idPacote);
                            }else{
                                if (!(indicacaoDeRecebimento[valorMovido])){ //pacote ainda nao foi recebido
                                    System.out.println("recebeu " + valorMovido + ",id="+idPacote+",tam="+tamanhoRecebido);
                                    System.arraycopy(receiveData, 0, bufferDeRecebimento[valorMovido], 0, 1024);
                                    tamanhoReal[valorMovido] = tamanhoRecebido;
                                    indicacaoDeRecebimento[valorMovido] = true;//sinaliza recebimento
                                    numeroRecebido = numeroRecebido+1;
                                    if (numeroRecebido>=10){
                                        //ja recebeu 10 pacotes, escreve os 10 pacotes e prescissa receber proximos 10
                                        for (int i=0;i<indicacaoDeRecebimento.length;i++){
                                            indicacaoDeRecebimento[i] = false;
                                        }
                                        System.out.println("escrevendo 10 pacotes");
                                        for (int i=0;i<indicacaoDeRecebimento.length;i++){
                                            out.write(bufferDeRecebimento[i],4,tamanhoReal[i]);
                                            tamanhoAcumulado = tamanhoAcumulado+tamanhoReal[i];
                                        }
                                        numeroRecebido = 0;
                                        maximo = maximo+10;
                                        System.arraycopy(ack, 0, ackByte, 0, ack.length);
                                        clientSocket.send(pacoteDeAck);
                                    }
                                }
                            }
                        }
                    }catch(SocketTimeoutException e){
                        if (estadoFinal){
                            throw e;
                        }else{
                            contadorDeRetransmissoes = contadorDeRetransmissoes+1;
                            if (contadorDeRetransmissoes>=maxNumeroDeRetransmissaoDeAcks){
                                estadoFinal = true;
                            }
                            if (!(nackMontado)){//monta pacote NCK
                                //pedir retransmissao de pacotes nao encontrados
                                System.arraycopy(nck, 0, ackByte, 0, nck.length);
                                int pacotesRestante = 10-numeroRecebido;
                                System.out.println("ainda faltam "+pacotesRestante);
                                wrapper.clear();
                                wrapper.putInt(pacotesRestante);
                                byte[] pacotesR = wrapper.array();
                                System.arraycopy(pacotesR, 0, ackByte, nck.length, pacotesR.length);
                                int count = 0;
                                for (int i=0;i<indicacaoDeRecebimento.length;i++){
                                    if (!(indicacaoDeRecebimento[i])){
                                        System.out.println("falta pacote " + i);
                                        wrapper.clear();
                                        wrapper.putInt(i);
                                        byte[] array = wrapper.array();
                                        System.out.println("gravando int na pos inicial : " + (nck.length+pacotesR.length+count*4) + ",valor = " + ByteBuffer.wrap(array).getInt());
                                        System.arraycopy(array,0,ackByte,nck.length+pacotesR.length+count*4,array.length);
                                        count = count+1;
                                    }
                                }
                                clientSocket.send(pacoteDeAck);
                                nackMontado = true;
                            }else{
                                System.out.println("retransmitindo nack");
                                clientSocket.send(pacoteDeAck);
                            }
                        }
                    }
                    finally{
                        System.out.println("tamanho escrito = " + tamanhoAcumulado);
                    }
                }
            }
            clientSocket.close();
            return(0);
        }catch(SocketTimeoutException e){
            return(-1);
        }catch(Exception e){
            e.printStackTrace();
            return(-2);
        }finally{
            try{
                if (!(out==null)){
                    out.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
