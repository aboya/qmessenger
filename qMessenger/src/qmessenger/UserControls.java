/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;


import clientapp.Global;
import clientapp.Log;
import java.util.Vector;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
    TrayMenu trayMenu;
    Display display;

    Vector<BaseControl> controlsList;

    public UserControls(Composite composite, Display _display) {
        this.display = _display;
        controlsList = new Vector<BaseControl>();
        userList = new UserList(composite, display);
        controlsList.add(userList);
        inputTextField = new InputTextField(composite);
        controlsList.add(inputTextField);
        outputTextField = new OutputTextField(composite);
        controlsList.add(outputTextField);
        buttonSendMessage = new ButtonSendMessage(composite);
        controlsList.add(buttonSendMessage);
        trayMenu = new TrayMenu((Shell)composite, display);
        controlsList.add(trayMenu);


        SelectionListener sListner = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    String message = inputTextField.getTextControl().getText();
                    Global.user.SendTextMessage(message);
                    inputTextField.getTextControl().setText("");
                }catch(Exception e)
                {
                    Log.WriteException(e);
                }
                
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {

            }


        };
        buttonSendMessage.getButton().addSelectionListener(sListner);
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
    public void AddMessageToScreen(final String message)
    {
        // эта хрень нужна для того что бы была возможность менять
        // контролы из другова потока подробнее http://java.sys-con.com/node/37509
        display.syncExec(
            new Runnable() {
                public void run(){
                    outputTextField.getTextControl().append(message + "\n");
                }
            }
        );

    }

    public void Close() {
         trayMenu.getTrayItem().dispose();
    }


}