/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import RegistrationForm.RegistrationData;
import java.io.File;
import java.net.Socket;
import java.util.Set;

/**
 *
 * @author Администратор
 */
public class FormatedMessages extends Messages{
 

    public FormatedMessages(Socket in, Socket out, User usr){
        
        super(in, out, usr);

    }
    public void Initialize() throws Exception
    {
        super.InitializeMessages();
    }
    public void ReConnect(Socket in, Socket out) throws Exception
    {
        super.ReConnect(in, out);
    }
    public void ListenForMessages() throws Exception
    {
        while(true) {
            String metaData = this.ReceiveMessage();
            if(metaData.equals(FormatCharacters.TextMessege) ) this.ReceiveTextMessage();
            else if(metaData.equals(FormatCharacters.Auth)) this.AuthenticationPostBack();
            else if(metaData.equals(FormatCharacters.RequestStructureTree)) this.ResponseStructureTree();
        }        
    }
    public void ReceiveTextMessage() throws Exception
    {
        // получили мессагу и выводим отправляем на экран
        String txtMessage = this.ReceiveMessage();
        String []usrInfo = txtMessage.split(":", 2);
        UserInfo uInfo = UserInfo.Parse(usrInfo[0]);
        this.user.getScreenView().AddMessageToScreen(uInfo.treeName,usrInfo[1]);

    }
    public void SendTextMessage(String txtMessage, Set<Integer> ids) throws Exception
    {
        String sids = ids.toString();
        sids = sids.substring(1, sids.length() - 1);
        this.SendMessage(FormatCharacters.TextMessege);
        this.SendMessage(sids);
        this.SendMessage(txtMessage);
    }
    public void SendFileTo()
    {
    }
    public boolean AuthenticateUser(String ip) throws Exception
    {
        this.SendMessage(FormatCharacters.Auth);
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
        this.SendMessage(FormatCharacters.RequestStructureTree);
        return this.ReceiveMessageSync();
    }
    public String RequestUserStructureTree() throws Exception
    {
        this.SendMessage(FormatCharacters.getUserStructureTree);
        return this.ReceiveMessageSync();
    }
    public boolean RegisterUser(RegistrationData rData) throws Exception
    {
        this.SendMessage(FormatCharacters.RequestRegistration);
        this.SendMessage(rData.toString());
        String response = this.ReceiveMessageSync();
        if(response.length() == 4) return true;
        return false;
        
    }
    public void getOfflineMessages() throws Exception
    {
        this.SendMessage(FormatCharacters.getOfflineMessages);
    }
    public void SendFiles(File [] files, Set<Integer> ids) throws Exception
    {
        long totalSize = 0;
        for(int i = 0; i < files.length; i++)
            totalSize += files[i].length();

        this.SendMessage(FormatCharacters.SendFile);
        this.SendMessage(String.valueOf(totalSize));
        this.SendMessage(String.valueOf(files.length));


    }
    public String getFilesMetadata() throws Exception
    {
        this.SendMessage(FormatCharacters.getFiles);
        String metadata = this.ReceiveMessageSync() ;
        return metadata;
    }

}
