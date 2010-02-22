/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;
import ReceiveFileDialog.ReceiveFileDialogView;
import RegistrationForm.RegistrationForm;
import SendFileDialog.SendFileDialogView;
import UserGUIControls.uMessageBox;
import java.net.*;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.swt.SWT;
import qmessenger.ScreenView;
import org.jdesktop.application.SingleFrameApplication;
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
    ReceiveFileDialogView receiveFiles = null;
    ServerSocket inp;
    public TimerTask taskCheckFiles;
    int result;
    Timer timer = null;
    public void SheduleNewTimerForCheckFiles()
    {
        if(timer != null ) {
           // timer.purge();
            timer.cancel(); // this call garbage collector
        }
        timer = new Timer();
        taskCheckFiles = new TimerTask_CheckFiles();
        timer.schedule(taskCheckFiles, 10000);
    }
    public User()
    {
        //sendFiles = new SendFileDialogView("Send");
        


    }
    public void Connect() throws Exception
    {
        Socket SocketOut,SocketIn;
        try {
            inp = new ServerSocket(Global.IncomingPort, 0);
            SocketOut = new Socket(Global.ServAddr, Global.ServerPort);

        }catch(Exception e)
        {
            Log.WriteException(e);
            uMessageBox messageBox = new uMessageBox("Cannot Connect to server",  SWT.ICON_ERROR);
            messageBox.open();
            if(timer != null) timer.cancel();
            return;
        }
        try {
            
            SocketIn = inp.accept();
            message = new FormatedMessages(SocketIn, SocketOut, this);
            message.Initialize();
            
            this.RequestStructureTree();
            if(!this.AuthenticateUser()) {
                int RetCode = new RegistrationForm(this.structTreeXml).run();
                if(RetCode == SWT.CLOSE ) this.Disconnect();
                if(RetCode == 0) {
                    this.screenView = new ScreenView();
                    this.getScreenView().setStatusText("Connected");
                    this.start();
                    this.getScreenView().run();
                }
            }
            else {
                this.screenView = new ScreenView();
                this.getScreenView().setStatusText("Connected");
                this.start();
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
        this.SheduleNewTimerForCheckFiles();
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
                }catch(Exception ee){}
                try{
                   inp.close();
                }catch(Exception ee) {}
                
            }
            // trying reconnect
            while(true)
            {
                try {
                    sleep(Global.ReconnectInterval);
                    this.getScreenView().setStatusText("Reconecting...");
                    inp = new ServerSocket(Global.IncomingPort, 0);
                    Socket SocketOut = new Socket(Global.ServAddr, Global.ServerPort);
                    Socket SocketIn = inp.accept();
                    message.ReConnect(SocketIn, SocketOut);
                    this.AuthenticateUser();
                    this.getScreenView().setStatusText("Connected");
                    this.SheduleNewTimerForCheckFiles();
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
        if(timer != null) timer.cancel();
        try {
            message.CloseConnection();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        try {
             this.interrupt();
        }catch(Exception ee) {  }
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
    public void SendFiles(String path, String [] fileList, Set <Integer> ids) throws Exception
    {
        /*
        if(sendFiles == null || !sendFiles.isAlive())  {
            if (sendFiles != null && !sendFiles.isClosed()) sendFiles.Close();
            sendFiles = new SendFileDialogView("Send");
            sendFiles.SendFiles(path, fileList, ids);
        }else {
            sendFiles.AddFileToQuene(path, fileList, ids);
        }
         * 
         */
        sendFiles = new SendFileDialogView();
        sendFiles.launch(SendFileDialogView.class, null);
        

    }
    public SendFileDialogView getSendFileDialogView()
    {
        return sendFiles;
    }

    public ScreenView getScreenView() {
        return screenView;
    }
    public ReceiveFileDialogView getReceiveFileDialogView()
    {
        return receiveFiles;
    }
    public void CheckReceiveFiles() throws Exception
    {
        String input =  message.getFilesMetadata();
        if(input.length() == 0) return;
        String [] metadata = input.split(FormatCharacters.marker + "");
        String fnames = metadata[0];
        final long totalSize = Integer.valueOf(metadata[1]);
        final int count = Integer.valueOf(metadata[2]);
        if(count == 0) return;
        String []fileNames = new String[count];
        int [] fileIds = new int[count];
        String [] tmp = fnames.split("\\|");
        if(count != tmp.length / 2 || tmp.length % 2 != 0) throw new Exception("Incorect file names & ids");
        for(int i = 0; i < count; i++)
        {
            fileNames[i] = tmp[2 * i];
            fileIds[i] = Integer.valueOf(tmp[2 * i + 1]);
        }
        Global.getDisplay().syncExec(new Runnable() {

            public void run() {
                 int res;
                 uMessageBox msg = new uMessageBox(String.format("вы хотите принять %d файла(ов) с общим размером %d ?",
                         count, totalSize), SWT.OK|SWT.CANCEL);
                 // ниче умней я не придумал
                 result = msg.open();
            }
       });
      if(result == SWT.OK)
      {
          if(receiveFiles == null || !receiveFiles.isAlive())  {
               if (receiveFiles != null && !receiveFiles.isClosed()) receiveFiles.Close();
                   receiveFiles = new ReceiveFileDialogView("Receive");
                   receiveFiles.ReceiveFiles(fileIds, fileNames);
               }else {
                   receiveFiles.AddFileToQuene(fileIds, fileNames);
               }
       }
    }
}
class TimerTask_CheckFiles extends TimerTask
{


    @Override
    public void run() {
       
        try {

          Global.getUser().CheckReceiveFiles();
          Global.getUser().SheduleNewTimerForCheckFiles();
          this.cancel();
        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
    }

}