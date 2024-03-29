/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;
import Comunication.TextMessage;
import java.sql.ResultSet;
import java.util.*;
/**
 *
 * @author Серёжа
 */
public class ConnectionQueue {
    LinkedList <User> queue;
    dbConnection connection;
    Map<Integer, Pair> userPaths = new TreeMap<Integer,Pair>();

    public ConnectionQueue() {
        queue = new LinkedList<User>();
        connection = new dbConnection();
    }
    public void PushUser(User user) throws Exception
    {
        if(this.CountUsers() + 1 >= Global.max_users)
        {
            //user.SendMessage("Maximum users connected... you can not connected");
            Log.Write("Maximum users connected !, host:" + user.ip + " is disconected");
        }else {
            user.start();
            queue.add(user);
        }
    }
    public User FindUserByIp(String ip) throws Exception
    {
       if(queue.isEmpty()) return null;
       ListIterator <User> it = queue.listIterator();
       User usr;
       do {
           usr = it.next();
           if(usr.ip.equals(ip)) return usr;
       }while(it.hasNext());
       return null;
    }

    public void SendToUser(String userName,String fileName, byte[]mess , int lenght, int totalLength)
    {
        
    }
    public void RemoveUser(User user)
    {
        try {
           if(queue.isEmpty()) return;
           ListIterator <User> it = queue.listIterator();
           User usr;
           do  {
               usr = it.next();
               if(usr.equals(user))
               {
                   it.remove();
                   usr.DisconnectUser();
                   return;
               }
           }while(it.hasNext());
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    public int CountUsers()
    {
        return queue.size();
    }

    public void SendMessageToUser(String txtMessage, User srcUser)
    {
       if(queue.isEmpty()) return;
       ListIterator <User> it = queue.listIterator();
       User usr;
       do {
           try {
                usr = it.next();
                usr.SendMessageTo(txtMessage, srcUser);
           }catch(Exception e)
           {
               Log.WriteException(e);
           }

       }while(it.hasNext());
    }
    public void UpdateAllUserTree(User newRegistredUser)
    {
        for(User u:queue)
        {
            if(!u.equals(newRegistredUser))
                u.updateUserTree();
        }
    }
    public void SendMessageToUser(TextMessage txt, User srcUser)
    {
       if(queue.isEmpty()) return;
       ListIterator <User> it = queue.listIterator();
       //User usr;
       Set <Integer> dstUsers = txt.ids;


       //sending online messages
       for(User usr: queue)
       {
          try {
              if(dstUsers.contains(usr.getUserID()))
              {
                    usr.SendMessageTo(txt.message, srcUser);
                    dstUsers.remove(usr.getUserID());
                    // saving message history
                    // for offline users saving message is automatically // see bellow
                    if(txt.saveOnServer)
                    {
                        dbFunc.SaveMessageOnServer(connection, txt.message, usr.getUserID(), srcUser.getUserID());
                    }
              }
           }catch(Exception e)
           {
               Log.WriteException(e);
           }
       }

       // тем кто остался, посылаем оффлайновые мессаги
       
       try {
          if(!dstUsers.isEmpty())
          {
              Object [] ids = dstUsers.toArray();
              for(int i = 0; i < ids.length; i++)
              {
                  dbFunc.sendOfflineMessageToUser(connection, txt.message, (Integer)ids[i], srcUser.getUserID());
              }
          }
       }catch(Exception e)
       {
           Log.WriteException(e);
       }

       
    }
    public void FillUserPaths()
    {
        List<Integer> allIds = new LinkedList<Integer>();
        try {
            connection.Connect();
            ResultSet rs = connection.ExecuteQuery("select UserID from user");
            while(rs.next())
            {
                allIds.add(rs.getInt("UserID"));
            }
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        connection.Close();

        for(Integer id:allIds)
        {
            userPaths.put(id, dbFunc.getParentPath(connection, id));
        }
    }
    public Pair getUserPath(int userId)
    {
        if(userPaths.isEmpty()) this.FillUserPaths();
        return  userPaths.get(userId);
    }
}
