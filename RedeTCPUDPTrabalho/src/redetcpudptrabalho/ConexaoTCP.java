/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redetcpudptrabalho;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Usuario
 */
public class ConexaoTCP {
    public static final int PORTA = 8000;
    public static final String SERVIDOR = "localhost";//127.0.0.1 
    
    public static void main(String args[]) {
        Socket socket = null;
        try {
            socket = new Socket(SERVIDOR, PORTA);
            DataOutputStream out = new DataOutputStream(
                    socket.getOutputStream());
            out.writeUTF("Hello!");
            
            DataInputStream in = new DataInputStream(
                            socket.getInputStream());
            System.out.println("Cliente Recebeu: "+
                    in.readUTF());
        } catch (IOException ex) {
            Logger.getLogger("tcp").log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger("tcp").log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /*
            try {
            ServerSocket serverSocket = new ServerSocket(PORTA);
            System.out.println("Aguardando conexao...");
            Socket socket = serverSocket.accept();//bloqueante
            System.out.println("Conectado com: "+
                    socket.getInetAddress().getHostAddress());
            
            DataInputStream in = new DataInputStream(
                            socket.getInputStream());
            System.out.println("Servidor Recebeu: "+
                    in.readUTF());
            
            DataOutputStream out = new DataOutputStream(
                    socket.getOutputStream());
            out.writeUTF("Hello com eco!");
            socket.close();
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger("tcp").log(Level.SEVERE, null, ex);
        }
    */
    
    public void ouvirPedidos(int porta,File file){
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(porta);
            serverSocket.setSoTimeout(1000);
            System.out.println("aguardando conexao");
            while(true){
                try{
                    Socket socket = serverSocket.accept();
                    if (Thread.interrupted()){
                        System.out.println("thread interompida");
                        break;
                    }
                    Thread t1 = new threadDeSocket(socket,file);
                    t1.start();
                }catch(SocketTimeoutException e){
                    if (Thread.interrupted()){
                        System.out.println("thread interompida");
                        break;
                    }else{
                        System.out.println("esperando conexao");
                    }
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }finally{
            if (!(serverSocket==null)){
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ConexaoTCP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public Object solicitarPedido(String servidor,int porta,File destino,boolean interupcaoFlag){
        Socket socket = null;
        BufferedOutputStream out = null;
        long startTime = System.nanoTime();
        boolean erro = false;
        try {
            socket = new Socket(servidor, porta);
            DataInputStream in = new DataInputStream(
                            socket.getInputStream());
            //leitura do tamanho do noem do arquivo
            byte[] tamNome = new byte[4];
            in.read(tamNome);
            int tamanhoNomeArquivo = java.nio.ByteBuffer.wrap(tamNome).getInt();
            //leitura de nome do arquivo
            byte[] nomeArquivo = new byte[tamanhoNomeArquivo];
            in.read(nomeArquivo);
            String nome = new String(nomeArquivo);
            File newFile = new File(destino,nome);
            out = new BufferedOutputStream(new FileOutputStream(newFile));
            //leitura de dados
            int bytesRead;
            byte[] linhaLida = new byte[512];
            int contador = 0;
            do{
                bytesRead = in.read(linhaLida);
                if (bytesRead>0){
                    out.write(linhaLida, 0, bytesRead);
                    //System.out.println("lendo linha "+contador);
                    contador++;
                }
            }while(bytesRead > -1);
            Double elapsedTime = (System.nanoTime()+0.00-startTime+0.00)/1000000000;
            return(elapsedTime);
        } catch (IOException ex) {
            Logger.getLogger("tcp").log(Level.SEVERE, null, ex);
            return(null);
        } finally {
            System.out.println("conexao finalizada!");
            try {
                if (!(socket==null)){
                    socket.close();
                }
                if (!(out==null)){
                    out.close();
                }
            } catch (IOException ex) {
                Logger.getLogger("tcp").log(Level.SEVERE, null, ex);
            }
        }
    }
    
    class threadDeSocket extends Thread{
        Socket socketBuffer = null;
        File arquivo = null;
        threadDeSocket(Socket socket,File arquivo){
            socketBuffer = socket;
            this.arquivo = arquivo;
        }
        @Override
        public void run(){
            try{
                System.out.println("Conexao feita!");
                DataOutputStream out = new DataOutputStream(socketBuffer.getOutputStream());
                BufferedInputStream bInput = new BufferedInputStream(new FileInputStream(arquivo));
                //enviando tamanho do nome do arquivo
                int tamNomeArquivo = arquivo.getName().length();
                ByteBuffer wrapped = ByteBuffer.allocate(4);
                wrapped.putInt(tamNomeArquivo);
                byte[] tamNome;
                tamNome = wrapped.array();
                out.write(tamNome);
                //envio de nome do arquivo
                out.write(arquivo.getName().getBytes());

                //envio de dados
                byte[] linhaLida = new byte[512];
                int bytesLidos = 0;
                while (true){
                    bytesLidos = bInput.read(linhaLida);
                    if (bytesLidos>0){
                    out.write(linhaLida,0,bytesLidos);
                    }else{
                        break;
                    }
                }
                System.out.println("arquivo enviado!");
                out.close();
                socketBuffer.close();
            }catch(Exception e){
                
            }
        }
    }
}