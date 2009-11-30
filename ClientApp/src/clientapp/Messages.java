/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

/**
 *
 * @author Серёжа
 */
import java.net.Socket;
public class Messages {
    private Socket SocketIn;
    private Socket SocketOut;
    //Reserved size append to message - indicates size of message
    // 9999999999999999999
    private static Integer PreffixSize = 18;
    private  static char marker ='&';
    public Messages(Socket in, Socket out)
    {
        this.SocketIn = in;
        this.SocketOut = out;
    }
    void SendMessage(String message) throws Exception
    {
        String res = String.valueOf(message.length()) + marker;
        res += message;
        SocketOut.getOutputStream().write(res.getBytes(), 0, res.length());
    }
    String ReceiveMessage() throws Exception
    {
        
        String len, res;
        int Length, readed;
        byte []b = new byte[Global.PACKET_SIZE];
        len = "";
        while(true)
        {
            SocketIn.getInputStream().read(b, 0, 1);
            if(b[0] == marker) break;
            len += b[0] - '0';
        }
        Length = Integer.valueOf(len);
        res = "";
        while(Length > 0)
        {
            readed = SocketIn.getInputStream().read(b, 0, Global.PACKET_SIZE);
            res += new String(b, 0, readed);
            Length -= readed;
        }
        return res;
    }

}

