/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.net.Socket;

/**
 *
 * @author Администратор
 */
public class FormatedMessages extends Messages{

    public FormatedMessages(Socket in, Socket out, User usr) {
        super(in, out, usr);
    }
    public void SendFileTo(User ruser, String FileName, long FileSize)
    {
       // super.SendMessage(FileName);

    }
    public void ReceiveFileFrom(User ruser)
    {
        
    }

}
