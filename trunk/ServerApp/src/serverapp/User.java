/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;
import java.net.Socket;
import java.sql.ResultSet;
/**
 *
 * @author Серёжа
 */
public class User extends Thread{

    String ip;
    String TreeName = "";
    int TreeId = 0;
    int Timeout;
    boolean isRun;
    private ConnectionQueue queue;
    FormatedMessages messages;
    dbConnection connection;

    public User(Socket inputSocket, Socket outputSocket, ConnectionQueue Q) {
       // messages = new Messages(inputSocket, outputSocket, this);
        try {
           messages = new FormatedMessages(inputSocket, outputSocket, this);
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
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
            messages.Initialize();
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
        getQueue().RemoveUser(this);
        this.isRun = false;
    }
    //Send message to client
    public void SendMessage(String message, String srcIp) throws Exception
    {
        //messages.SendTextMessage(srcIp + ":" + message);
        messages.SendTextMessage(this.ip +"asd" + ":" + message);
    }
    public void SendMessageTo(String message, User srcUser)throws Exception
    {
        messages.SendTextMessage(srcUser.getTreeName() + ":" + message);
    }
    public void DisconnectUser() throws Exception
    {
        messages.CloseConnection();
        this.interrupt();
    }
    public void AuthenticateUser()
    {
        Boolean Auth = false;
        Integer res;
        try {
            connection.Connect();
            res = (Integer)connection.ExecuteScalar(String.format("select FindUser('%s')", this.ip));
            Auth = res > 0;
            if(Auth) this.getUserTreeName();
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        connection.Close();

        messages.AuthenticateUserPostBack(Auth);
    }
    public Boolean RegisterUser(String structureId) throws Exception
    {
        boolean res;
        res = dbFunc.RegisterUser(connection, ip, structureId);
        if(res) this.getUserTreeName();
        return res;
    }
    public String getTreeName()
    {
        return TreeName;
    }
    public int getTreeID()
    {
        return TreeId;
    }
    private void getUserTreeName() throws Exception
    {
         ResultSet r = dbFunc.getUserTreeName(connection, ip, this.TreeName, this.TreeId);
         this.TreeName = r.getString("TreeName");
         this.TreeId = r.getInt("TreeID");
         r.close();
    }

    /**
     * @return the queue
     */
    public ConnectionQueue getQueue() {
        return queue;
    }

    /**
     * @param queue the queue to set
     */
    public void setQueue(ConnectionQueue queue) {
        this.queue = queue;
    }
}
