/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;
import com.mysql.jdbc.log.Log4JLogger;
import java.net.Socket;
import java.net.SocketException;
import java.util.Vector;
/**
 *
 * @author Серёжа
 */
public class User extends Thread{

    String ip;
    int Timeout;
    int id;
    boolean isRun;
    ConnectionQueue queue;
    FormatedMessages messages;
    dbConnection connection;

    public User(Socket inputSocket, Socket outputSocket, ConnectionQueue Q) {
       // messages = new Messages(inputSocket, outputSocket, this);
        messages = new FormatedMessages(inputSocket, outputSocket, this);
        this.isRun = false;
        ip = inputSocket.getInetAddress().getHostAddress();
        queue = Q;
        connection = new dbConnection();
    }
    // Listen incoming messages and redirect to users
    @Override
    public void run()
    {
        try {
            this.isRun = true;
            String message;
            messages.ListenForMessages();
        }
        catch(Exception e)
        {
            Log.Write("user:" + this.ip + " disconected");
            Log.WriteException(e);
        }

        // ignoring errors
        try {
            this.DisconnectUser();
        }catch(Exception e){

        }
        queue.RemoveUser(this);
        this.isRun = false;
    }
    //Send message to client
    public void SendMessage(String message) throws Exception
    {
         messages.SendMessage(message);
    }
    public void SendTextMessage(Vector<User> userList, String txtMessage) throws Exception
    {
        messages.SendTextMessageTo(userList, txtMessage);
    }

    public void SendFile(String [] userNames, String fileName, byte [] packet, int lenght, int totalLength)
    {
        int i, n = userNames.length;
        for(i = 0; i < n; i++)
            queue.SendToUser(userNames[i],fileName, packet, lenght, totalLength);
    }


    public void DisconnectUser() throws Exception
    {
        messages.CloseConnection();
        this.interrupt();
    }

    public void SendFileTo(User ruser, String FileName, long FileSize)
    {
       // messages.SendFileTo(ruser, FileName, FileSize);
    }
    public void ReceiveFileFrom(User ruser)
    {
       // messages.ReceiveFileFrom(ruser);
    }
    public int getUserID()
    {
        return  id;
    }
    public void AuthenticateUser()
    {
        Boolean Auth = false;
        Integer res;
        try {
            connection.Connect();
            res = (Integer)connection.ExecuteScalar(String.format("select FindUser('%s')", this.ip)
                    );
           
            Auth = res > 0;
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        connection.Close();
        messages.AuthenticateUserPostBack(Auth);
    }
    public Boolean RegisterUser(String structureId)
    {
        return dbFunc.RegisterUser(connection, ip, structureId);
    }
}
