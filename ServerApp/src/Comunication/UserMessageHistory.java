/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Comunication;

import java.io.Serializable;
import java.sql.Date;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Администратор
 */
public class UserMessageHistory extends ComunicationBase  implements Iterable<UserMessageHistory.SingleMessage>{
    public LinkedList<SingleMessage> messages = new LinkedList<SingleMessage>();
    public class SingleMessage implements Serializable
     {
         public boolean isSended; 
         public  Date date;
         public String message;
         public String fromUserName;
         public String parentPath;
     }
    public void Add(Date date, String message, String fromUserName, String parentPath, boolean isSended)
    {
        SingleMessage msg = new SingleMessage();
        msg.isSended = isSended;
        msg.date = date;
        msg.message = message;
        msg.fromUserName = fromUserName;
        msg.parentPath = parentPath;
        messages.add(msg);
    }
    public SingleMessage get(int i)
    {
        return messages.get(i);
    }
    public Iterator<SingleMessage> iterator()
    {
        return messages.iterator();
    }
}
