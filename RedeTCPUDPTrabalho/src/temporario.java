
import redetcpudptrabalho.clienteFrame;
import redetcpudptrabalho.servidorFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Usuario
 */
public class temporario {
    public static void main(String args[]){
        Thread t1 = new Thread(){
            @Override
            public void run(){
                clienteFrame c = new clienteFrame();
                c.setVisible(true);
            }
        };

        Thread t2 = new Thread(){
            @Override
            public void run(){
                servidorFrame c = new servidorFrame();
                c.setVisible(true);
            }
        };
        
        t1.start();
        t2.start();
    }
}
