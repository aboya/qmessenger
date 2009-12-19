/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;


import clientapp.Log;
import java.util.Vector;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Администратор
 */
public class UserControls {
    UserList userList;
    InputTextField inputTextField;
    OutputTextField outputTextField;
    ButtonSendMessage buttonSendMessage;

    Vector<BaseControl> controlsList;

    public UserControls(Composite composite, Display display) {
        controlsList = new Vector<BaseControl>();
        userList = new UserList(composite, display);
        controlsList.add(userList);
        inputTextField = new InputTextField(composite);
        controlsList.add(inputTextField);
        outputTextField = new OutputTextField(composite);
        controlsList.add(outputTextField);
        buttonSendMessage = new ButtonSendMessage(composite);
        controlsList.add(buttonSendMessage);
        TrayMenu trayMenu = new TrayMenu((Shell)composite, display);
        controlsList.add(trayMenu);
    }
    public void Resize(Rectangle rect)  
    {
        int i, n  = controlsList.size();
        for(i = 0; i < n; i++)
        {
            try {
                controlsList.get(i).Resize(rect);
            }
            catch(Exception e) {
                Log.WriteException(e);
            
            }
        }
    }


}
