/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redetcpudptrabalho;

import java.awt.Color;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Usuario
 */
public class servidorFrame extends javax.swing.JFrame {
    File fileBuffer = null;
    JFileChooser fcBuffer = null;
    int portaValor = 5963;
    boolean portaLock = false;
    boolean usarTCP = true;
    Thread t1 = null;
    boolean decisao = false;
    /**
     * Creates new form NovoJFrame
     */
    public servidorFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btSelecionarArquivo = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbArquivoNome = new javax.swing.JLabel();
        lbTamanhoArquivo = new javax.swing.JLabel();
        tbLiberarArquivo = new javax.swing.JToggleButton();
        pConfig = new javax.swing.JPanel();
        tbTransporte = new javax.swing.JToggleButton();
        tfPorta = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tfQuantiaBuffers = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tfTamanhoBuffers = new javax.swing.JTextField();
        lbInfo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        btSelecionarArquivo.setText("Selecionar Arquivo");
        btSelecionarArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelecionarArquivoActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Arquivo :");

        jLabel2.setText("Tam :");

        lbArquivoNome.setText("NULL");
        lbArquivoNome.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        lbTamanhoArquivo.setText("NULL");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbTamanhoArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
            .addComponent(lbArquivoNome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbArquivoNome, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lbTamanhoArquivo))
                .addContainerGap())
        );

        tbLiberarArquivo.setText("LiberarArquivo");
        tbLiberarArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbLiberarArquivoActionPerformed(evt);
            }
        });

        tbTransporte.setText("TCP");
        tbTransporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbTransporteActionPerformed(evt);
            }
        });

        tfPorta.setText("5963");
        tfPorta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfPortaActionPerformed(evt);
            }
        });
        tfPorta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfPortaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfPortaKeyReleased(evt);
            }
        });

        jLabel3.setText("Porta :");

        jLabel4.setText("QuantiaBuffers :");

        tfQuantiaBuffers.setBackground(new java.awt.Color(0, 0, 0));
        tfQuantiaBuffers.setText("10");

        jLabel5.setText("TamanhoBuffers :");

        tfTamanhoBuffers.setText("1024");
        tfTamanhoBuffers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfTamanhoBuffersActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pConfigLayout = new javax.swing.GroupLayout(pConfig);
        pConfig.setLayout(pConfigLayout);
        pConfigLayout.setHorizontalGroup(
            pConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConfigLayout.createSequentialGroup()
                .addComponent(tbTransporte, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 157, Short.MAX_VALUE))
            .addGroup(pConfigLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pConfigLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfQuantiaBuffers))
                    .addGroup(pConfigLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfPorta))
                    .addGroup(pConfigLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tfTamanhoBuffers)))
                .addContainerGap())
        );
        pConfigLayout.setVerticalGroup(
            pConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConfigLayout.createSequentialGroup()
                .addComponent(tbTransporte)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfPorta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfQuantiaBuffers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(tfTamanhoBuffers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbInfo.setText("NULL");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btSelecionarArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tbLiberarArquivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btSelecionarArquivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tbLiberarArquivo)
                    .addComponent(lbInfo))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void limparGui(){
        lbArquivoNome.setText("NULL");
        lbTamanhoArquivo.setText("NULL");
    }
    
    private void btSelecionarArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelecionarArquivoActionPerformed
        // TODO add your handling code here:
        this.setVisible(true);
        if (fcBuffer==null)
        {
            fcBuffer = new JFileChooser();
        }
        
        int returnVal = fcBuffer.showOpenDialog(this);
        
        try{
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                fileBuffer = fcBuffer.getSelectedFile();
                long tamanho = fileBuffer.length();
                String texto = "";
                double tamanhoKb = (tamanho+0.0)/1024.0;
                if (tamanhoKb >= 1.0){
                    double tamanhoMb = tamanhoKb/1024.0;
                    if (tamanhoMb >= 1.0){
                        double tamanhoGb = tamanhoMb/1024.0;
                        if (tamanhoGb >= 1.0){
                            texto = String.format("%.2f GB",tamanhoGb);
                        }else{
                            texto = String.format("%.2f MB",tamanhoMb);
                        }
                    }else{
                        texto = String.format("%.2f KB",tamanhoKb);
                    }
                }else{
                    texto = Long.toString(tamanho)+" B";
                }
                lbArquivoNome.setText(fileBuffer.getName());
                lbTamanhoArquivo.setText(texto);
            }
            else
            {
                
            }
        }
        catch( Exception e)
        {
            e.printStackTrace();
        }
        System.out.println("end");
    }//GEN-LAST:event_btSelecionarArquivoActionPerformed

    private void tfPortaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPortaActionPerformed
        // TODO add your handling code here:
        if (portaLock){
            return;
        }
        try{
            portaLock = true;
            int novaPorta = Integer.parseInt(tfPorta.getText());
            portaValor = novaPorta;
        }catch(Exception e){
            tfPorta.setText(Integer.toString(portaValor));
        }finally{
            portaLock = false;
        }
    }//GEN-LAST:event_tfPortaActionPerformed

    private void tfPortaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPortaKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_tfPortaKeyPressed

    private void tfPortaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfPortaKeyReleased
        // TODO add your handling code here:
        if (portaLock){
            return;
        }
        try{
            portaLock = true;
            if (tfPorta.getText().equals("")){
                tfPorta.setText("0");
            }
            int novaPorta = Integer.parseInt(tfPorta.getText());
            portaValor = novaPorta;
        }catch(Exception e){
            tfPorta.setText(Integer.toString(portaValor));
        }finally{
            portaLock = false;
        }
    }//GEN-LAST:event_tfPortaKeyReleased

    private void enviarArquivo(int quantiaBuffers,int tamanhoBuffers){
        System.out.println("chamando enviar arquivo");
        try{
            if (usarTCP){
                t1 = new Thread(){
                     @Override
                     public void run(){
                         ConexaoTCP conex = new ConexaoTCP();
                         conex.ouvirPedidos(portaValor, fileBuffer,tamanhoBuffers);
                     }
                 };
            }else{
                t1 = new Thread(){
                     @Override
                     public void run(){
                         ConexaoUDP conex = new ConexaoUDP();
                         conex.ouvirPedidos(fileBuffer,quantiaBuffers,tamanhoBuffers);
                     }
                 };
            }
            t1.start();
        }catch(Exception e){
            e.printStackTrace();
            lbInfo.setText("erro thread");
        }finally{
            tbLiberarArquivo.setSelected(false);
        }
    }
    
    private void tbLiberarArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbLiberarArquivoActionPerformed
        // TODO add your handling code here:
        boolean erro = false;
        boolean decisaoBuffer = decisao;
        try{
            decisao = !(decisao);
            int quantiaBuffers = Integer.parseInt(tfQuantiaBuffers.getText());
            int tamanhoBuffers = Integer.parseInt(tfTamanhoBuffers.getText());
            if (quantiaBuffers<=0){
                tfQuantiaBuffers.setText("10");
                lbInfo.setText("QuantiaBuffers deve ser maior que 0");
                erro = true;
                return;
            }
            if (tamanhoBuffers<100){
                tfTamanhoBuffers.setText("100");
                lbInfo.setText("TamanhoBuffers deve ser maior que 99");
                erro = true;
                return;
            }
            if (!(fileBuffer==null)){
                tfPorta.setEditable(!decisao);
                btSelecionarArquivo.setEnabled(!decisao);
                tbTransporte.setEnabled(!decisao);
                tfQuantiaBuffers.setEditable(!decisao);
                tfTamanhoBuffers.setEditable(!decisao);
                if (decisao){
                    if ((t1==null) || !(t1.isAlive())){
                    lbInfo.setText("Enviando arquivo!");
                        enviarArquivo(quantiaBuffers,tamanhoBuffers);
                    }else{
                        lbInfo.setText("Thread ativa?");
                        erro = true;
                    }
                }else{
                    lbInfo.setText("Envio cancelado.");
                    t1.interrupt();
                }
            }else{
                lbInfo.setText("Erro!, arquivo nulo!");
                erro  = true;
            }
        }catch(NumberFormatException e){
            lbInfo.setText("Erro! conversao em inteiro falhou!");
        }finally{
            if (erro){
                //System.out.println("erro!");
                decisao = decisaoBuffer;
            }
            tbLiberarArquivo.setSelected(false);
        }
    }//GEN-LAST:event_tbLiberarArquivoActionPerformed

    private void tbTransporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbTransporteActionPerformed
        // TODO add your handling code here:
        usarTCP = !(tbTransporte.isSelected());
        tfQuantiaBuffers.setEditable(!usarTCP);
        //tfTamanhoBuffers.setEditable(!usarTCP);
        if (usarTCP){
            tbTransporte.setText("TCP");
            tfPorta.setEditable(true);
            tfPorta.setBackground(Color.WHITE);
            tfQuantiaBuffers.setBackground(Color.BLACK);
            //tfTamanhoBuffers.setBackground(Color.BLACK);
        }else{
            tbTransporte.setText("UDP");
            tfPorta.setEditable(false);
            tfPorta.setBackground(Color.BLACK);
            tfQuantiaBuffers.setBackground(Color.WHITE);
            //tfTamanhoBuffers.setBackground(Color.WHITE);
        }
    }//GEN-LAST:event_tbTransporteActionPerformed

    private void tfTamanhoBuffersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfTamanhoBuffersActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfTamanhoBuffersActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(servidorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(servidorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(servidorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(servidorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new servidorFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btSelecionarArquivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lbArquivoNome;
    private javax.swing.JLabel lbInfo;
    private javax.swing.JLabel lbTamanhoArquivo;
    private javax.swing.JPanel pConfig;
    private javax.swing.JToggleButton tbLiberarArquivo;
    private javax.swing.JToggleButton tbTransporte;
    private javax.swing.JTextField tfPorta;
    private javax.swing.JTextField tfQuantiaBuffers;
    private javax.swing.JTextField tfTamanhoBuffers;
    // End of variables declaration//GEN-END:variables
}
