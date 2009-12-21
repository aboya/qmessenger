/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 *
 * @author Администратор
 */
public class Messages {
    private Socket SocketIn;
    private Socket SocketOut;
    protected User user;
    BufferedReader in = null;
    public Messages(Socket in, Socket out, User usr)
    {
        this.SocketIn = in;
        this.SocketOut = out;
        this.user = usr;

    }
    protected void SendMessage(String message) throws Exception
    {
        String res = String.valueOf(message.length()) + FormatCharacters.marker;
        res += message;
        PrintWriter out = new PrintWriter(SocketOut.getOutputStream(), true);
        out.print(message);
        out.close();

       // SocketOut.getOutputStream().write(res.getBytes(), 0, res.length());
    }
    // send message to socket from read
    protected void SendMessageSync(String message) throws Exception
    {
        String res = String.valueOf(message.length()) + FormatCharacters.marker;
        res += message;
        PrintWriter out = new PrintWriter(SocketIn.getOutputStream(), true);
        out.print(message);
        out.close();

       // SocketIn.getOutputStream().write(res.getBytes(), 0, res.length());
    }
    protected  String ReceiveMessage() throws Exception
    {
        String len, res;
        int Length, readed;
        //byte []b = new byte[Global.PACKET_SIZE];
        char []c = new char[Global.PACKET_SIZE];
        len = "";
                
        in = new BufferedReader(new InputStreamReader(SocketIn.getInputStream()));
        while(true)
        {

            // SocketIn.getInputStream().read(b, 0, 1);
             c[0] = (char) in.read();
             if(c[0] == FormatCharacters.marker) break;
             len += c[0] - '0';
        }
        Length = Integer.valueOf(len);
        res = "";
        while(Length > 0)
        {
             readed = in.read(c, 0, Math.min(Length, Global.PACKET_SIZE));
             //readed = SocketIn.getInputStream().read(b, 0, Math.min(Length, Global.PACKET_SIZE));
             res += new String(c, 0, readed);
             Length -= readed;
        }
        in.close();
        return res;
    }
    protected  String ReceiveMessageSync() throws Exception
    {
        String len, res;
        int Length, readed;
        //byte []b = new byte[Global.PACKET_SIZE];
        char []c = new char[Global.PACKET_SIZE];
        len = "";

        in = new BufferedReader(new InputStreamReader(SocketOut.getInputStream()));
        while(true)
        {

            // SocketOut.getInputStream().read(b, 0, 1);
             c[0] = (char) in.read();
             if(c[0] == FormatCharacters.marker) break;
             len += c[0] - '0';
        }
        Length = Integer.valueOf(len);
        res = "";
        while(Length > 0)
        {
             readed = in.read(c, 0, Math.min(Length, Global.PACKET_SIZE));
             //readed = SocketOut.getInputStream().read(b, 0, Math.min(Length, Global.PACKET_SIZE));
             res += new String(c, 0, readed);
             Length -= readed;
        }
        in.close();
        return res;
    }
    public void CloseConnection() throws Exception
    {
        SocketIn.close();
        SocketOut.close();
    }
}
