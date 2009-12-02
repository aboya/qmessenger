/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import java.net.Socket;
import java.util.Vector;

/**
 *
 * @author Администратор
 */
public class FormatedMessages extends Messages{

    public FormatedMessages(Socket in, Socket out, User usr) {
        super(in, out, usr);
    }
    private void Receive() throws Exception
    {
        String metaData = this.ReceiveMessage();
        if(metaData == Global.TextMessege) this.ReceiveMessage();

        return;
    }
    public void ReceiveTextMessage() throws Exception
    {
        String txtMessage = this.ReceiveMessage();
        this.SendMessage(Global.TextMessege);
        this.SendMessage(txtMessage);
    }
    public void SendTextMessageTo(Vector<User> userList, String txtMesseage) throws Exception
    {
        int i, n = userList.size();
        User u;
        for(i = 0; i < n; i++)
        {
            u = userList.get(i);
           // u.SendMessage(Global.TextMessege);
           // u.SendMessage(txtMesseage);
        }
    }
    public void SendFileTo()
    {
    }

}
