/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.io.File;
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
            if(metaData.equals(Global.TextMessege)) this.ReceiveMessage();
            else if(metaData.equals(Global.Auth)) this.AuthenticateUser();
            else if(metaData.equals(Global.RequestStructureTree)) this.ResponseStructureTree();
            else if(metaData.equals(Global.RequestRegistration)) this.RegisterUser();
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
            u.SendMessage(Global.TextMessege);
            u.SendMessage(txtMesseage);
        }
    }
    public void SendFileTo()
    {
    }
    public void AuthenticateUser() throws Exception
    {
        // receiving ip but not using
        // this is neccessarry !
        String ip = this.ReceiveMessage();
        this.user.AuthenticateUser();
    }
    public void AuthenticateUserPostBack(Boolean Auth)
    {
        try {
          // this.SendMessage(Global.Auth);
           this.SendMessageSync(Auth.toString());
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    public void ResponseStructureTree() throws Exception
    {
        //this.SendMessage(Global.RequestStructureTree);
        this.SendMessageSync(Global.XmlTree);
    }
    public void RegisterUser() throws Exception
    {
        String structureId = this.ReceiveMessage();
        String result = this.user.RegisterUser(structureId).toString();
        this.SendMessageSync(result);   
    }

}
