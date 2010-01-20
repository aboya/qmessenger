/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
        File file = null;
        String metadata;
        boolean ReceiveOk = true;
        int readed;
        int fileid = 0;
        try {
            int metadataLen = 0;
            while(true)
            {
                readed = socket.getInputStream().read(packet, 0, 1);
                if(readed <= 0) throw  new Exception("Connection closed by user");
                if(packet[0] == FormatCharacters.marker) break;
                _len += packet[0] - '0';
                metadataLen ++;
                if(metadataLen > Global.MAXLEN) throw new Exception("To long metadata lenght");
            }
            len = Integer.valueOf(_len);
            socket.getInputStream().read(packet, 0, len);
            
            for(int i = 0, k = 1; i < len; i++ , k *= 10)
                fileid += (packet[len - i - 1] - '0') * k;
            
            rs = dbFunc.getFilePathByID(connection, fileid);
            if( !rs.next() ) throw new Exception("Ids not found");
            path = rs.getString("Path");
            fileName = rs.getString("FileName");
            
            file = new File(path);
            fileSize = file.length();
            fileInputStream = new FileInputStream(path);
            long checkSum = Utils.Checksum(path);
            metadata = String.valueOf(fileSize) + 
                    FormatCharacters.marker +
                    fileName +
                    FormatCharacters.marker +
                    String.valueOf(checkSum);
            metadata = String.valueOf(metadata.length()) +
                    String.valueOf(FormatCharacters.marker)+
                    metadata;
            socket.getOutputStream().write(metadata.getBytes());
            Log.Write(String.valueOf(fileSize));
            while(fileSize > 0)
            {
                readed = fileInputStream.read(packet, 0, Global.PACKET_SIZE);
                if(readed <= 0) throw new Exception("Somthing failed when read file");
                socket.getOutputStream().write(packet, 0, readed);
                fileSize -= readed;
            }
        }catch(Exception ee)
        {
            ReceiveOk = false;
            Log.WriteException(ee);
        }
        try {
          if(socket != null) socket.close();
          }catch(Exception ee) {}
         try {
          if(rs != null) rs.close();
          }catch(Exception ee) {}
         try {
          if(fileInputStream != null)
              fileInputStream.close();
         }catch(Exception ee) {}
        if(ReceiveOk)
        {
            try {
             if( file != null && dbFunc.RemoveFileByID(connection, fileid) == 0 ) {
                 //force call to gc, if we don't call this file not deleted !
                 System.gc();
                 file.delete();
             }
            }catch(Exception ee) {
                Log.WriteException(ee);
            }
        }
        connection.Close();
    }
}
