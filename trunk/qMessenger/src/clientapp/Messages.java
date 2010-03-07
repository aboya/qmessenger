/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;


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
    protected User user;
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
    protected void InitializeMessages() throws Exception
    {
        InputStreamReader r;
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(SocketOut.getOutputStream(),Global.codePage));
        bufferedWriterSync = new BufferedWriter(new OutputStreamWriter(SocketIn.getOutputStream(),Global.codePage));
        this.bufferedReader = new BufferedReader(r = new InputStreamReader(SocketIn.getInputStream(),Global.codePage));
        bufferedReaderSync = new BufferedReader(new InputStreamReader(SocketOut.getInputStream(),Global.codePage));
        System.out.println(r.getEncoding());
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
        while(true)
        {
             bufferedReader.read(c, 0, 1);
             if(c[0] == 0) throw new Exception("Connection Close by server");
             if(c[0] == FormatCharacters.marker) break;
             len += c[0] - '0';
        }
        Length = Integer.valueOf(len);
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
        while(true)
        {
             bufferedReaderSync.read(c, 0, 1);
             if(c[0] == 0) throw new Exception("Connection Close by server");
             if(c[0] == FormatCharacters.marker) break;
             len += c[0] - '0';
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
    protected void CloseConnection()
    {
        try{
        SocketIn.close();
        }catch(Exception e)
        {}
        try{
        SocketOut.close();
        }catch(Exception e)
        {}
        try{
        bufferedReader.close();
        }catch(Exception e)
        {}
        try{
        bufferedReaderSync.close();
        }catch(Exception e)
        {}
        try{
        bufferedWriter.close();
        }catch(Exception e){}
        try{
        bufferedWriterSync.close();
        }catch(Exception e)
        {}
        SocketIn = null;
        SocketOut = null;
        bufferedReader = null;
        bufferedReaderSync = null;
        bufferedWriter = null;
        bufferedWriterSync = null;
    }
    public void ReConnect(Socket in, Socket out) throws Exception
    {
        this.SocketIn = in;
        this.SocketOut = out;
        InitializeMessages();
    }
    public static String ConvertCodePage(String inp) throws Exception
    {
        return new String(inp.getBytes("UTF-8"));
    }

}
