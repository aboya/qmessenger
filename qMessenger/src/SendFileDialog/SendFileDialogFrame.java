/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SendFileDialogFrame.java
 *
 * Created on 22.02.2010, 20:55:44
 */

package SendFileDialog;

import MainWindow.DisableEditingTableModel;
import javax.swing.JTable;

/**
 *
 * @author Администратор
 */
public class SendFileDialogFrame extends javax.swing.JFrame {

    /** Creates new form SendFileDialogFrame */
    SendFileDialogView parent = null;
    public SendFileDialogFrame(SendFileDialogView _parent) {
        initComponents();
        parent = _parent;
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
        jtProcessTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                closeWindow(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtProcessTable.setModel(new DisableEditingTableModel(new Object[]{"Файл","%"}, 0));
        jtProcessTable.setName("jtProcessTable"); // NOI18N
        jScrollPane1.setViewportView(jtProcessTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeWindow(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeWindow
        parent.Close();
    }//GEN-LAST:event_closeWindow

    /**
    * @param args the command line arguments
    */

    public JTable getTable()
    {
        return jtProcessTable;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtProcessTable;
    // End of variables declaration//GEN-END:variables

}
