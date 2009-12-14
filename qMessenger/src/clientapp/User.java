/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;
import RegistrationForm.RegistrationForm;
import java.net.*;
import java.util.Vector;
import qmessenger.ScreenView;

/**
 *
 * @author Серёжа
 */
public class User extends Thread {
    //Socket SocketIn;
    //Socket SocketOut;
    FormatedMessages message;
    public int isAuth;
    public String structTreeXml;
    public User()
    {
        isAuth = -1;
    }
    public void Chat()
    {
        try {
            while(true)
            {
                java.io.BufferedReader r = new java.io.BufferedReader (new java.io.InputStreamReader (System.in));
                String s;
                s = r.readLine();
               // this.SocketOut.getOutputStream().write(s.getBytes(),0,s.length());
                this.message.SendMessage(s);
            }
        }catch(Exception e)
        {
            System.out.println(e);
        }
    }
    public void Connect() throws Exception
    {
        try {

            Socket SocketOut = new Socket(Global.ServAddr, Global.ServerPort);
            ServerSocket inp = new ServerSocket(Global.IncomingPort, 0);
            Socket SocketIn = inp.accept();
            message = new FormatedMessages(SocketIn, SocketOut, this);
            this.start();
            if(!this.AuthenticateUser()) {
                this.RequestStructureTree();
                new RegistrationForm(this.structTreeXml).run();
            }
            else new ScreenView().run();
            
        }catch(Exception e)
        {
            Log.WriteException(e);
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
        }
    }
    public void SendTextMessageTo(Vector<User> userList, String txtMessage) throws Exception
    {
        message.SendTextMessageTo(userList, txtMessage);
        while(true) {
            
        }
    }
    public boolean AuthenticateUser() throws Exception
    {
        InetAddress thisIp =InetAddress.getLocalHost();
        String ip = thisIp.toString();
        ip = ip.substring(ip.indexOf("/")+1);
        this.isAuth = -1;
        message.AuthenticateUser(ip);
        while(this.isAuth < 0) {
            sleep(1000);
        }
        if(this.isAuth == 1) return true;
        else return false;
    }
    public void RequestStructureTree() throws Exception
    {
        structTreeXml = null;
        message.RequestStructureTree();
        while(structTreeXml == null) {
            sleep(1000);
        }
         
    }



 

}
