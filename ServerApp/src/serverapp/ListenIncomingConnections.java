/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import javax.net.ssl.SSLSocket;
/**
 *
 * @author Серёжа
 */
public class ListenIncomingConnections {
    
    ConnectionQueue queue = null;
    public ListenIncomingConnections()
    {
        queue = new ConnectionQueue();

    }
    public void Listen()
    {
       WaitForConnections a = new WaitForConnections();
       WaitForReceiveFiles b = new WaitForReceiveFiles();
       a.start();
       b.start();
    }

    public class WaitForConnections extends  Thread {

        @Override
        public void run()
        {
           ServerSocket s = null;
           try {
             s = new ServerSocket(Global.IncomingPort,0);
           }catch(Exception e) {
               Log.WriteException(e);
               return;
           }
           while(true)
           {
              try {

                   
                   Socket inputSocket,outputSocket = null;
                   inputSocket = s.accept();
                   outputSocket = new Socket(inputSocket.getInetAddress(), Global.ClientPort);
                   queue.PushUser(new User(inputSocket, outputSocket, queue));
                   Log.Write("host:" + inputSocket.getInetAddress() + " connected");
               }catch(Exception e)
               {
                  Log.WriteException(e);
               }
           }
        }
    }

    public class WaitForReceiveFiles extends Thread
    {
        

        @Override
        public void run() {
            
            while(true)
            {
                ServerSocket listenSocket = null;
                Socket acceptSocket = null;
                ReceiveFile receiveFile = null;
                try {

                   listenSocket = new ServerSocket(Global.IncomingFilePort);
                   acceptSocket = listenSocket.accept();
                   receiveFile = new ReceiveFile(acceptSocket);
                   receiveFile.start();

                }catch(Exception e)
                {
                    Log.WriteException(e);
                }
                try {
                   if(listenSocket != null ) listenSocket.close();
                }catch(Exception ee) {}
            }
            
        }

    }

}
