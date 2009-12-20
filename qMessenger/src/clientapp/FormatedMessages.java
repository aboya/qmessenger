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
    public void ListenForMessages() throws Exception
    {
        while(true) {

            String metaData = this.ReceiveMessage();
            if(metaData.equals(Global.TextMessege) ) this.ReceiveMessage();
            else if(metaData.equals(Global.Auth)) this.AuthenticationPostBack();
            else if(metaData.equals(Global.RequestStructureTree)) this.ResponseStructureTree();
            else if(metaData.endsWith(Global.RequestRegistration)) this.user.isRegister = 1;
        }

        
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
    public boolean AuthenticateUser(String ip) throws Exception
    {
        this.SendMessage(Global.Auth);
        this.SendMessage(ip);
        String response = this.ReceiveMessageSync();
        // cool & quick way to check true :)))))
        if(response.length() == 4) return true;
        return false;
    }
    public void AuthenticationPostBack() throws Exception
    {
        //String res = this.ReceiveMessage();
       // if(res.equals("true")) this.user.isAuth = 1;
       // else this.user.isAuth = 0;
    }
    public void ResponseStructureTree() throws Exception
    {
        user.structTreeXml = this.ReceiveMessage();
    }
    public String RequestStructureTree() throws Exception
    {
        this.SendMessage(Global.RequestStructureTree);
        return this.ReceiveMessageSync();
    }
    public boolean RegisterUser(int structureId) throws Exception
    {
        this.SendMessage(Global.RequestRegistration);
        this.SendMessage(String.valueOf(structureId));
        String response = this.ReceiveMessageSync();
        if(response.length() == 4) return true;
        return false;
        
    }

}