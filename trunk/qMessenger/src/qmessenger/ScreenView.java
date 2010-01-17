/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

/**
 *
 * @author Администратор
 */
import SendFileDialog.SendFileDialogView;
import clientapp.Global;
import clientapp.Log;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;


public class ScreenView {
    UserControls userControls;
    Shell shell;
    Display display;
    public ScreenView() {
        display = Global.getDisplay();
        shell = new Shell(display);
        shell.setText("qMessenger");
        Listener resizeListner = new Listener() {
               public void handleEvent(Event e)  {
                   Rectangle rect = shell.getBounds();
                   rect.width = Math.max(rect.width, 300);
                   rect.height = Math.max(rect.height, 500);
                   shell.setBounds(rect);
                   userControls.Resize(rect);
                }
         };
        shell.addListener(SWT.Resize, resizeListner);
        shell.addListener(SWT.Move, resizeListner);
        userControls = new UserControls(shell, display);
        shell.setSize(500, 400);
        shell.open();
        shell.setActive();
    }
     public void run() 
     {
        try
        {
            Global.getUser().getOfflineMessages();

        }catch(Exception e)
        {
            Log.WriteException(e);
        }
         while (!shell.isDisposed()) {
           if (!display.readAndDispatch()) {
             display.sleep();
           }
         }
         display.dispose();
         Global.getUser().Disconnect();
         //display.addListener(SWT.CLOSE, resizeListner);
         
   }
   public void AddMessageToScreen(String message)
   {
       
      userControls.AddMessageToScreen(message);
   }
   public void Close()
   {
        display.syncExec(
            new Runnable() {
                public void run(){
                     userControls.Close();
                     display.dispose();
                }
            }
        );
      
   }
   public void setStatusText(final String txt)
   {
       display.syncExec(
            new Runnable() {
                public void run(){
                    if(shell.isDisposed()) return;
                    userControls.SetStatusText(txt);
                }
            }
        );
   }

}
