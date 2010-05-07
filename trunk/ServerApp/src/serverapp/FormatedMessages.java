/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import Comunication.ComunicationBase;
import Comunication.TextMessage;
import Comunication.UserMessageHistory;
import java.io.BufferedWriter;
import java.io.File;
import java.net.Socket;
import java.sql.ResultSet;
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
            Log.Write(metaData);
            if(metaData.equals(FormatCharacters.TextMessege)) this.ReceiveTextMessage();
            else if(metaData.equals(FormatCharacters.Auth)) this.AuthenticateUser();
            else if(metaData.equals(FormatCharacters.RequestStructureTree)) this.ResponseStructureTree();
            else if(metaData.equals(FormatCharacters.RequestRegistration)) this.RegisterUser();
            else if(metaData.equals(FormatCharacters.getOfflineMessages)) this.SendOfflineMessages();
            else if(metaData.equals(FormatCharacters.getFiles)) this.getListFiles();
            else if(metaData.equals(FormatCharacters.getUserStructureTree)) this.ResponseUserStructureTree();
            else if(metaData.equals(FormatCharacters.getUserPath)) this.SendUserPath();
            else if(metaData.equals(FormatCharacters.getUserHistory)) this.SendUserHistory();
        }
    }
    public void SendUserHistory()
    {
        try {
            String IsGetFullPaths = this.ReceiveMessage();
            UserMessageHistory msg = getUser().getMessageHistory(Boolean.valueOf(IsGetFullPaths));
            String _data = ComunicationBase.toString(msg);
            this.SendMessageSync(_data);
        }
        catch(Exception e)
        {
            Log.WriteException(e);
        }
    }
    public void SendUserPath() throws Exception
    {
        String userId = this.ReceiveMessage();
        Pair p = getUser().getQueue().getUserPath(Integer.valueOf(userId));
        this.SendMessageSync(String.valueOf(p.getSecond()));
    }
    public void SendOfflineMessages() throws Exception
    {
        Vector <Pair> Offmess = dbFunc.getMessagesForUser(this.getUser().connection, this.getUser().ip);
        for(int i = 0; i < Offmess.size(); i++)
             this.SendTextMessage((String)Offmess.get(i).getSecond() + ":" + (String)Offmess.get(i).getFirst());
    }
    public void ReceiveTextMessage() throws Exception
    {
        // получаем от юзера мессагу и шлем(временно) всем юзерам через очередь юзеров
        String object = ReceiveMessage();
        TextMessage txt = (TextMessage)ComunicationBase.fromString(object);
        this.getUser().getQueue().SendMessageToUser(txt, getUser());
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
    public void ResponseUserStructureTree() throws Exception
    {
        this.SendMessageSync(GenerateUserTree.getUserTree());
    }
    public void RegisterUser() throws Exception
    {

        String registrationData = this.ReceiveMessage();
        RegistrationData rData = RegistrationData.Parse(registrationData);
        String result = this.getUser().RegisterUser(rData).toString();
        //this.getUser().getQueue().
        this.SendMessageSync(result);   
    }
    public void getListFiles()
    {
        ResultSet rs = null;
        long totalSize;
        int count;
        dbConnection connection = this.getUser().connection;
        try {
           rs = dbFunc.getFilesForUser(connection, this.getUser().ip);
           String fnames = "";
           
           File f;
           totalSize = 0;
           count = 0;
           String filePath;
           while(rs.next())
           {
               fnames += rs.getString("FileName") + "|" + rs.getString("ID") + "|";
               filePath = rs.getString("Path");
               f = new File(filePath);
               totalSize += f.length();
               count ++;
           }
           connection.Close();

           String metadata = "";
           if(fnames.length() > 0)
           {
               metadata = fnames.substring(0, fnames.length() - 1)+  // remove last "|"
                          FormatCharacters.marker +
                          String.valueOf(totalSize) +
                          FormatCharacters.marker +
                          String.valueOf(count) +
                          FormatCharacters.marker;
           }
           
           this.SendMessageSync(metadata);

        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        try {
            if(rs != null) rs.close();
        }catch(Exception ee){}
    }
    public void UpdateUserTree() throws Exception
    {
        this.SendMessage(FormatCharacters.updateUserTree);
    }
}
