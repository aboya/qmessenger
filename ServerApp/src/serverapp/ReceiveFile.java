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
        String []allids;
        path += Utils.GetDate() + "_";
        FileOutputStream fileOutputStream = null;
        BufferedReader bufferedReader = null;
        byte []packet = new byte[Global.PACKET_SIZE];
        char [] buf = new char[metadatasize];
        long fileSize = 0;
        long checkSum = 0;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),Global.codePage));
            String len = "";
            while(true)
            {
                socket.getInputStream().read(packet, 0, 1);
                if(packet[0] == 0) throw  new Exception("Connection closed by user");
                if(packet[0] == FormatCharacters.marker) break;
                len += packet[0] - '0';
            }
            //socket.getInputStream().read(packet, 0, Integer.valueOf(len));
            bufferedReader.read(buf, 0, Math.min(metadatasize, Integer.valueOf(len)));
            String dbg = new String(buf, 0, Integer.valueOf(len));
            String [] metadata = new String(buf, 0, Integer.valueOf(len)).split(FormatCharacters.marker + "");
            allids = metadata[2].split(",");
            fileSize = Long.valueOf(metadata[0]);
            path += metadata[1];
            checkSum = Long.valueOf(metadata[3]);
            fileOutputStream = new FileOutputStream(path);
            int readed;
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
            if(socket != null) socket.close();
            if(fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            if(bufferedReader != null) bufferedReader.close();
            if(fileSize > 0)
            {
                File f = new File(path);
                f.delete();
            }
        }catch(Exception e) {}
    }
}
