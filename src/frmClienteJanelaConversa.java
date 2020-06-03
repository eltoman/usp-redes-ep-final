/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * frmClienteHome.java
 *
 * Created on 14/10/2015, 11:26:58
 */
package ep1_redes;

import java.net.*;

/**
 *
 * @author Elton
 */
public class frmClienteJanelaConversa extends javax.swing.JFrame {

    public static String strMyUserName = "Eu";
    public static String strUserNameContato;
    public static String strIPContato;
    public static String strStatusContato;
    public static Integer intPortaContato;
    public static ClienteConexao clienteConexao;
    public static Cliente clienteContato;
    public static String strMensagemEnvio = "";
    boolean enviarMensagem = false;
    public static Socket c_socket;

    public frmClienteJanelaConversa(String s) {
    }

    /** Creates new form frmClienteHome */
    public frmClienteJanelaConversa() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        strMensagensConversa = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        strNovaMensagem = new javax.swing.JTextArea();
        btnEnviarMensagem = new javax.swing.JButton();
        lblConversaCom = new javax.swing.JLabel();
        lblNomeContatoConversa = new javax.swing.JLabel();
        lblStatusContato = new javax.swing.JLabel();
        lblLegendaIP = new javax.swing.JLabel();
        lblIpContato = new javax.swing.JLabel();
        lblLegendaPortaContato = new javax.swing.JLabel();
        lblPortaContato = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        strMensagensConversa.setColumns(20);
        strMensagensConversa.setRows(5);
        jScrollPane1.setViewportView(strMensagensConversa);

        strNovaMensagem.setColumns(20);
        strNovaMensagem.setRows(5);
        jScrollPane2.setViewportView(strNovaMensagem);

        btnEnviarMensagem.setText("Enviar");
        btnEnviarMensagem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarMensagemActionPerformed(evt);
            }
        });

        lblConversaCom.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblConversaCom.setText("Conversa com");

        lblNomeContatoConversa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblNomeContatoConversa.setText("Nome do contato conversa");

        lblStatusContato.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblStatusContato.setText("Status Contato");

        lblLegendaIP.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLegendaIP.setText("IP:");

        lblIpContato.setText("ip");

        lblLegendaPortaContato.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblLegendaPortaContato.setText("Porta:");

        lblPortaContato.setText("porta");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(23, 23, 23).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(lblLegendaIP).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(lblIpContato).addGap(204, 204, 204).addComponent(lblLegendaPortaContato).addGap(18, 18, 18).addComponent(lblPortaContato)).addGroup(layout.createSequentialGroup().addComponent(lblConversaCom).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(lblNomeContatoConversa, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(66, 66, 66).addComponent(lblStatusContato)).addGroup(layout.createSequentialGroup().addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(btnEnviarMensagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 649, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(22, 22, 22)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblConversaCom).addComponent(lblNomeContatoConversa).addComponent(lblStatusContato, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblLegendaIP).addComponent(lblIpContato).addComponent(lblLegendaPortaContato).addComponent(lblPortaContato)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(27, 27, 27).addComponent(btnEnviarMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(16, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        this.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
        if (strUserNameContato.length() > 0) {
            lblNomeContatoConversa.setText(strUserNameContato);
        }
        if (strStatusContato.length() > 0) {
            lblStatusContato.setText(strStatusContato);
        }
        if (strIPContato.length() > 0) {
            lblIpContato.setText(strIPContato);
        }
        if (intPortaContato > 0) {
            lblPortaContato.setText(intPortaContato.toString());
        }

        clienteConexao.ClienteConexaoSetarJanela(this);

        Runnable f = new Runnable() {

            public void run() {
                System.out.println("clienteConexao.ComunicacaoP2P(): " + strUserNameContato);
                System.out.println(clienteContato == null);
                clienteContato.ip = strIPContato;
                clienteContato.porta = intPortaContato;
                clienteConexao.ComunicacaoP2P(strMyUserName, clienteContato);
            }
        };
        new Thread(f).start();
    }

    private void btnEnviarMensagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarMensagemActionPerformed
        strMensagemEnvio = strNovaMensagem.getText();
        clienteConexao.enviaMensagemSocket(c_socket, strMensagemEnvio);
        strMensagensConversa.setText(strMensagensConversa.getText() + "Voce disse:\n" + strNovaMensagem.getText() + "\n");
        strNovaMensagem.setText("");
    }

    public void registraMensagemRecebida(String novaMensagem) {
        strMensagensConversa.setText(strMensagensConversa.getText() + strUserNameContato + ": \n" + novaMensagem + "\n");
    }

    public frmClienteJanelaConversa getFormJanela() {
        return this;
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviarMensagem;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblConversaCom;
    private javax.swing.JLabel lblIpContato;
    private javax.swing.JLabel lblLegendaIP;
    private javax.swing.JLabel lblLegendaPortaContato;
    private javax.swing.JLabel lblNomeContatoConversa;
    private javax.swing.JLabel lblPortaContato;
    private javax.swing.JLabel lblStatusContato;
    private javax.swing.JTextArea strMensagensConversa;
    private javax.swing.JTextArea strNovaMensagem;
    // End of variables declaration//GEN-END:variables
}
