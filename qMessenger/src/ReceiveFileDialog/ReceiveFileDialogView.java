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
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.DirectoryDialog;
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
public class ReceiveFileDialogView extends Thread {
     Shell shell;
     final Display display;
     boolean isClosed = false; // нужна для корректного выхода
     boolean pleaseWait = false;
     Integer index;
     Semaphore semaphore = null;
     /*
      * abortSetFileList нужна для синхронизации
      * когда юзверь удаляет текущий аплоад, мы закрываем сокет
      * и удалям строки из таблицы, но после закрытия сокета процедура еще может
      * вызвать Set таблицы, с неверным индеском. Вот что бы этого не происходило
      * устанавливаем abortSetFileList перед входом в функцию в фалс, и будем смотреть
      * на него в ходе выполнения.
      */

     Socket socket;
     ReceiveFileDialogControls userControls;
     LinkedList<Pair<String, Integer > > fileQuene = new LinkedList<Pair<String, Integer >>();
     FileOutputStream fileOutputStream = null;


    public ReceiveFileDialogView(final String WindowName) {
        display = Global.getDisplay();
        display.syncExec(new Runnable() {
            public void run() {
                shell = new Shell(display);
                DirectoryDialog dlg = new DirectoryDialog(shell);
                dlg.setFilterPath(Global.lastSavePath);
                Global.lastSavePath = dlg.open() + "\\";
            }
        });
        
        userControls = new ReceiveFileDialogControls(shell, display);

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
                   Global.getUser().getReceiveFileDialogView().Close();
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
    public void AddFileToQuene(int [] fileIds, String [] fileNames)
    {
        Table table = userControls.getTable();
        for(int i = 0; i < fileNames.length; i++)
        {
            TableItem item = new TableItem(table, i);
            item.setText(0, fileNames[i]);
            item.setText(1, "0%");
            fileQuene.addLast(new Pair<String, Integer >(fileNames[i], fileIds[i]));
        }
        this.start();
    }
    public void ReceiveFiles(final int [] fileIds, final String [] fileNames)
    {
        Global.getDisplay().syncExec(new Runnable() {
            public void run() {
                Table table = userControls.getTable();
                table.removeAll();
                fileQuene.clear();
                for(int i = 0; i < fileNames.length; i++)
                {
                    TableItem item = new TableItem(table, i);
                    item.setText(0, fileNames[i]);
                    item.setText(1, "0%");
                    fileQuene.addLast(new Pair<String, Integer >(fileNames[i], fileIds[i]));
                }
            }
        });
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
        this.socket = null ;
        fileOutputStream = null;
        byte [] packet = new byte[Global.PACKET_SIZE];
        long lasttime = 0, currenttime = 0;
        boolean status = true;
        String metadata;
        long fileSize, checkSum, Len;
        String fileName = "";
        try {
            socket = new Socket(Global.ServAddr, Global.ServerFileDownloadPort);
            metadata = ids.toString();
            metadata = Integer.valueOf(metadata.length()) + String.valueOf(FormatCharacters.marker) + metadata;
            socket.getOutputStream().write( metadata.getBytes());
            String _len = "";
            int len;
            int readed;
            while(true)
            {
                readed = socket.getInputStream().read(packet, 0, 1);
                if(readed <= 0) throw new Exception("Connection closed ..");
                if(packet[0] == FormatCharacters.marker) break;
                _len += packet[0] - '0';
            }
            len = Integer.valueOf(_len);
            socket.getInputStream().read(packet, 0, len);
            metadata = new String(packet, 0, len);
            String []arr = metadata.split(FormatCharacters.marker + "");
            fileSize = Long.valueOf(arr[0]);
            fileName = arr[1];
            checkSum = Long.valueOf(arr[2]);
            fileOutputStream = new FileOutputStream(Global.lastSavePath + fileName);
            Len = fileSize;
            while(fileSize > 0)
            {
                readed =  socket.getInputStream().read(packet, 0, Global.PACKET_SIZE);
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
            fileOutputStream.close();
            if(Utils.Checksum(Global.lastSavePath + fileName) != checkSum)
            {
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
        try{
           if(socket != null) socket.close();
        }catch(Exception ee) {}
        if(!this.isClosed())
        {
            if(status) {
                this.SetStatus(String.format("%.2f%%", 100.0));
            }
            else {
                this.SetStatus("Failed");
                new File(Global.lastSavePath + fileName).delete();
            }
        }
        try {
        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
        if(semaphore != null)
        {
            // отпускаем поток ждущий завершения этого цикла
            semaphore.release();
            // теперь сами ждем его завершения
            try {
             this.Pause();
            }catch(Exception ee) {}
        }
    }
    private void SetStatus(final String s)
    {
          display.syncExec(
           new Runnable() {
                public void run(){
                    if(isClosed()) return;
                    if(index < 0) return;
                    Table table = userControls.getTable();
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
            Global.getDisplay().syncExec(new Runnable() {
                public void run() {
                    shell.dispose();
                }
            });
        }
        try {
            // жестко вырубаем текущий аплоад
            this.socket.close();
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
            while (pleaseWait) {
                try {
                    if(this.isClosed()) this.Resume();
                    wait(100);
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
    public void RemoveCurrentReceiveFileFromQuene()
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

}
 class RemoveTask extends Thread
 {
     ReceiveFileDialogView receiveFile;
     int []selectedIndexes;
     Table table;

     public RemoveTask(ReceiveFileDialogView view, int [] indexes, Table _table) {
         this.receiveFile = view;
         this.selectedIndexes = indexes;
         this.table = _table;
     }

     @Override
     public void run() {
           if(selectedIndexes.length == 0) return;
           try {
                  // делаем паузу для синхронизации
                 Global.getUser().getReceiveFileDialogView().Pause();
           }catch(Exception e)
           {
               Log.WriteException(e);
           }
           receiveFile.RemoveFilesFromQuene(selectedIndexes);
           Global.getDisplay().syncExec(new Runnable() {
                  public void run() {
                       table.remove(selectedIndexes);
                  }
                });
           Global.getUser().getReceiveFileDialogView().Resume();
    }
}

