/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;


import UserGUIControls.uMessageBox;
import clientapp.Global;
import clientapp.Log;
import java.io.File;
import java.util.Set;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
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
    final Composite composite;
    StatusBar statusBar;
    SendFileButton sendFileButton;

    Vector<BaseControl> controlsList;

    public UserControls(Composite _composite, Display _display) {
        this.composite = _composite;
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
        statusBar = new StatusBar((Shell)composite);
        controlsList.add(statusBar);
        sendFileButton = new SendFileButton((Shell)composite);
        controlsList.add(sendFileButton);

        SelectionListener sListner = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                try {
                    String message = inputTextField.getTextControl().getText();
                    Set <Integer> ids = userList.getSelectedId();
                    if( ids == null || ids.size() == 0)
                    {
                        uMessageBox msg = new uMessageBox("You must select users", SWT.OK);
                        msg.open();
                        return;
                    }
                    Global.getUser().SendTextMessage(message, ids);
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

        sListner = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                Set <Integer> ids = userList.getSelectedId();
                if( ids == null || ids.size() == 0)
                {
                    uMessageBox msg = new uMessageBox("You must select users", SWT.OK);
                    msg.open();
                    return;
                }
                FileDialog fd = new FileDialog((Shell)composite, SWT.MULTI);
                fd.setText("Open");
                fd.setFilterPath(Global.lastOpenPath);
                String[] filterExt = { "*.*" };
                fd.setFilterExtensions(filterExt);
                
                fd.open();

                String path = fd.getFilterPath() + File.pathSeparator;
                if(fd.getFileNames().length == 0) return;
                try {
                    Global.getUser().SendFiles(path, fd.getFileNames(),ids);
                }catch(Exception ee)
                {
                    Log.WriteException(ee);
                }
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {

            }
        };
        sendFileButton.getButton().addSelectionListener(sListner);


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
    public UserList getUserList()
    {
        return userList;
    }
    public void SetStatusText(final String txt)
    {
       statusBar.SetText(txt);
    }


}
