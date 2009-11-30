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
       WaitForConnections a =   new WaitForConnections();
       a.start();     
    }

    public class WaitForConnections extends  Thread {

        @Override
        public void run()
        {
            try{
                ServerSocket s = new ServerSocket(Global.IncomingPort,0);
                while(true)
                {
                    Socket inputSocket,outputSocket = null;
                    inputSocket = s.accept();
                    outputSocket = new Socket(inputSocket.getInetAddress(), Global.ClientPort);
                    queue.PushUser(new User(inputSocket, outputSocket, queue));
                    Log.Write("host:" + inputSocket.getInetAddress() + " connected");

                }
            }catch(Exception e)
            {
                Log.WriteException(e);
            }
        }
        
    }

}
