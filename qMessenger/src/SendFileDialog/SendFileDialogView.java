/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SendFileDialog;

import clientapp.FormatCharacters;
import clientapp.Global;
import clientapp.Log;
import clientapp.Pair;
import clientapp.Utils;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Semaphore;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import org.jdesktop.application.SingleFrameApplication;
import qmessenger.ScreenView;

/**
 *
 * @author Администратор
 */
public class SendFileDialogView extends Thread {

     boolean isClosed = false; // нужна для корректного выхода
     boolean pleaseWait = false;
     Integer index;
     Semaphore semaphore = null;
     SendFileDialogFrame sendFileDialogFrame = null;

     Socket socket;

     LinkedList<Pair<String, Set<Integer> > > fileQuene = new LinkedList<Pair<String, Set<Integer>>>();
     FileInputStream fileInputStream = null;

     /*
      * abortSetFileList нужна для синхронизации
      * когда юзверь удаляет текущий аплоад, мы закрываем сокет
      * и удалям строки из таблицы, но после закрытия сокета процедура еще может
      * вызвать Set таблицы, с неверным индеском. Вот что бы этого не происходило
      * устанавливаем abortSetFileList перед входом в функцию в фалс, и будем смотреть
      * на него в ходе выполнения.
      */




    public SendFileDialogView() {

        sendFileDialogFrame = new SendFileDialogFrame(this);
        sendFileDialogFrame.setVisible(true);
        sendFileDialogFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);


    }

    public void SendFiles(String [] filePaths, Set <Integer> ids)
    {

        DefaultTableModel model = (DefaultTableModel)sendFileDialogFrame.getTable().getModel();
        fileQuene.clear();
        for(int i = 0; i < filePaths.length; i++)
        {
            File f = new File(filePaths[i]);
            Object [] row = {f.getName(), "0%"};
            f = null;
            model.addRow(row);
            fileQuene.addLast(new Pair<String, Set<Integer>>(filePaths[i], ids));
        }
        sendFileDialogFrame.getTable().repaint();

        this.start();
        
    }
    @Override
    public void run()
    {
         this.SendFiles();
    }
    private void SendFiles()
    {
        Pair < String , Set<Integer> > p;
        index = 0;
        while( index < fileQuene.size())
        {
           p = fileQuene.get(index);
           this.CheckWait();
           this.SendFile(p.getFirst(), p.getSecond(), index);
           index++;
        }
    }
    private void SendFile(String path, Set<Integer> ids, int lineNumber)
    {
        File f = new File(path);
        String allids = ids.toString();
        allids = allids.substring(1, allids.length() - 1);
        this.socket = null ;
        fileInputStream = null;
        OutputStreamWriter outputStreamReader = null;
        BufferedWriter bufferedWriter = null;

        long lasttime = 0, currenttime = 0;
        boolean status = true;
        try {
            socket = new Socket(Global.ServAddr, Global.ServerFileUploadPort);
            outputStreamReader = new OutputStreamWriter(socket.getOutputStream(),Global.codePage);
            bufferedWriter = new BufferedWriter(outputStreamReader);
            String metadata;
            outputStreamReader = new OutputStreamWriter(socket.getOutputStream(),Global.codePage);
            metadata = FormatCharacters.marker + 
                       String.valueOf(f.length()) +
                       FormatCharacters.marker +
                       f.getName() +
                       FormatCharacters.marker +
                       allids +
                       FormatCharacters.marker +
                       String.valueOf(Utils.Checksum(path)) +
                       FormatCharacters.marker;
            //socket.getOutputStream().write((String.valueOf(metadata.length()) + metadata).getBytes());
            //socket.getOutputStream().flush();
            bufferedWriter.write((String.valueOf(metadata.length()) + metadata));
            bufferedWriter.flush();
            bufferedWriter = null;
            fileInputStream = new FileInputStream(path);
            long len = f.length();
            long Len = len;
            int readed;
            byte [] packet = new byte[Global.PACKET_SIZE];
            currenttime = lasttime = Utils.GetDate();
            System.gc();
            //this.sleep(500);
            while(len > 0)
            {
                readed = fileInputStream.read(packet, 0, (int)Math.min(len,  Global.PACKET_SIZE));
                len -= readed;
                if(socket.isClosed() ||  !socket.isConnected()) break;
                socket.getOutputStream().write(packet, 0, readed);
                if(isClosed) break;
                currenttime = Utils.GetDate();
                if(currenttime - lasttime > 1000)
                {
                    lasttime = currenttime;
                    this.SetStatus(String.format("%.2f%%", 100.0 - 100.0 * len / Len));
                    this.CheckWait();
                }
            }
        }catch(Exception ee)
        {
            status = false;
            Log.WriteException(ee);
        }
        try {
           if(fileInputStream != null) fileInputStream.close();
        }catch(Exception ee) {}
        try {
            if( socket != null ) socket.close();
         }catch(Exception ee) {}
        try {
            if(bufferedWriter != null) bufferedWriter.close();
        }catch(Exception ee) {}
        if(!this.isClosed())
        {
            if(status) {
                this.SetStatus(String.format("%.2f%%", 100.0));
                AddSendedFileToScreen(ids, f.getName(), Utils.getAdekvatSize(f.length()));
            }
            else this.SetStatus("Failed");
        }
        socket = null;
        fileInputStream = null;
        bufferedWriter = null;
        System.gc();
    }
    private void SetStatus(final String s)
    {
        JTable table = sendFileDialogFrame.getTable();
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setValueAt(s, index, 1);
        table.repaint();
    }
    public void Close()
    {

        isClosed = true;

        try {
            // жестко вырубаем текущий аплоад
            this.socket.close();
        }
        catch(Exception e){}
        try {
            if(fileInputStream != null) fileInputStream.close();
        }catch(Exception e) {}
        
        sendFileDialogFrame.setVisible(false);
        sendFileDialogFrame = null;
        fileInputStream = null;
        this.interrupt();
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
        synchronized(this) {
            while (pleaseWait) {
                try {
                    if(this.isClosed()) this.Resume();
                    sleep(100);
                }
                catch (Exception e) { }
            }
        }
    }
    public void RemoveFilesFromQuene(int [] lineNumbers)
    {

        for(int i = 0; i < lineNumbers.length; i++)
        {
            if(lineNumbers[i] == index) {
                this.RemoveCurrentSendFileFromQuene();
                break;
            }
        }
        for(int i = 0; i < lineNumbers.length; i++)
        {
            fileQuene.remove(lineNumbers[i]);
            if(lineNumbers[i] <= index) index --;

        }

    }
    public void RemoveCurrentSendFileFromQuene()
    {
        // жестко вырубаем загрузку файла
        try {
            semaphore = new Semaphore(0);
            
            if(this.socket != null) this.socket.close();
            this.Resume();
            // ждем пока завершится цикл передающий файл
            // если этого не сделать то установится статус Failed не на той строчке
            // поскольку мы закрыли сокет то поток то цикл завершится быстро
            
            semaphore.acquire();
            semaphore = null;
            

        }catch(Exception e) {}
    }
    private void AddSendedFileToScreen(Set<Integer> toIds, String fileName, String size)
    {
        ScreenView sv = Global.getUser().getScreenView();
        for(Integer id:toIds)
        {
            sv.AddSendedFileToScreen(sv.getTreeUserNameById(id), fileName, size);
        }
    }

}
 
