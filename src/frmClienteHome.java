/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * frmClienteHome.java
 *
 * Created on 14/10/2015, 11:36:54
 */
package ep1_redes;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Elton
 */
public class frmClienteHome extends javax.swing.JFrame {

    public static String myUserName;
    public static ClienteConexao clienteConexao;

    /** Creates new form frmClienteHome */
    public frmClienteHome() {
        initComponents();
    }

    public static void iniciarThreadNovasConexoes() {
        try {
            Runnable r = new Runnable() {

                public void run() {
                    try {
                        clienteConexao.novasConexoes();
                    } catch (Exception e) {
                    }
                }
            };
            new Thread(r).start();
        } catch (Exception e) {
        }
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
        tblContatoStatus = new javax.swing.JTable();
        lblLegendaUser = new javax.swing.JLabel();
        strUserName = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblLegendaInicieUmaConversa = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tblContatoStatus.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
//        tblContatoStatus.setModel(new javax.swing.table.DefaultTableModel(
//                new Object[][]{
//                    {"elton912", "Online", "192.168.0.104", new Integer(8082)},
//                    {"Giones", "Offline", "192.168.0.104", new Integer(8083)},
//                    {"elton912", "Offline", "192.168.0.5", new Integer(8081)},
//                    {"Giones", "Online", "192.168.0.5", new Integer(8083)}
//                },
//                new String[]{
//                    "Contatos", "Status", "IP", "Porta"
//                }) {
//
//            Class[] types = new Class[]{
//                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types[columnIndex];
//            }
//        });
        tblContatoStatus.setRowHeight(20);
        tblContatoStatus.setRowMargin(2);
        tblContatoStatus.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblContatoStatusMouseEntered(evt);
            }

            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblContatoStatusMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblContatoStatus);

        lblLegendaUser.setFont(new java.awt.Font("Tahoma", 1, 14));
        lblLegendaUser.setText("User Name:");

        strUserName.setFont(new java.awt.Font("Tahoma", 0, 14));
        strUserName.setText("user name");

        jLabel1.setText("OBS.: Duplo click sobre um contato online para iniciar uma conversa ");

        lblLegendaInicieUmaConversa.setFont(new java.awt.Font("Tahoma", 1, 14));
        lblLegendaInicieUmaConversa.setText("Inicie uma conversa");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(304, Short.MAX_VALUE).addComponent(jLabel1).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(lblLegendaUser).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(strUserName)).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 604, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(18, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGap(250, 250, 250).addComponent(lblLegendaInicieUmaConversa).addContainerGap(253, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(21, 21, 21).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblLegendaUser).addComponent(strUserName)).addGap(42, 42, 42).addComponent(lblLegendaInicieUmaConversa).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jLabel1).addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if (myUserName.length() > 0) {
            strUserName.setText(myUserName);
        }

        preencherTableModel(ClienteConexao.listaContatos);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
            	
            		JFrame frame = (JFrame) e.getSource();
            	try{	
                    clienteConexao.updateAntesFechar();
                    clienteConexao = null;
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            	}catch(NullPointerException nill){
            		frame.setVisible(false);
            		System.exit(0);
            	}
                
               
            }
        });



    }//GEN-LAST:event_formWindowOpened

    private void preencherTableModel(Map<String, Cliente> listaContatos) {
        String[] header = {"USER_NAME", "STATUS", "IP", "PORTA"};
        DefaultTableModel model = new DefaultTableModel(header, 0);

        for (Map.Entry<String, Cliente> entry : listaContatos.entrySet()) {
            Cliente cli = entry.getValue();
            String status = null;
            if (cli.status == true) {
                status = "online";
            } else {
                status = "offline";
            }
            String[] linha = {cli.userName, status, cli.ip, Integer.toString(cli.porta)};
            model.addRow(linha);

            tblContatoStatus.setModel(model);

        }
        clienteConexao.tblContatoStatus = tblContatoStatus;
    }

    // Evento nao utilizado
    private void tblContatoStatusMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContatoStatusMouseEntered
    }//GEN-LAST:event_tblContatoStatusMouseEntered
// Evento nao utilizado

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
    }//GEN-LAST:event_formMousePressed

    /* 
     * Inicia nova janela de conversa apos clique no usuario escolhido
     */
    private void tblContatoStatusMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContatoStatusMousePressed
        int rw = tblContatoStatus.getSelectedRow();
        String aux = String.valueOf(tblContatoStatus.getValueAt(rw, 1));
        if (aux.toLowerCase().equals("offline")) {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    JOptionPane.showMessageDialog(null, "Voce nao pode conversar com quem esta offline!", "YOU SHALL NOT PASS", JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new frmClienteJanelaConversa().setVisible(true);
                    frmClienteJanelaConversa.strMyUserName = myUserName;
                    int rw = tblContatoStatus.getSelectedRow();
                    frmClienteJanelaConversa.strUserNameContato = tblContatoStatus.getValueAt(rw, 0).toString();
                    frmClienteJanelaConversa.strStatusContato = tblContatoStatus.getValueAt(rw, 1).toString();
                    frmClienteJanelaConversa.strIPContato = tblContatoStatus.getValueAt(rw, 2).toString();
                    frmClienteJanelaConversa.intPortaContato = Integer.parseInt(tblContatoStatus.getValueAt(rw, 3).toString());
                    frmClienteJanelaConversa.clienteConexao = clienteConexao;
                    frmClienteJanelaConversa.clienteContato = clienteConexao.listaContatos.get(tblContatoStatus.getValueAt(rw, 0).toString());
                }
            });
        }


    }//GEN-LAST:event_tblContatoStatusMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new frmClienteHome().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblLegendaInicieUmaConversa;
    private javax.swing.JLabel lblLegendaUser;
    private javax.swing.JLabel strUserName;
    public javax.swing.JTable tblContatoStatus;
    // End of variables declaration//GEN-END:variables
}
