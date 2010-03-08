/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ReceiveFileDialog;

/**
 *
 * @author Администратор
 */
/*
public class ReceiveFileDialogView {

}
 * */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import clientapp.FormatCharacters;
import clientapp.Global;
import clientapp.Log;
import clientapp.Pair;
import clientapp.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Администратор
 */
public class ReceiveFileDialogView extends Thread {
    ReceiveFileDialogFrame receiveFileDialogFrame = null;
     boolean isClosed = false; // нужна для корректного выхода
     boolean pleaseWait = false;
     Integer index;
     Semaphore semaphore = null;
     Boolean isRemoveFileFromServer; //этот бит говорит серверу удалить файл и больше не присылать запрос на его скачивание
     /*
      * abortSetFileList нужна для синхронизации
      * когда юзверь удаляет текущий аплоад, мы закрываем сокет
      * и удалям строки из таблицы, но после закрытия сокета процедура еще может
      * вызвать Set таблицы, с неверным индеском. Вот что бы этого не происходило
      * устанавливаем abortSetFileList перед входом в функцию в фалс, и будем смотреть
      * на него в ходе выполнения.
      */

     Socket socket;
     LinkedList<Pair<String, Integer > > fileQuene = new LinkedList<Pair<String, Integer >>();
     FileOutputStream fileOutputStream = null;


