/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

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
    public void Initialize() throws Exception
    {
        super.InitializeMessages();
    }
    public void ListenForMessages() throws Exception
    {
        while(true) {
            String metaData = this.ReceiveMessage();
            if(metaData.equals(FormatCharacters.TextMessege)) this.ReceiveTextMessage();
            else if(metaData.equals(FormatCharacters.Auth)) this.AuthenticateUser();
            else if(metaData.equals(FormatCharacters.RequestStructureTree)) this.ResponseStructureTree();
            else if(metaData.equals(FormatCharacters.RequestRegistration)) this.RegisterUser();
        }
    }
    public void ReceiveTextMessage() throws Exception
    {
        // получаем от юзера мессагу и шлем(временно) всем юзерам через очередь юзеров
        String txtMessage = this.ReceiveMessage();
        this.user.queue.SendMessageToUser(txtMessage, this.user);

    }
    public void SendTextMessage(String txtMessage) throws Exception
    {
        this.SendMessage(FormatCharacters.TextMessege);
        this.SendMessage(txtMessage);
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
           this.SendMessageSync(Auth.toString());
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    public void ResponseStructureTree() throws Exception
    {
        this.SendMessageSync(Global.XmlTree);
    }
    public void RegisterUser() throws Exception
    {
        String structureId = this.ReceiveMessage();
        String result = this.user.RegisterUser(structureId).toString();
        this.SendMessageSync(result);   
    }


}
