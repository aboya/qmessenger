/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author Администратор
 */
public class SendFile extends Thread {
    Socket socket;

    public SendFile(Socket _socket) {
        this.socket = _socket;

    }

    @Override
    public void run() {
        ResultSet rs = null;
        dbConnection connection = new dbConnection();
        long fileSize;
        String _len = "";
        int len;
        String path, fileName;
        byte []packet = new byte[Global.PACKET_SIZE];
        FileInputStream fileInputStream = null;
        File file;
        String metadata;
        try {
            while(true)
            {
                socket.getInputStream().read(packet, 0, 1);
                if(packet[0] == 0) throw  new Exception("Connection closed by user");
                if(packet[0] == FormatCharacters.marker) break;
                _len += packet[0] - '0';
            }
            len = Integer.valueOf(_len);
            socket.getInputStream().read(packet, 0, len);
            int fileid = 0;
            for(int i = 0, k = 1; i < len; i++ , k *= 10)
                fileid += packet[len - i - 1] * k;
            
            rs = dbFunc.getFilePathByID(connection, fileid);
            path = rs.getString("Path");
            fileName = rs.getString("FileName");
            file = new File(path);
            fileSize = file.length();
            fileInputStream = new FileInputStream(file);
            long checkSum = Utils.Checksum(path);
            metadata = String.valueOf(fileSize) + 
                    FormatCharacters.marker +
                    fileName +
                    FormatCharacters.marker +
                    String.valueOf(checkSum);
            socket.getOutputStream().write((Integer.valueOf(metadata.length()) + FormatCharacters.marker +  metadata).getBytes());
            int readed;
            while(fileSize > 0)
            {
                readed = fileInputStream.read(packet, 0, Global.PACKET_SIZE);
                if(readed <= 0) throw new Exception("Somthing failed when read file");
                socket.getOutputStream().write(packet, 0, readed);
                fileSize -= readed;
            }
            if( dbFunc.RemoveFileByID(connection, fileid) == 0 )
            {
                file.delete();
            }
        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        try {
          if(socket != null) socket.close();
          if(rs != null) rs.close();
          if(fileInputStream != null) fileInputStream.close();
        }catch(Exception ee)
        {}
    }


}
