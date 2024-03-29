/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * qMessengerAbout.java
 *
 * Created on 11.04.2010, 10:12:49
 */

package MainWindow;

import clientapp.Global;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Администратор
 */
public class qMessengerAbout extends javax.swing.JFrame {

    /** Creates new form qMessengerAbout */
    public qMessengerAbout() {
        initComponents();
        lblImage.setIcon(new ImageIcon(Global.applicationIcon));
        this.setIconImage(Global.applicationIcon);
        lblInfo.setText("<html>" +
                "<br>qMessenger 1.0" +
                "<br>Автор: Абоимов Сергей" +
                "<br>2010г" +
                "</html>");
        setLocationRelativeTo(Global.getUser().getScreenView().getMainFrame());
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblImage = new javax.swing.JLabel();
        lblInfo = new javax.swing.JLabel();
        btnOk = new javax.swing.JButton();

        setTitle("О Программе");
        setName("aboutWindow"); // NOI18N
        setResizable(false);

        lblImage.setName("lblImage"); // NOI18N

        lblInfo.setFont(new java.awt.Font("Tahoma", 1, 14));
        lblInfo.setText("qMessenger");
        lblInfo.setName("lblInfo"); // NOI18N

        btnOk.setText("Ok");
        btnOk.setName("btnOk"); // NOI18N
        btnOk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnOkMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                        .addGap(22, 22, 22))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnOk)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblInfo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblImage, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnOk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOkMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOkMousePressed
        this.setVisible(false);
    }//GEN-LAST:event_btnOkMousePressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new qMessengerAbout().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOk;
    private javax.swing.JLabel lblImage;
    private javax.swing.JLabel lblInfo;
    // End of variables declaration//GEN-END:variables

}
