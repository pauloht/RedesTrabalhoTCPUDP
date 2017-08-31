/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redetcpudptrabalho;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Usuario
 */
public class ClienteTCP {
    public static final int PORTA = 8000;
    public static final String SERVIDOR = "localhost";//"10.81.112.104";
    
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
}
