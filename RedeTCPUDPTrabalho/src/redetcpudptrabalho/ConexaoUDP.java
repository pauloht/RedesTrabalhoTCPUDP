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
            DatagramPacket receivePacket = new DatagramPacket(recebimentoCumprimento, recebimentoCumprimento.length);
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
                    
                    
                    //comecar a enviar arquivo para ip solicitante(nova thread)
                    Thread t1 = new Thread(){
                        @Override
                        public void run(){
                            try{

                                //envio de dados
                                //
                                int bytesLidos = 0;
                                byte[] envio = new byte[1024];
                                BufferedInputStream bInput = new BufferedInputStream(new FileInputStream(file));
                                int numeroDePacote = 0;
                                while (true){
                                    bytesLidos = bInput.read(envio,4,1020);
                                    if (bytesLidos>0){
                                        wrapped.clear();
                                        wrapped.putInt(numeroDePacote);
                                        byte[] idPacote;
                                        idPacote = wrapped.array();
                                        envio[0] = idPacote[0];
                                        envio[1] = idPacote[1];
                                        envio[2] = idPacote[2];
                                        envio[3] = idPacote[3];
                                        DatagramPacket dataPacket = new DatagramPacket(envio, envio.length, ipSolicitante, 9876);
                                        clientSocket.send(dataPacket);
                                        System.out.println("enviando pacote : " + numeroDePacote);
                                        numeroDePacote = numeroDePacote + 1;
                                    }else{
                                        //enviar datagrama de fim de conexao
                                        wrapped.clear();
                                        wrapped.putInt(-1);
                                        byte[] idPacote;
                                        idPacote = wrapped.array();
                                        envio[0] = idPacote[0];
                                        envio[1] = idPacote[1];
                                        envio[2] = idPacote[2];
                                        envio[3] = idPacote[3];
                                        DatagramPacket dataPacket = new DatagramPacket(envio, envio.length, ipSolicitante, 9876);
                                        clientSocket.send(dataPacket);
                                        System.out.println("fim!");
                                        break;
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
        BufferedOutputStream out = null;
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
            //enviou primeira msg, esperar resposta(até 10 segundos)
            serverSocket.setSoTimeout(10000);
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
                
                for (int i=0;i<indicacaoDeRecebimento.length;i++){
                    indicacaoDeRecebimento[0] = false;
                }
                while (true){ // recebe pacotes até receber pacote com numero -1
                    serverSocket.receive(receivePacket);
                    int numeroPacote = 0;
                    byte[] idPacoteB = new byte[4];
                    for (int i=0;i<4;i++){
                        idPacoteB[i] = receiveData[i];
                        
                    }
                    int idPacote = java.nio.ByteBuffer.wrap(idPacoteB).getInt();
                    if (idPacote == -1){
                        System.out.println("fim!");
                        break;
                    }else{
                        out.write(receiveData, 4,1020);
                        System.out.println("recebeu pacote "+idPacote);
                    }
                }
            }
            /*
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            clientSocket.send(sendPacket);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            String modifiedSentence = new String(receivePacket.getData());
            System.out.println("FROM SERVER:" + modifiedSentence);
            */
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
