/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redetcpudptrabalho;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public void ouvirPedidos(File file,int quantiaBuffers,int tamanhoBuffers){
        DatagramSocket serverSocket = null;
        try{
            DatagramSocket clientSocket = new DatagramSocket();
            serverSocket = new DatagramSocket(6789);
            String cumprimento = "Okay";
            byte[] cumprimentoBytes = cumprimento.getBytes();
            byte[] pacoteCumprimento = new byte[1024];
            byte[] recebimentoCumprimento = new byte[8];
            DatagramPacket receivePacket = new DatagramPacket(recebimentoCumprimento, recebimentoCumprimento.length);
            serverSocket.setSoTimeout(1000);
            while (true){
                //esperar uma solicitação
                try{
                    //System.out.println("esperando cliente");
                    serverSocket.receive(receivePacket);
                    String msgRecebida = new String(receivePacket.getData());
                    System.out.println("msg recebida do cliente = " + msgRecebida);
                    if (msgRecebida.equals("SendData")){
                        //enviando tamanho do nome do arquivo
                        System.arraycopy(cumprimentoBytes, 0, pacoteCumprimento, 0, cumprimentoBytes.length);
                        int tamNomeArquivo = file.getName().length();
                        ByteBuffer wrapped = ByteBuffer.allocate(4);
                        wrapped.putInt(tamNomeArquivo);
                        byte[] tamNome;
                        tamNome = wrapped.array();
                        int movimento = cumprimentoBytes.length;
                        System.arraycopy(tamNome, 0, pacoteCumprimento, movimento, tamNome.length);
                        wrapped.clear();
                        movimento = movimento+tamNome.length;
                        byte[] nomeArquivo = file.getName().getBytes();
                        System.arraycopy(nomeArquivo, 0, pacoteCumprimento, movimento, nomeArquivo.length);
                        movimento = movimento+nomeArquivo.length;
                        wrapped.putInt(quantiaBuffers);
                        byte[] quantiaBuffersB = wrapped.array();
                        System.arraycopy(quantiaBuffersB, 0, pacoteCumprimento, movimento, quantiaBuffersB.length);
                        movimento = movimento+quantiaBuffersB.length;
                        wrapped.clear();
                        wrapped.putInt(tamanhoBuffers);
                        byte[] tamanhoBuffersB = wrapped.array();
                        System.arraycopy(tamanhoBuffersB, 0, pacoteCumprimento, movimento, tamanhoBuffersB.length);
                        //send okay, tam nome do arquivo e nome do arquivo

                        //enviar para um ip solicitante
                        InetAddress ipSolicitante = receivePacket.getAddress();
                        DatagramPacket primeiroPacket = new DatagramPacket(pacoteCumprimento, pacoteCumprimento.length, ipSolicitante, 9876);
                        clientSocket.send(primeiroPacket);

                        //comecar a enviar arquivo para ip solicitante(nova thread)
                        Thread t1 = new Thread(){
                            @Override
                            public void run(){
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Date date = new Date();
                                System.out.println(dateFormat.format(date));
                                DatagramSocket threadServerSocket = null;
                                try{

                                    //envio de dados
                                    //
                                    byte[] retransmissaoBytes = new byte[8+quantiaBuffers*4];
                                    DatagramPacket retransmissaoPacket = new DatagramPacket(retransmissaoBytes, retransmissaoBytes.length);
                                    threadServerSocket = new DatagramSocket(5678);
                                    int bytesLidos = 0;
                                    BufferedInputStream bInput = new BufferedInputStream(new FileInputStream(file));
                                    int numeroDePacote = 0;
                                    byte[][] bufferDePacote = new byte[quantiaBuffers][tamanhoBuffers];
                                    int[] tamanhoReal = new int[quantiaBuffers];
                                    int contadorDeEnvio = 0;
                                    threadServerSocket.setSoTimeout(10000);
                                    boolean arquivoLido = false;
                                    while (true){
                                        if (contadorDeEnvio<quantiaBuffers && (!arquivoLido)){
                                            bytesLidos = bInput.read(bufferDePacote[contadorDeEnvio],4,tamanhoBuffers-4);
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
                                                //System.out.println("enviando pacote : " + numeroDePacote+",count["+(contadorDeEnvio-1)+"] tam : " + bytesLidos);
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
                                                    //System.out.println("Numero retransmitido : " + numeroDePacotesPerdidos);
                                                    int[] indicesRetransmitidos = new int[numeroDePacotesPerdidos];
                                                    for (int i=0;i<numeroDePacotesPerdidos;i++){
                                                        byte[] valorI = new byte[4];
                                                        //System.out.println("lendo byte da pos inicial : " + (7+i*4));
                                                        System.arraycopy(retransmissaoBytes, 7+i*4, valorI, 0, 4);
                                                        indicesRetransmitidos[i] = ByteBuffer.wrap(valorI).getInt();
                                                    }
                                                    for (int i=0;i<indicesRetransmitidos.length;i++){
                                                        if (indicesRetransmitidos[i]<contadorDeEnvio){//retransmissao valida
                                                            byte[] pacote = bufferDePacote[indicesRetransmitidos[i]];
                                                            int tamanho = tamanhoReal[indicesRetransmitidos[i]];
                                                            //System.out.println("prescissa retransmitir : "+indicesRetransmitidos[i]+",contadorDeEnvio="+contadorDeEnvio+"tamanho="+tamanho);
                                                            DatagramPacket dataPacket = new DatagramPacket(pacote, tamanho+4, ipSolicitante, 9876);
                                                            clientSocket.send(dataPacket);
                                                        }else{//retransmissao invalida
                                                            //System.out.println("retransmissao invalida!="+indicesRetransmitidos[i]);
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
                                }finally{
                                    System.out.println("fim thread server!");
                                    if (!(threadServerSocket==null)){
                                        threadServerSocket.close();
                                    }
                                }
                            }
                        };
                        System.out.println("iniciando thread de envio!");
                        t1.start();
                    }else{
                        System.out.println("diferente!");
                    }
                }catch(SocketTimeoutException e){
                    
                }finally{
                    if (Thread.interrupted()){
                        System.out.println("Interompendo thread");
                        break;
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }finally{
            System.out.println("fim thread");
            if (!(serverSocket==null)){
                serverSocket.close();
            }
        }
    }
    
    public Object solicitarPedido(String servidor,File destino,boolean interupcaoFlag){
        BufferedOutputStream out=null;
        DatagramSocket clientSocket = null;
        DatagramSocket serverSocket = null;
        long endTime;
        try{
            clientSocket = new DatagramSocket();
            serverSocket = new DatagramSocket(9876);
            int retransmissaoCumprimento = 10;
            while (retransmissaoCumprimento > 0){
                retransmissaoCumprimento = retransmissaoCumprimento-1;
                byte[] receiveData = new byte[1024];
                String comprimento = "SendData";
                byte[] pacoteCumprimento = comprimento.getBytes();
                InetAddress ipServidor = InetAddress.getByName(servidor);
                DatagramPacket primeiroPacket = new DatagramPacket(pacoteCumprimento, pacoteCumprimento.length, ipServidor, 6789);
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.send(primeiroPacket); 
                //enviou primeira msg, esperar resposta(até 1 segundos)
                serverSocket.setSoTimeout(1000);
                String msgRecebida = "null";
                try{
                    serverSocket.receive(receivePacket);
                    msgRecebida = new String(receivePacket.getData());//recebe okay junto com tamaho do nome arquivo,nome do arquivo,quantia de buffers e tamanho de buffers
                }catch (SocketTimeoutException e){
                    System.out.println("espera timeout!");
                }
                byte[] codigo = new byte[4];//recebe okay
                System.arraycopy(receiveData, 0, codigo, 0, codigo.length);
                int movimento = codigo.length;
                String sCodigo = new String(codigo);
                ArrayList<Object> retorno = new ArrayList<>();
                if (sCodigo.equals("Okay")){
                    System.out.println("Okay recebido iniciando transmissao ....");
                    long startTime = System.nanoTime();
                    //leitura do tamanho do nome do arquivo
                    byte[] tamNome = new byte[4];
                    System.arraycopy(receiveData, movimento, tamNome, 0, tamNome.length);
                    movimento = movimento+tamNome.length;
                    int tamanhoNomeArquivo = ByteBuffer.wrap(tamNome).getInt();
                    byte[] nomeArquivo = new byte[tamanhoNomeArquivo];
                    System.arraycopy(receiveData, movimento, nomeArquivo, 0, nomeArquivo.length);
                    movimento = movimento+nomeArquivo.length;
                    byte[] quantiaBuffersB = new byte[4];
                    System.arraycopy(receiveData, movimento, quantiaBuffersB, 0, quantiaBuffersB.length);
                    movimento = movimento+quantiaBuffersB.length;
                    byte[] tamanhoBuffersB = new byte[4];
                    System.arraycopy(receiveData,movimento,tamanhoBuffersB,0,tamanhoBuffersB.length);
                    int quantiaBuffers = ByteBuffer.wrap(quantiaBuffersB).getInt();
                    int tamanhoBuffers = ByteBuffer.wrap(tamanhoBuffersB).getInt();
                    String nome = new String(nomeArquivo);
                    File newFile = new File(destino,nome);
                    out = new BufferedOutputStream(new FileOutputStream(newFile));
                    boolean[] indicacaoDeRecebimento = new boolean[quantiaBuffers];
                    byte[][] bufferDeRecebimento = new byte[quantiaBuffers][tamanhoBuffers];
                    int[] tamanhoReal = new int[quantiaBuffers];

                    for (int i=0;i<indicacaoDeRecebimento.length;i++){
                        indicacaoDeRecebimento[i] = false;
                    }
                    int maximo = quantiaBuffers;
                    int numeroRecebido = 0;
                    boolean estadoFinal = false;
                    byte[] ackByte = new byte[8+quantiaBuffers*4];
                    DatagramPacket pacoteDeAck = new DatagramPacket(ackByte, ackByte.length, ipServidor, 5678);
                    byte[] ack = "ACK".getBytes();
                    byte[] nck = "NCK".getBytes();
                    byte[] fin = "FIN".getBytes();
                    ByteBuffer wrapper = ByteBuffer.allocate(4);
                    int contadorDeRetransmissoes = 0;
                    int maxNumeroDeRetransmissaoDeAcks = 100;
                    boolean nackMontado = false;
                    serverSocket.setSoTimeout(100);
                    int tamanhoRecebido = 0;
                    int fimDeArquivoUltimoBuffer = 0;
                    int tamanhoAcumulado = 0;
                    int pacotesValidos = 0;
                    int pacotesRetransmitidos = 0;
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
                                    //System.out.println("fin");
                                    out.flush();
                                    System.arraycopy(fin, 0, ackByte, 0, ack.length);
                                    clientSocket.send(pacoteDeAck);
                                    break;
                                }else{ //retransmissao invalida
                                    fimDeArquivoUltimoBuffer = numeroDePacotesRestantes;
                                    //System.out.println("Ja recebeu todos os pacotes necessarios");
                                    if (numeroRecebido>=fimDeArquivoUltimoBuffer){
                                            //ja recebeu os ultimos pacotes, so prescissa escreve-los
                                            for (int i=0;i<indicacaoDeRecebimento.length;i++){
                                                indicacaoDeRecebimento[i] = false;
                                            }
                                            //System.out.println("escrevendo ultimos "+(fimDeArquivoUltimoBuffer+1)+" pacotes.");
                                            //System.out.println("ultimos pacotes : " + (quantiaBuffers-(fimDeArquivoUltimoBuffer+1)));
                                            pacotesRetransmitidos = pacotesRetransmitidos-(quantiaBuffers-(fimDeArquivoUltimoBuffer+1));
                                            for (int i=0;i<=fimDeArquivoUltimoBuffer;i++){
                                                out.write(bufferDeRecebimento[i],4,tamanhoReal[i]);
                                                tamanhoAcumulado = tamanhoAcumulado+tamanhoReal[i];
                                            }
                                            System.arraycopy(fin, 0, ackByte, 0, ack.length);
                                            clientSocket.send(pacoteDeAck);
                                            out.flush();
                                            break;
                                    }else{
                                        //System.out.println("fim="+fimDeArquivoUltimoBuffer+",buffer="+numeroRecebido);
                                    }
                                }
                            }else{
                                int valorMovido = idPacote-maximo+quantiaBuffers;
                                if (valorMovido>=quantiaBuffers || valorMovido<0){
                                    //System.out.println("id invalido "+idPacote);
                                }else{
                                    if (!(indicacaoDeRecebimento[valorMovido])){ //pacote ainda nao foi recebido
                                        pacotesValidos = pacotesValidos + 1;
                                        //System.out.println("recebeu " + valorMovido + ",id="+idPacote+",tam="+tamanhoRecebido);
                                        System.arraycopy(receiveData, 0, bufferDeRecebimento[valorMovido], 0, tamanhoRecebido+4);
                                        tamanhoReal[valorMovido] = tamanhoRecebido;
                                        indicacaoDeRecebimento[valorMovido] = true;//sinaliza recebimento
                                        numeroRecebido = numeroRecebido+1;
                                        if (numeroRecebido>=quantiaBuffers){
                                            //ja recebeu quantiaBuffers pacotes, escreve os quantiaBuffers pacotes e prescissa receber proximos 10
                                            for (int i=0;i<indicacaoDeRecebimento.length;i++){
                                                indicacaoDeRecebimento[i] = false;
                                            }
                                            //System.out.println("escrevendo "+quantiaBuffers+" pacotes");
                                            for (int i=0;i<indicacaoDeRecebimento.length;i++){
                                                out.write(bufferDeRecebimento[i],4,tamanhoReal[i]);
                                                tamanhoAcumulado = tamanhoAcumulado+tamanhoReal[i];
                                            }
                                            numeroRecebido = 0;
                                            maximo = maximo+quantiaBuffers;
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
                                    int pacotesRestante = quantiaBuffers-numeroRecebido;
                                    pacotesRetransmitidos = pacotesRetransmitidos + pacotesRestante;
                                    //System.out.println("ainda faltam "+pacotesRestante);
                                    wrapper.clear();
                                    wrapper.putInt(pacotesRestante);
                                    byte[] pacotesR = wrapper.array();
                                    System.arraycopy(pacotesR, 0, ackByte, nck.length, pacotesR.length);
                                    int count = 0;
                                    for (int i=0;i<indicacaoDeRecebimento.length;i++){
                                        if (!(indicacaoDeRecebimento[i])){
                                            //System.out.println("falta pacote " + i);
                                            wrapper.clear();
                                            wrapper.putInt(i);
                                            byte[] array = wrapper.array();
                                            //System.out.println("gravando int na pos inicial : " + (nck.length+pacotesR.length+count*4) + ",valor = " + ByteBuffer.wrap(array).getInt());
                                            System.arraycopy(array,0,ackByte,nck.length+pacotesR.length+count*4,array.length);
                                            count = count+1;
                                        }
                                    }
                                    clientSocket.send(pacoteDeAck);
                                    nackMontado = true;
                                }else{
                                    //System.out.println("retransmitindo nack");
                                    clientSocket.send(pacoteDeAck);
                                }
                            }
                        }
                    }
                endTime = System.nanoTime();
                double elapsedSegundos = (endTime-startTime+0.00)/1000000000.0;
                double retransmissaoPercentagem = ((pacotesRetransmitidos+0.00)/(pacotesValidos+0.00))*100;
                //System.out.println("tamanho escrito = " + tamanhoAcumulado+"bytes");
                //System.out.println("PacotesTotal : " + (pacotesValidos+pacotesRetransmitidos) + ",PacotesValidos : " +pacotesValidos+ ",PacotesRetransmitidos : " +pacotesRetransmitidos+ ",Porcetagem Retransmitida(Em relação a pacotes validos) : " + String.format("%.2f %%", retransmissaoPercentagem));
                retorno.add(elapsedSegundos);
                retorno.add(retransmissaoPercentagem);
                int pacotesTotal = pacotesValidos+pacotesRetransmitidos;
                retorno.add(pacotesTotal);
                return(retorno);
                }else{
                    //System.out.println("Recebeu msg : " + sCodigo);
                    System.out.println("retransmissao okay");
                }
            }
            return(new Integer(-3));
        }catch(SocketTimeoutException e){
            return(new Integer(-1));
        }catch(Exception e){
            e.printStackTrace();
            return(new Integer(-2));
        }finally{
            try{
                if (!(out==null)){
                    out.close();
                }
                if (!(clientSocket==null)){
                    clientSocket.close();
                }
                if (!(serverSocket==null)){
                    serverSocket.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
