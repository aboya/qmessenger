/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

/**
 *
 * @author Администратор
 */
import MainWindow.MessengerMainFrame;
import clientapp.Global;
import clientapp.Log;
import javax.swing.JFrame;
import javax.swing.RepaintManager;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.application.SingleFrameApplication;
public class ScreenView extends SingleFrameApplication {


    MessengerMainFrame messengerMainFrame;
    public ScreenView() {

    }
     @Override protected void startup() {


        show(messengerMainFrame = new MessengerMainFrame(this));
        //fillTree(Global.getUser().structTreeXml);
        messengerMainFrame.fillTree(Global.getUser().userStructTreeXml);
        // comments this line on http://weblogs.java.net/blog/2008/04/13/repaintmanagers-side-effect
        // for correct updating window
        RepaintManager.currentManager(null).setDoubleBufferingEnabled(false);
        RepaintManager.currentManager(null).setDoubleBufferingEnabled(true);
        try
        {
            Global.getUser().getOfflineMessages();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        Global.getUser().SheduleNewTimerForCheckFiles();
     }


   public void AddReceiveFileToScreen(String from, String fileName, String size)
   {
       DefaultTableModel model = (DefaultTableModel)messengerMainFrame.tblIncomingFiles.getModel();
       model.addRow(new Object[]{from, fileName, size});
   }
   public void AddMessageToScreen(String from, String message)
   {
       DefaultTableModel model = (DefaultTableModel) messengerMainFrame.tblIncomingMessages.getModel();
       model.addRow(new Object[]{from,message});
   }
   public void AddSendedFileToScreen(String forName, String fileName, String fileSize)
   {
       DefaultTableModel model = (DefaultTableModel) messengerMainFrame.tblSendedFiles.getModel();
       model.addRow(new Object[]{forName, fileName, fileSize});
   }
   public void AddSendedMessageToScreen(String forName, String message)
   {
       DefaultTableModel model = (DefaultTableModel) messengerMainFrame.tblSendedMessages.getModel();
       model.addRow(new Object[]{forName, message});
   }
   public void Close()
   {
       messengerMainFrame.setVisible(false);
   }
   public void setStatusText(final String txt)
   {

   }
    public JFrame getFrame()
    {
        return messengerMainFrame;
    }
    public void ShowPreferences()
    {
        
    }
    public void ClosePreferences()
    {
        
    }
}
