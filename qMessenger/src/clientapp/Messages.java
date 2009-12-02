/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;


import java.net.Socket;


/**
 *
 * @author Администратор
 */
public class Messages {
    private Socket SocketIn;
    private Socket SocketOut;
    private User user;
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
            SocketOut.getOutputStream().write(res.getBytes(), 0, res.length());
    }
    protected  String ReceiveMessage() throws Exception
    {
        String len, res;
        int Length, readed;
        byte []b = new byte[Global.PACKET_SIZE];
        len = "";
        while(true)
        {
             SocketIn.getInputStream().read(b, 0, 1);
             if(b[0] == FormatCharacters.marker) break;
             len += b[0]-'0';
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
    public void CloseConnection() throws Exception
    {
        SocketIn.close();
        SocketOut.close();
    }

}
