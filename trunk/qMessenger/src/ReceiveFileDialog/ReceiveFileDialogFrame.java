/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ReceiveFileDialogFrame.java
 *
 * Created on 22.02.2010, 20:53:46
 */

package ReceiveFileDialog;

import MainWindow.DisableEditingTableModel;
import clientapp.Global;
import java.awt.Graphics;
import javax.swing.JTable;

/**
 *
 * @author Администратор
 */
public class ReceiveFileDialogFrame extends javax.swing.JFrame {
    ReceiveFileDialogView parent = null;

    /** Creates new form ReceiveFileDialogFrame */
    public ReceiveFileDialogFrame(ReceiveFileDialogView _parent) {
        initComponents();
        parent = _parent;
        this.setIconImage(Global.applicationIcon);
 
    }
    @Override
    public Graphics getGraphics()
    {
        return super.getGraphics();
    }
    JTable getTable()
    {
        return tblFiles;
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
        tblFiles = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Получение файлов");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                WindowClose(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tblFiles.setModel(new DisableEditingTableModel(new Object[]{"Файл","%"}, 0));
        tblFiles.setName("tblFiles"); // NOI18N
        jScrollPane1.setViewportView(tblFiles);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void WindowClose(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowClose
        parent.Close();
    }//GEN-LAST:event_WindowClose



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFiles;
    // End of variables declaration//GEN-END:variables

}
