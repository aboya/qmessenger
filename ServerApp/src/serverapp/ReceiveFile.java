/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package serverapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
    dbConnection connection;
    enum MetaData {
        FileSize ,
        FileName ,
        UserIDs,
        CheckSum
    }

    public ReceiveFile(Socket acceptSocket) {
        socket = acceptSocket;
       
    }

    @Override
    public void run() {
        int metadatasize = 500;
        String path = Global.saveFilePath;
        String []allids = null;
        path += Utils.GetDate() + "_";
        FileOutputStream fileOutputStream = null;
        BufferedReader bufferedReader = null;
        byte []packet = new byte[Global.PACKET_SIZE];
        char [] buf = new char[metadatasize];
        long fileSize = 0;
        long checkSum = 0;
        String fileName = "";
        boolean ReceivingOk = true;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),Global.codePage));
            String len = "";
            int metadataLen = 0;
            while(true)
            {
                socket.getInputStream().read(packet, 0, 1);
                if(packet[0] == 0) throw  new Exception("Connection closed by user");
                if(packet[0] == FormatCharacters.marker) break;
                len += packet[0] - '0';
                metadataLen++;
                if(metadataLen > Global.MAXLEN) throw new Exception("Too long metadata length");
            }
            //socket.getInputStream().read(packet, 0, Integer.valueOf(len));
            bufferedReader.read(buf, 0, Integer.valueOf(len));
            String dbg = new String(buf, 0, Integer.valueOf(len));
            String [] metadata = new String(buf, 0, Integer.valueOf(len)).split(FormatCharacters.marker + "");
            allids = metadata[2].split(",");
            fileSize = Long.valueOf(metadata[0]);
            path += fileName = metadata[1];
            checkSum = Long.valueOf(metadata[3]);
            fileOutputStream = new FileOutputStream(path);
            int readed;
            Log.Write("ReceiveFileSize:" + String.valueOf(fileSize));
            while(fileSize > 0)
            {
                readed = socket.getInputStream().read(packet, 0, Global.PACKET_SIZE);
                if(readed <= 0) throw new Exception ("Reading closed by user or somthing else");
                fileOutputStream.write(packet, 0, readed);
                fileSize -= readed;
            }
            long downloadedFileCheckSum = Utils.Checksum(path);
            if(downloadedFileCheckSum != checkSum)
            {
                File f = new File(path);
                f.delete();
                ReceivingOk = false;
                // отправляем результат клиенту - не совпадение чексум
                socket.getOutputStream().write(0);
             }else {
                 // отправляем результат клиенту - всё пучком
                 socket.getOutputStream().write(1);
            }
        }catch(Exception e)
        {
            Log.WriteException(e);
        }
        try {
          if(fileOutputStream != null) fileOutputStream.close();
        }catch(Exception e) {}
        try {
         if(socket != null) socket.close();
        }catch(Exception ee){}
        try {
         if(bufferedReader != null) bufferedReader.close();
        }catch(Exception ee){}
        try {
            if(fileSize > 0)
            {
                File f = new File(path);
                f.delete();
                ReceivingOk = false;
            }
        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        if(ReceivingOk)
        {
            connection = new dbConnection();
            String  ip = socket.getInetAddress().toString();
            Integer [] whereIds = new Integer[allids.length];
            for(int i = 0; i < allids.length; i++)
                whereIds[i] = Integer.valueOf(allids[i].trim());
            dbFunc.SendFileToUser(connection, ip, whereIds, fileName, path, checkSum);
            connection.Close();
        }
        
    }
}
