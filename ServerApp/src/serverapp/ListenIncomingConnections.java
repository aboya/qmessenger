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
       WaitForConnections ConnectionsWaiter = new WaitForConnections();
       WaitForReceiveFiles ReceiveFileWaiter = new WaitForReceiveFiles();
       WaitForSendFiles SendFileWaiter = new WaitForSendFiles();
       ConnectionsWaiter.start();
       ReceiveFileWaiter.start();
       SendFileWaiter.start();
    }

    public class WaitForConnections extends  Thread {
        public class ConnectToUser extends  Thread {
            Socket inputSocket = null;
            Socket outputSocket = null;
            public ConnectToUser(Socket _inputSocket ,Socket _outputSocket) {
                this.inputSocket = _inputSocket;
                this.outputSocket = _outputSocket;
            }

            @Override
            public void run() {
               int attempt = 5;
               boolean isConnect = false;
               while(attempt > 0 && !isConnect)
               {
                   try {
                       sleep(1000);
                       outputSocket = new Socket(inputSocket.getInetAddress(), Global.ClientPort);
                       Log.Write("Send to client done");
                       queue.PushUser(new User(inputSocket, outputSocket, queue));
                       Log.Write("host:" + inputSocket.getInetAddress() + " connected");
                       isConnect = true;
                    }catch(Exception ee)
                    {
                        Log.Write("attemt failed");
                        Log.WriteException(ee);
                        attempt--;
                        try{
                          if(outputSocket != null) outputSocket.close();
                        }catch(Exception eee){}
                    }
               }
            }
        }

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
                   Log.Write("Socket accept" + inputSocket.getInetAddress());
                   new ConnectToUser(inputSocket, outputSocket).start();
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
                   Log.Write("Receive file begin:" + acceptSocket.getInetAddress().toString());
                   receiveFile = new ReceiveFile(acceptSocket);
                   listenSocket.close();
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
    public class WaitForSendFiles extends Thread
    {

        @Override
        public void run() {

            while(true)
            {
                ServerSocket listenSocket = null;
                Socket acceptSocket = null;
                SendFile sendFile = null;
                try {

                   listenSocket = new ServerSocket(Global.OutcomingFilePort);
                   acceptSocket = listenSocket.accept();
                   sendFile = new SendFile(acceptSocket);
                   listenSocket.close();
                   sendFile.start();
                }catch(Exception e)
                {
                    Log.WriteException(e);
                }
                try {
                   if(listenSocket != null) listenSocket.close();
                }catch(Exception ee) {}
            }

        }

    }

}
