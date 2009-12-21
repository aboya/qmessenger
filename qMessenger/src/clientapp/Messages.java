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
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(SocketOut.getOutputStream()));
        bufferedWriterSync = new BufferedWriter(new OutputStreamWriter(SocketIn.getOutputStream()));
        this.bufferedReader = new BufferedReader(new InputStreamReader(SocketIn.getInputStream()));
        bufferedReaderSync = new BufferedReader(new InputStreamReader(SocketOut.getInputStream()));
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
        //byte []b = new byte[Global.PACKET_SIZE];
        char []c = new char[Global.PACKET_SIZE];
        len = "";     
        while(true)
        {
             bufferedReader.read(c, 0, 1);
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
    public void CloseConnection() throws Exception
    {
        SocketIn.close();
        SocketOut.close();
        bufferedReader.close();
        bufferedReaderSync.close();
        bufferedWriter.close();
        bufferedWriterSync.close();
    }
}