    public ReceiveFileDialogView(final String WindowName) {
        receiveFileDialogFrame = new ReceiveFileDialogFrame();
        receiveFileDialogFrame.setVisible(true);
        receiveFileDialogFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void ReceiveFiles(final int [] fileIds, final String [] fileNames)
    {
        JTable table = receiveFileDialogFrame.getTable();
        DefaultTableModel model = (DefaultTableModel)table.getModel();

       fileQuene.clear();
       for(int i = 0; i < fileNames.length; i++)
       {
           model.addRow(new Object[] {fileNames[i], "0%" } );
           fileQuene.addLast(new Pair<String, Integer >(fileNames[i], fileIds[i]));
       }
       table.repaint();
       this.run();
    }
    @Override
    public void run()
    {
         this.ReceiveFiles();
   }
    private void ReceiveFiles()
    {
        Pair < String , Integer > p;
        index = 0;
        while( index < fileQuene.size())
        {
           p = fileQuene.get(index);
           this.CheckWait();
           this.ReceiveFile(p.getFirst(), p.getSecond(), index);
           index++;
        }
    }
    private void ReceiveFile(String path, Integer ids, int lineNumber)
    {
        this.isRemoveFileFromServer = false;
        this.socket = null ;
        fileOutputStream = null;
        byte [] packet = new byte[Global.PACKET_SIZE];
        char [] buf = new char [Global.PACKET_SIZE];
        long lasttime = 0, currenttime = 0;
        boolean status = true;
        String metadata;
        long fileSize, checkSum, Len = 0;
        String fileName = "";
        InputStream socketInputStream;
        OutputStream socketOutputStream;
        BufferedReader socketBufferedReader = null;
        BufferedWriter socketBufferedWriter = null;
        try {
            socket = new Socket(Global.ServAddr, Global.ServerFileDownloadPort);
            socketBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),Global.codePage));
            socketBufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Global.codePage));

            metadata = ids.toString();
            metadata = Integer.valueOf(metadata.length()) + String.valueOf(FormatCharacters.marker) + metadata;
            // запрос на сервер что бы он выдал файл
            //socket.getOutputStream().write(metadata.getBytes());
            socketBufferedWriter.write(metadata, 0, metadata.length());
            socketBufferedWriter.flush();
            String _len = "";
            int len;
            int readed;
            socketInputStream = socket.getInputStream();
            socketOutputStream = socket.getOutputStream();
            while(true)
            {
                readed = socketBufferedReader.read(buf, 0, 1);
                if(readed <= 0) throw new Exception("Connection closed ..");
                if(buf[0] == FormatCharacters.marker) break;
                _len += buf[0] - '0';
            }
            len = Integer.valueOf(_len);
            //socket.getInputStream().read(packet, 0, len);
            socketBufferedReader.read(buf, 0, len);
            metadata = new String(buf, 0, len);

            String []arr = metadata.split(FormatCharacters.marker + "");
            fileSize = Long.valueOf(arr[0]);
            fileName = arr[1];
            checkSum = Long.valueOf(arr[2]);
            fileOutputStream = new FileOutputStream(fileName = Utils.GenerateName(Global.defaultSavePath + fileName));
            Len = fileSize;
            socketBufferedReader = null;
            socketBufferedWriter = null;
            System.gc();
            sleep(500);
            while(fileSize > 0)
            {
                readed =  socketInputStream.read(packet, 0, (int)Math.min(fileSize,Global.PACKET_SIZE));
                if(readed <= 0) throw new Exception("Connection closed ..");
                fileOutputStream.write(packet, 0, readed);
                fileSize -= readed;
                currenttime = Utils.GetDate();
                if(currenttime - lasttime > 1000)
                {
                    lasttime = currenttime;
                    this.SetStatus(String.format("%.2f%%", 100.0 - 100.0 * fileSize / Len));
                    this.CheckWait();
                }

            }
            fileOutputStream.flush();
            fileOutputStream.close();
            fileOutputStream = null;
            System.gc();
            if(Utils.Checksum(fileName) != checkSum)
            {
                System.out.println("incorect checksum");
                status = false;
            }else {
                status = true;
            }
        }catch(Exception ee)
        {
            status = false;
            Log.WriteException(ee);
        }
        try {
           if(this.fileOutputStream != null) this.fileOutputStream.close();
       } catch(Exception ee) {}
        fileOutputStream = null;
        try{
           if(socket != null) socket.close();
        }catch(Exception ee) {}
        socket = null;
        if(!this.isClosed())
        {
            if(status) {
                this.SetStatus(String.format("%.2f%%", 100.0));
                AddReceiveFileToScreen("", fileName, Utils.getAdekvatSize(Len));
            }
            else {
                System.gc();
                this.SetStatus("Failed");
                new File(Utils.GenerateName(Global.defaultSavePath + fileName)).delete();
            }
        }

    }
    private void SetStatus(final String s)
    {
         JTable table = receiveFileDialogFrame.getTable();
         DefaultTableModel model = (DefaultTableModel)table.getModel();
         model.setValueAt(s, index, 1);
         table.repaint();
    }
    // вызывается когда клиент закрывает окно
    public void Close()
    {
        isClosed = true;
        try {
            // жестко вырубаем текущий аплоад
            this.socket.close();
        }catch(Exception e){}
        this.interrupt();
        receiveFileDialogFrame.setVisible(false);
        receiveFileDialogFrame = null;
        System.gc();
    }
    public boolean isClosed()
    {
        return isClosed;
    }
    public void Pause() throws Exception
    {
        synchronized (this) {
            this.pleaseWait = true;
        }
    }
    public void Resume()
    {
        synchronized (this) {
             this.pleaseWait = false;
             this.notify();
        }
    }
    private void CheckWait()
    {
            while (pleaseWait) {
                try {
                    if(this.isClosed()) this.Resume();
                    sleep(100);
                }
                catch (Exception e) { }
            }
    }
    public void RemoveFilesFromQuene(int [] lineNumbers)
    {

        for(int i = 0; i < lineNumbers.length; i++)
        {
            if(lineNumbers[i] == index) {
                this.RemoveCurrentReceiveFileFromQuene();
                break;
            }
        }
        for(int i = 0; i < lineNumbers.length; i++)
        {
            fileQuene.remove(lineNumbers[i]);
            if(lineNumbers[i] <= index) index --;

        }
    }
    public void RemoveCurrentDownload()
    {
        this.isRemoveFileFromServer = true;
    }
    public void RemoveCurrentReceiveFileFromQuene()
    {
        // жестко вырубаем загрузку файла
        try {
            semaphore = new Semaphore(0);

            this.RemoveCurrentDownload();
            this.Resume();
            // ждем пока завершится цикл передающий файл
            // если этого не сделать то установится статус Failed не на той строчке
            // поскольку мы закрыли сокет то поток то цикл завершится быстро

            semaphore.acquire();
            semaphore = null;


        }catch(Exception e) {}
    }
    private void AddReceiveFileToScreen(String from, String fileName, String size)
    {
        Global.getUser().getScreenView().AddReceiveFileToScreen(from, fileName, size);
    }

}

