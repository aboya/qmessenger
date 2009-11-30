/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;
import java.net.*;

/**
 *
 * @author Серёжа
 */
public class User extends Thread {
    //Socket SocketIn;
    //Socket SocketOut;
    Messages message;
    public User()
    {
        
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
    public void Connect()
    {
        //Socket s = new Socket(null, port)
        try {
            Socket SocketOut = new Socket(Global.ServAddr, Global.ServerPort);
            ServerSocket inp = new ServerSocket(Global.IncomingPort, 0);
            Socket SocketIn = inp.accept();
            message = new Messages(SocketIn, SocketOut);
            this.start();
            this.Chat();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    @Override
    public void run()
    {
        try {
            while(true)
            {
                String s = message.ReceiveMessage();
                System.out.println(s);
            }

        }catch(Exception e)
        {
            Log.WriteException(e);
        }
     
    }

}
