/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Администратор
 */
public class ReceiveFile  extends Thread{
    Socket socket;

    public ReceiveFile(Socket acceptSocket) {
        socket = acceptSocket;
    }

    @Override
    public void run() {
        String path = Global.saveFilePath;
        path += Utils.GetDate() + "_";
        FileOutputStream fileOutputStream = null;
        byte []packet = new byte[Global.PACKET_SIZE];
        try {
            String len = "";
            while(true)
            {
                socket.getInputStream().read(packet, 0, 1);
                if(packet[0] == 0) throw  new Exception("Connection closed by user");
                if(packet[0] == FormatCharacters.marker) break;
                len += packet[0] - '0';
            }
            socket.getInputStream().read(packet, 0, Integer.valueOf(len));
            String [] metadata = new String(packet, 0, Integer.valueOf(len)).split(FormatCharacters.marker + "");
            long fileSize = Long.valueOf(metadata[0]);
            path += metadata[1];
            fileOutputStream = new FileOutputStream(path);
            int readed;
            while(fileSize > 0)
            {
                readed = socket.getInputStream().read(packet, 0, (int)Math.min(fileSize, Global.PACKET_SIZE));
                fileOutputStream.write(packet, 0, readed);
                fileSize -= readed;
            }
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        try {
            if(socket != null) socket.close();
            if(fileOutputStream != null) fileOutputStream.close();
        }catch(Exception e) {}
    }
}
