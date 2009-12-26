/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;
import RegistrationForm.RegistrationForm;
import UserGUIControls.uMessageBox;
import java.net.*;
import java.util.Set;
import org.eclipse.swt.SWT;
import qmessenger.ScreenView;

/**
 *
 * @author Серёжа
 */
public class User extends Thread {
    //Socket SocketIn;
    //Socket SocketOut;
    FormatedMessages message;
    public String structTreeXml;
    ScreenView screenView;
    public User()
    {

    }
    public void Connect() throws Exception
    {
        Socket SocketOut,SocketIn;
        
        try {
            
            SocketOut = new Socket(Global.ServAddr, Global.ServerPort);

        }catch(Exception e)
        {
            Log.WriteException(e);
            uMessageBox messageBox = new uMessageBox("Cannot Connect to server",  SWT.ICON_ERROR);
            messageBox.open();
            return;
        }
        try {
            ServerSocket inp = new ServerSocket(Global.IncomingPort, 0);
            SocketIn = inp.accept();
            message = new FormatedMessages(SocketIn, SocketOut, this);
            message.Initialize();
            this.start();
            this.screenView = new ScreenView();
            this.RequestStructureTree();
            if(!this.AuthenticateUser()) {
                int RetCode = new RegistrationForm(this.structTreeXml).run();
                if(RetCode == SWT.CLOSE ) this.Disconnect();
                if(RetCode == 0) this.screenView.run();
            }
            else {
                this.screenView.run();
            }
            
        }catch(Exception e)
        {
            Log.WriteException(e);
            uMessageBox messageBox = new uMessageBox("Server drop connection",  SWT.ICON_ERROR);
            messageBox.open();
        }
    }
    @Override
    public void run()
    {
        try {
            message.ListenForMessages();
        }catch(Exception e)
        {
            Log.WriteException(e);
            this.Disconnect();
        }
    }

    public boolean AuthenticateUser() throws Exception
    {
        InetAddress thisIp =InetAddress.getLocalHost();
        String ip = thisIp.toString();
        ip = ip.substring(ip.indexOf("/")+1);
        return  message.AuthenticateUser(ip);

    }
    public void RequestStructureTree() throws Exception
    {
        structTreeXml = null;
        structTreeXml = message.RequestStructureTree();
    }
    public void Disconnect()
    {
        try {
            message.CloseConnection();
            this.screenView.Close();
            this.interrupt();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    public boolean RegisterUser(int structureId) throws Exception
    {
        return message.RegisterUser(structureId);
    }
    public void SendTextMessage(String txtMessage, Set <Integer> ids) throws Exception
    {
        message.SendTextMessage(txtMessage, ids);
    }

}
