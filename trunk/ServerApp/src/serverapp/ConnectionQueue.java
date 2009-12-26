/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;
import java.util.*;
/**
 *
 * @author Серёжа
 */
public class ConnectionQueue {
    LinkedList <User> queue;
    dbConnection connection;

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
    public void SendMessageToUser(String txtMessage, Set <Integer> dstUsers, User srcUser)  
    {
       if(queue.isEmpty()) return;
       ListIterator <User> it = queue.listIterator();
       User usr;
       do {
           try {
                usr = it.next();
                if(dstUsers.contains(usr.getTreeID()))
                {
                    usr.SendMessageTo(txtMessage, srcUser);
                    dstUsers.remove(usr.getTreeID());
                }
           }catch(Exception e)
           {
               Log.WriteException(e);
           }

       }while(it.hasNext());
       try {
          if(!dstUsers.isEmpty())
          {
              Object [] ids = dstUsers.toArray();
              for(int i = 0; i < ids.length; i++)
              {
                  dbFunc.sendOfflineMessageToUser(connection, txtMessage, (Integer)ids[i], srcUser.getTreeID());
              }
          }
       }catch(Exception e)
       {
           Log.WriteException(e);
       }
       
    }
}
