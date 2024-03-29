/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.ResultSet;

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
        String path, fileName, fromUserId;
        byte []packet = new byte[Global.PACKET_SIZE];
        char [] buf = new char[Global.PACKET_SIZE];
        FileInputStream fileInputStream = null;
        File file = null;
        String metadata;
        boolean ReceiveOk = true;
        int readed;
        int fileid = 0;
        BufferedWriter socketBufferedWriter = null;
        BufferedReader socketBufferedReader = null;

        try {
            InputStream socketInputStream = socket.getInputStream();
            OutputStream socketOutputStrean = socket.getOutputStream();
            socketBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Global.codePage));
            socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),Global.codePage));
            int metadataLen = 0;
            while(true)
            {
                //readed = socketInputStream.read(packet, 0, 1);
                readed = socketBufferedReader.read(buf, 0, 1);
                if(readed <= 0) throw  new Exception("Connection closed by user");
                if(buf[0] == FormatCharacters.marker) break;
                _len += buf[0] - '0';
                metadataLen ++;
                if(metadataLen > Global.MAXLEN) throw new Exception("To long metadata lenght");
            }
            len = Integer.valueOf(_len);
            socketBufferedReader.read(buf, 0, len);
            
            for(int i = 0, k = 1; i < len; i++ , k *= 10)
                fileid += (buf[len - i - 1] - '0') * k;
            
            rs = dbFunc.getFilePathByID(connection, fileid);
            if( !rs.next() ) throw new Exception("Ids not found");
            path = rs.getString("Path");
            fileName = rs.getString("FileName");
            fromUserId = rs.getString("fromUserID");
            file = new File(path);
            fileSize = file.length();
            fileInputStream = new FileInputStream(path);
            long checkSum = Utils.Checksum(path);
            metadata = String.valueOf(fileSize) + 
                    FormatCharacters.marker +
                    fileName +
                    FormatCharacters.marker +
                    String.valueOf(checkSum) +
                    FormatCharacters.marker +
                    fromUserId;
            metadata = String.valueOf(metadata.length()) +
                    String.valueOf(FormatCharacters.marker)+
                    metadata;
            socketBufferedWriter.write(metadata, 0, metadata.length());
            socketBufferedWriter.flush();
            socketBufferedWriter = null;
            socketBufferedReader = null;
            System.gc();
            this.sleep(500);
            //socket.getOutputStream().write(metadata.getBytes());
            Log.Write(String.valueOf(fileSize));
            while(fileSize > 0)
            {
                readed = fileInputStream.read(packet, 0, (int)Math.min(fileSize, Global.PACKET_SIZE));
                if(readed <= 0) throw new Exception("Somthing failed when read file");
                socketOutputStrean.write(packet, 0, readed);
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
         socket = null;
         try {
          if(rs != null) rs.close();
          }catch(Exception ee) {}
         try {
          if(fileInputStream != null)  fileInputStream.close();
         }catch(Exception ee) {}
         fileInputStream = null;
        if(ReceiveOk)
        {
            try {
                //RemoveFileByID удаляет фалй из базы и возвращает количество юзеров ожидающих этот файл
             if( file != null && dbFunc.RemoveFileByID(connection, fileid) == 0 ) {
                 new DeleteFile(file).start();
             }
            }catch(Exception ee) {
                Log.WriteException(ee);
            }
        }

        connection.Close();
    }
}
