/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;
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
            else if(metaData.equals(FormatCharacters.getOfflineMessages)) this.SendOfflineMessages();
        }
    }
    public void SendOfflineMessages() throws Exception
    {
        Vector <Pair> Offmess = dbFunc.getMessagesForUser(this.getUser().connection, null, this.getUser().ip);
        for(int i = 0; i < Offmess.size(); i++)
             this.SendTextMessage((String)Offmess.get(i).getSecond() + ":" + (String)Offmess.get(i).getFirst());
    }
    public void ReceiveTextMessage() throws Exception
    {
        // получаем от юзера мессагу и шлем(временно) всем юзерам через очередь юзеров
        String ids = ReceiveMessage();
        String [] SplitIds = ids.split(",");
        Set <Integer> allIds = new TreeSet<Integer>();
        for(int i = 0; i < SplitIds.length; i++)
        {
            allIds.add(Integer.parseInt(SplitIds[i].trim()));
        }
        allIds.add(this.getUser().getTreeID());
        String txtMessage = this.ReceiveMessage();
        this.getUser().getQueue().SendMessageToUser(txtMessage, allIds,getUser());
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
        this.getUser().AuthenticateUser();
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
        String result = this.getUser().RegisterUser(structureId).toString();
        this.SendMessageSync(result);   
    }
}
