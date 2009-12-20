/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package UserGUIControls;


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Администратор
 */
public class uMessageBox {
    Shell shell;
    Display display;
    MessageBox messageBox;

    public uMessageBox(String message, int type) {
        shell = new Shell();
        messageBox = new MessageBox(shell, type);
        messageBox.setMessage(message);        
    }
    public int open()
    {
        return messageBox.open();
    }


}
