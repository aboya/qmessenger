/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;
import Comunication.UserMessageHistory;
import java.net.Socket;
/**
 *
 * @author Серёжа
 */
public class User extends Thread{
    UserInfo userInfo = null;
    String ip;

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
    public void SendMessageTo(String message, User srcUser)throws Exception
    {
        messages.SendTextMessage(srcUser.getUserInfo().toString() + ":" + message);
    }
    public void DisconnectUser() 
    {
        messages.CloseConnection();
        try {
          this.interrupt();
        }catch(Exception e){}

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
    public Boolean RegisterUser(RegistrationData rData) throws Exception
    {
        boolean res;
        res = dbFunc.RegisterUser(connection, ip, rData);
        if(res) {
            this.getUserTreeName();
            getQueue().UpdateAllUserTree(this);
        }
        return res;
    }
    public String getTreeName()
    {
        if(userInfo == null) getUserTreeName();
        return userInfo.treeName;
    }
    public int getTreeID()
    {
        if(userInfo == null) getUserTreeName();
        return userInfo.treeId;
    }
    public int getUserID()
    {
        if(userInfo == null) getUserTreeName();
        return userInfo.userId;
    }
    public UserInfo getUserInfo()
    {
        if(userInfo == null) getUserTreeName();
        return userInfo;
    }
    private void getUserTreeName() 
    {
        try{
            userInfo = dbFunc.getUserInfo(connection, ip);
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    public UserMessageHistory getMessageHistory(boolean IsGetFullTreePath)
    {
        return dbFunc.GetMessageHistory(connection, getUserID(), IsGetFullTreePath);
    }

    public void updateUserTree()
    {
        try {
            messages.UpdateUserTree();
        }catch(Exception e)
        {
            Log.Write("failed to update user tree");
            Log.WriteException(e);
        }
        
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
