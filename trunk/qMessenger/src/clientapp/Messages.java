/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
    protected void CloseConnection() throws Exception
    {
        SocketIn.close();
        SocketOut.close();
        bufferedReader.close();
        bufferedReaderSync.close();
        bufferedWriter.close();
        bufferedWriterSync.close();
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
    public void SendFile(File file) throws Exception
    {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufFileReader = new BufferedReader(fileReader);
        long len = file.length();
        int readed;
        char [] packet = new char[Global.PACKET_SIZE];
        while(len > 0)
        {
            if(len < Global.PACKET_SIZE) readed = bufFileReader.read(packet, 0, (int)len);
            else readed = bufFileReader.read(packet, 0, Global.PACKET_SIZE);
            len -= readed;
            bufferedWriter.write(packet, 0, readed);
            bufferedWriter.flush();
        }
        fileReader.close();
        bufFileReader.close();
    }
    public void ReceiveFile(String path, long fileSize) throws Exception
    {
        FileWriter fileWriter = new FileWriter(path);
        BufferedWriter bufFileWriter = new BufferedWriter(fileWriter);
        int readed;
        char [] packet = new char[Global.PACKET_SIZE];
        while(fileSize > 0)
        {
            if( fileSize < Global.PACKET_SIZE )
                 readed = bufferedReader.read(packet, 0, (int)fileSize);
            else readed = bufferedReader.read(packet, 0, Global.PACKET_SIZE);

            bufFileWriter.write(packet, 0, readed);
            bufFileWriter.flush();
            fileSize -= readed;
        }
        fileWriter.close();
        bufFileWriter.close();

    }
}
