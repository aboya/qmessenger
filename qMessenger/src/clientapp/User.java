/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;
import Comunication.UserMessageHistory;
import ReceiveFileDialog.ReceiveFileDialogView;
import RegistrationForm.RegistrationData;
import RegistrationForm.RegistrationForm;
import SendFileDialog.SendFileDialogView;
import java.net.*;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
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
    public String userStructTreeXml;
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

    }

    public void CreateMainWindow() 
    {
        try {
           this.RequestUserStructureTree();
           
        }catch(Exception e)
        {
            Log.WriteException(e, "RequestStructureTree is failed to get");
        }
        ScreenView.launch(ScreenView.class, null);
        this.screenView = null;
        this.start();

    }
    public void Connect() throws Exception
    {

        Socket SocketOut,SocketIn;
        try {
            inp = new ServerSocket(Global.IncomingPort, 0);
            SocketOut = new Socket(Global.ServAddr, Global.ServerPort);

        }catch(Exception e)
        {
            Utils.SetLookAndFeel();
            Log.WriteException(e);
            JOptionPane.showMessageDialog(null,"Невозможно подключится к серверу", "Ошибка", JOptionPane.ERROR_MESSAGE);
            if(timer != null) timer.cancel();
            return;
        }
        try {
            
            SocketIn = inp.accept();
            message = new FormatedMessages(SocketIn, SocketOut, this);
            message.Initialize();
            if(!this.AuthenticateUser()) {
                RequestStructureTree();
                RegistrationForm.launch(RegistrationForm.class, null);
            }
            else {
                CreateMainWindow();
            }
        }catch(Exception e)
        {
            Log.WriteException(e);
            JOptionPane.showMessageDialog(null,"Невозможно подключится к серверу", "Ошибка", JOptionPane.ERROR_MESSAGE);
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
                    Global.getUser().getOfflineMessages();
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
        InetAddress thisIp = InetAddress.getLocalHost();
        String ip = thisIp.toString();
        ip = ip.substring(ip.indexOf("/") + 1);
        return  message.AuthenticateUser(ip);

    }
    public void RequestStructureTree() throws Exception
    {
        structTreeXml = null;
        structTreeXml = message.RequestStructureTree();
    }
    public void RequestUserStructureTree() throws Exception
    {
        userStructTreeXml = null;
        userStructTreeXml = message.RequestUserStructureTree();

    }
    public void UpdateUserStructureTree()
    {
        try {
            RequestUserStructureTree();
            getScreenView().UpdateUserTree(this.userStructTreeXml);
        }catch(Exception ee)
        {
            Log.WriteException(ee, "update user tree failed");
        }
    }
    public void Disconnect()
    {
        if(timer != null) timer.cancel();
        message.CloseConnection();
        try {
             this.interrupt();
        }catch(Exception ee) {  }
    }
    public boolean RegisterUser(RegistrationData rData) throws Exception
    {
        return message.RegisterUser(rData);
    }
    public void SendTextMessage(String txtMessage, Set <Integer> ids) throws Exception
    {
        message.SendTextMessage(txtMessage, ids);
        for(int id : ids)
        {
            getScreenView().AddSendedMessageToScreen(getScreenView().getTreeUserNameById(id), txtMessage);
        }
    }
    public void getOfflineMessages() throws Exception
    {
        message.getOfflineMessages();
    }
    public void SendFiles(String [] fileList, Set <Integer> ids) throws Exception
    {
       sendFiles = new SendFileDialogView();
       sendFiles.SendFiles(fileList, ids);
    }
    public SendFileDialogView getSendFileDialogView()
    {
        return sendFiles;
    }

    public ScreenView getScreenView() {
        if(screenView == null)
        {
            screenView = ScreenView.getInstance(ScreenView.class);
        }
        return screenView;
    }
    public ReceiveFileDialogView getReceiveFileDialogView()
    {
        return receiveFiles;
    }
    public UserMessageHistory getHistory()
    {
        try {
             return message.getMessageHistory();
        }catch(Exception e)
        {
           Log.WriteException(e);
           return new UserMessageHistory();
        }
    }
    public void CheckReceiveFiles() throws Exception
    {
        String input =  message.getFilesMetadata();
        if(input.length() == 0) return;
        String [] metadata = input.split(FormatCharacters.marker + "");
        String fnames = metadata[0];
        final long totalSize = Long.valueOf(metadata[1]);
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
     
        String mes = String.format("Вы хотите принять %d файла(ов) общим размером %s",count, Utils.getAdekvatSize(totalSize));
        if(JOptionPane.showConfirmDialog(getScreenView().getMainFrame(), mes, "Вопрос", JOptionPane.OK_CANCEL_OPTION) ==
                JOptionPane.OK_OPTION)

         {
             receiveFiles = new ReceiveFileDialogView("Receive");
             receiveFiles.ReceiveFiles(fileIds, fileNames);
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