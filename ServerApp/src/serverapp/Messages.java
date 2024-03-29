/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


/**
 *
 * @author Администратор
 */
public class Messages {
    private Socket SocketIn;
    private Socket SocketOut;
    private User user;
    BufferedReader bufferedReader = null;
    BufferedReader bufferedReaderSync = null;
    BufferedWriter bufferedWriter = null;
    BufferedWriter bufferedWriterSync = null;
    public Messages(Socket in, Socket out, User usr)
    {
        this.SocketIn = in;
        this.SocketOut = out;
        this.user = usr;
    }
    protected  void InitializeMessages() throws Exception
    {
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(SocketOut.getOutputStream(),Global.codePage));
        bufferedWriterSync = new BufferedWriter(new OutputStreamWriter(SocketIn.getOutputStream(),Global.codePage));
        this.bufferedReader = new BufferedReader(new InputStreamReader(SocketIn.getInputStream(),Global.codePage));
        bufferedReaderSync = new BufferedReader(new InputStreamReader(SocketOut.getInputStream(),Global.codePage));
    }
    protected void SendMessage(String message) throws Exception
    {
        String res = String.valueOf(message.length()) + FormatCharacters.marker;
        res += message;
        bufferedWriter.write(res);
        bufferedWriter.flush();
    }
    // send message to socket from read
    protected void SendMessageSync(String message) throws Exception
    {
        String res = String.valueOf(message.length()) + FormatCharacters.marker;
        res += message;        
        bufferedWriterSync.write(res);
        bufferedWriterSync.flush();
    }
    protected  String ReceiveMessage() throws Exception
    {
        String len, res;
        int Length, readed;
        char []c = new char[Global.PACKET_SIZE];
        len = "";
        int metadataLen = 0;
        while(true)
        {
             bufferedReader.read(c, 0, 1);
             if(c[0] == 0) throw new Exception("Connection Close by user");
             if(c[0] == FormatCharacters.marker) break;
             len += c[0] - '0';
             metadataLen ++;
             if(metadataLen > Global.MAXLEN) throw new Exception("too long metadata len");
        }
        Length = Integer.valueOf(len);
        if(Length < 0) throw new Exception("Metadata can not bee negative");
        res = "";
        while(Length > 0)
        {
             readed = bufferedReader.read(c, 0, Math.min(Length, Global.PACKET_SIZE));
             res += new String(c, 0, readed);
             Length -= readed;
        }
        return res;
    }
    protected  String ReceiveMessageSync() throws Exception
    {
        String len, res;
        int Length, readed;
        char []c = new char[Global.PACKET_SIZE];
        len = "";
        int metadataLen = 0;
        while(true)
        {
             bufferedReaderSync.read(c, 0, 1);
             if(c[0] == 0) throw new Exception("Connection Close by user");
             if(c[0] == FormatCharacters.marker) break;
             len += c[0] - '0';
             metadataLen ++;
             if(metadataLen > Global.MAXLEN) throw new Exception("too long metadata len");
        }
        Length = Integer.valueOf(len);
        res = "";
        while(Length > 0)
        {
             readed = bufferedReaderSync.read(c, 0, Math.min(Length, Global.PACKET_SIZE));
             res += new String(c, 0, readed);
             Length -= readed;
        }
        return res;
    }
    public void CloseConnection() 
    {
        //Ignoring all closing exeptions
        try {
        SocketIn.close();
        }catch(Exception ee) {}
        try {
        SocketOut.close();
        }catch(Exception ee) {}
        try {
        bufferedReader.close();
        }catch(Exception ee) {}
        try {
        bufferedReaderSync.close();
        }catch(Exception ee) {}
        try {
        bufferedWriter.close();
        }catch(Exception ee) {}
        try {
        bufferedWriterSync.close();
        }catch(Exception ee){}

        SocketIn = null;
        SocketOut = null;
        bufferedReader = null;
        bufferedReaderSync = null;
        bufferedWriter = null;
        bufferedWriterSync = null;
        System.gc();
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }
}
