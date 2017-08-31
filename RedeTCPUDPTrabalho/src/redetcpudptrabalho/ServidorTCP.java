/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redetcpudptrabalho;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Usuario
 */
public class ServidorTCP {
    public static final int PORTA = 8000;
    public static final String SERVIDOR = "localhost";//127.0.0.1
    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
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
    }
}