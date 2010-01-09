/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;
import RegistrationForm.RegistrationForm;
import SendFileDialog.SendFileDialogView;
import UserGUIControls.uMessageBox;
import java.net.*;
import java.util.Set;
import org.eclipse.swt.SWT;
import qmessenger.ScreenView;
import java.io.File;
import java.util.Vector;
/**
 *
 * @author Серёжа
 */
public class User extends Thread {
    //Socket SocketIn;
    //Socket SocketOut;

    FormatedMessages message;
    public String structTreeXml;
    private ScreenView screenView;
    SendFileDialogView sendFiles = null;
    ServerSocket inp;
    public User()
    {
        //sendFiles = new SendFileDialogView("Send");
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
            inp = new ServerSocket(Global.IncomingPort, 0);
            SocketIn = inp.accept();
            message = new FormatedMessages(SocketIn, SocketOut, this);
            message.Initialize();
            this.start();
            this.RequestStructureTree();
            this.screenView = new ScreenView();
            if(!this.AuthenticateUser()) {
                int RetCode = new RegistrationForm(this.structTreeXml).run();
                if(RetCode == SWT.CLOSE ) this.Disconnect();
                if(RetCode == 0) {
                    this.getScreenView().setStatusText("Connected");
                    this.getScreenView().run();

                }
            }
            else {
                this.getScreenView().setStatusText("Connected");
                this.getScreenView().run();

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
        while(true)
        {
            try {

                message.ListenForMessages();

            }catch(Exception e)
            {
                this.getScreenView().setStatusText("Connection Lost...");
                Log.WriteException(e);
                try {
                    message.CloseConnection();
                    inp.close();
                }catch(Exception ee){}
               // this.getScreenView().Close();
            }
            while(true)
            {
                try {
                    sleep(Global.ReconnectInterval);
                    this.getScreenView().setStatusText("Reconecting...");
                    Socket SocketOut = new Socket(Global.ServAddr, Global.ServerPort);
                    inp = new ServerSocket(Global.IncomingPort, 0);
                    Socket SocketIn = inp.accept();
                    message.ReConnect(SocketIn, SocketOut);
                    this.AuthenticateUser();
                    this.getScreenView().setStatusText("Connected");
                    break;
                }catch(Exception ee)
                {
                    try {
                       inp.close();
                    }catch(Exception eee){}
                    Log.WriteException(ee);
                    this.getScreenView().setStatusText("Reconect failed...");
                }
            }
        }
    }
    public void ReConnect()
    {
        
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
    public void getOfflineMessages() throws Exception
    {
        message.getOfflineMessages();
    }
    public void SendFiles(String [] fileList, Set <Integer> ids) throws Exception
    {
        /*
        File [] files = new File[fileList.length];
        for(int i = 0; i < fileList.length; i++)
        {
            files[i] = new File(fileList[i]);
        }
        message.SendFiles(files, ids);
         *
         */
        if(sendFiles == null || !sendFiles.isAlive())  {
            sendFiles = new SendFileDialogView("Send");
            sendFiles.SendFiles(fileList, ids);
        }else {
            sendFiles.AddFileToQuene(fileList, ids);

        }
        
    }

    /**
     * @return the screenView
     */
    public ScreenView getScreenView() {
        return screenView;
    }

    /**
     * @param screenView the screenView to set
     */

}
