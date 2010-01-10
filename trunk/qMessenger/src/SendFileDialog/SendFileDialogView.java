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
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;
import javax.swing.text.AbstractDocument.LeafElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 *
 * @author Администратор
 */
public class SendFileDialogView extends Thread {
     final Shell shell;
     final Display display;
     boolean isClosed = false; // нужна для корректного выхода
     boolean pleaseWait = false;
     Integer index;
     /*
      * abortSetFileList нужна для синхронизации
      * когда юзверь удаляет текущий аплоад, мы закрываем сокет
      * и удалям строки из таблицы, но после закрытия сокета процедура еще может
      * вызвать Set таблицы, с неверным индеском. Вот что бы этого не происходило
      * устанавливаем abortSetFileList перед входом в функцию в фалс, и будем смотреть
      * на него в ходе выполнения.
      */

     Socket socket;
     SendFileDialogControls userControls;
     LinkedList<Pair<String, Set<Integer> > > fileQuene = new LinkedList<Pair<String, Set<Integer>>>();
     FileInputStream fileInputStream = null;


    public SendFileDialogView(final String WindowName) {
        display = Display.getCurrent();
        shell = new Shell(display);
        userControls = new SendFileDialogControls(shell, display);
       
        final Listener resizeListner = new Listener() {
               public void handleEvent(Event e)  {
                   shell.setRedraw(false);
                   Rectangle rect = shell.getBounds();
                   rect.width = Math.max(rect.width, 400);
                   rect.height = Math.max(rect.height, 400);
                   shell.setBounds(rect);
                   userControls.Resize(rect);
                   shell.setRedraw(true);
                }
         };
        final Listener closeListner = new Listener() {
               public void handleEvent(Event e)  {
                   Global.getUser().getSendFileDialogView().Close();
                }
         };
         display.syncExec(
           new Runnable() {
                public void run(){
                    shell.setText(WindowName);
                    shell.addListener(SWT.Resize, resizeListner);
                    shell.addListener(SWT.Move, resizeListner);
                    shell.addListener(SWT.Close, closeListner);
                    shell.setSize(200, 200);
                    shell.open();
                }
            }
         );

    }
    public void AddFileToQuene(String path, String [] filePaths, Set <Integer> ids)
    {
        Table table = userControls.getTable();
        for(int i = 0; i < filePaths.length; i++)
        {
            TableItem item = new TableItem(table, i);
            item.setText(0, filePaths[i]);
            item.setText(1, "0%");
            fileQuene.addLast(new Pair<String, Set<Integer>>(path + filePaths[i], ids));
        }
        this.start();
    }
    public void SendFiles(String path, String [] filePaths, Set <Integer> ids)
    {
        Table table = userControls.getTable();
        table.removeAll();
        fileQuene.clear();
        for(int i = 0; i < filePaths.length; i++)
        {
            TableItem item = new TableItem(table, i);
            item.setText(0, filePaths[i]);
            item.setText(1, "0%");
            fileQuene.addLast(new Pair<String, Set<Integer>>(path + filePaths[i], ids));
        }
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
        long lasttime = 0, currenttime = 0;
        boolean status = true;
        try {
            String metadata;
            socket = new Socket(Global.ServAddr, Global.ServerFileUploadPort);
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
            socket.getOutputStream().write((String.valueOf(metadata.length()) + metadata).getBytes());
            fileInputStream = new FileInputStream(path);
            long len = f.length();
            long Len = len;
            int readed;
            byte [] packet = new byte[Global.PACKET_SIZE];
            currenttime = lasttime = Utils.GetDate();
            while(len > 0)
            {
                 
                readed = fileInputStream.read(packet, 0,  Global.PACKET_SIZE);
                len -= readed;
                if(socket.isClosed() ||  !socket.isConnected()) break;
                socket.getOutputStream().write(packet, 0, readed);
                if(isClosed) break;
                currenttime = Utils.GetDate();
                if(currenttime - lasttime > 1000)
                {
                    lasttime = currenttime;
                    this.SetStatus(String.format("%.2f%%", 100.0 - 100.0 * len / Len),this.getIndexByPath(path));
                    this.CheckWait();
                }
            }
            status = socket.getInputStream().read() == 1;
        }catch(Exception ee)
        {
            status = false;
            Log.WriteException(ee);
        }
        try {

           if(fileInputStream != null) fileInputStream.close();
           if( socket != null ) socket.close();

        }catch(Exception ee) {}
        if(!this.isClosed())
        {
            if(status) this.SetStatus(String.format("%.2f%%", 100.0), this.getIndexByPath(path));
            else this.SetStatus("Failed", this.getIndexByPath(path));
        }
    }
    private void SetStatus(final String s, final int lineNumber)
    {  
          display.syncExec(
           new Runnable() {
                public void run(){
                    if(isClosed()) return;
                    if(index < 0) return;
                    Table table = userControls.getTable();
                    /*поскольку юзер может удалить закачку то индексы изменятся,
                     * и поскольку мы в конце вызываем SetStatus - он может ссылатся на недопустимую строку
                     * этот костыль убирает проблему.  
                     *
                     * */
                    table.getItem(index).setText(1, s);
                }
            }
         );
    }
    public void Close()
    {

        isClosed = true;
        if(this.shell != null && !this.shell.isDisposed())
        {
            // если вызовим close - сработает перехватчик событий и вызовит эту функцию опять, и получится
            // бесконечная рекурсия
            this.shell.dispose();
        }
        try {
            // жестко вырубаем текущий аплоад
            this.socket.close();
            if(fileInputStream != null) fileInputStream.close();
            this.interrupt();
        }catch(Exception e) {}
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
        synchronized (this) {
            while (pleaseWait) {
                try {
                    if(this.isClosed()) this.Resume();
                    wait(100);
                }
                catch (Exception e) { }
            }
        }
    }
    public void RemoveFilesFromQuene(int [] lineNumbers)
    {
        for(int i = 0; i < lineNumbers.length; i++)
        {
            fileQuene.remove(lineNumbers[i]);
            if(lineNumbers[i] == index) this.RemoveCurrentSendFileFromQuene();
            if(lineNumbers[i] <= index) index --;

        }
    }
    public void RemoveCurrentSendFileFromQuene()
    {
        // жестко вырубаем загрузку файла
        try {
            if(this.socket != null) this.socket.close();
        }catch(Exception e) {}
    }

    private int getIndexByPath(String path)
    {
        int i = 0;
        for(Pair p : fileQuene)
        {
            if(p.getFirst().equals(path)) return i;
            i++;
        }
        return -1;
    }
}
