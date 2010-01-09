/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SendFileDialog;

import UserGUIControls.uMessageBox;
import clientapp.FormatCharacters;
import clientapp.Global;
import clientapp.Log;
import clientapp.Pair;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Set;
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
     SendFileDialogControls userControls;
     LinkedList<Pair<String, Set<Integer> > > fileQuene = new LinkedList<Pair<String, Set<Integer>>>();
    


    public SendFileDialogView(String WindowName) {
        display = Display.getCurrent();
        shell = new Shell(display);

        //shell = new Shell(display);
        userControls = new SendFileDialogControls(shell, display);
        shell.setText(WindowName);

    }
    public void AddFileToQuene(String [] filePaths, Set <Integer> ids)
    {
        Table table = userControls.getTable();
        for(int i = 0; i < filePaths.length; i++)
        {
            TableItem item = new TableItem(table, i);
            item.setText(filePaths[i]);
            fileQuene.addLast(new Pair<String, Set<Integer>>(filePaths[i], ids));
        }
    }
    public void SendFiles(String [] filePaths, Set <Integer> ids)
    {
        Table table = userControls.getTable();
        table.removeAll();
        fileQuene.clear();
        for(int i = 0; i < filePaths.length; i++)
        {
            TableItem item = new TableItem(table, i);
            item.setText(filePaths[i]);
            fileQuene.addLast(new Pair<String, Set<Integer>>(filePaths[i], ids));
        }
        this.start();
    }
    @Override
    public void run()
    {
        final Listener resizeListner = new Listener() {
               public void handleEvent(Event e)  {
                   Rectangle rect = shell.getBounds();
                   rect.width = Math.max(rect.width, 200);
                   rect.height = Math.max(rect.height, 200);
                   shell.setBounds(rect);
                   userControls.Resize(rect);
                }
         };
         
         display.syncExec(
           new Runnable() {
                public void run(){
                    shell.addListener(SWT.Resize, resizeListner);
                    shell.addListener(SWT.Move, resizeListner);
                    shell.setSize(200, 200);
                    shell.open();
                }
            }
         );
         this.SendFiles();
   }
    private void SendFiles()
    {
        Pair < String , Set<Integer> > p;
        while(!fileQuene.isEmpty())
        {
           p = fileQuene.getFirst();
           this.SendFile(p.getFirst(), p.getSecond());
           fileQuene.removeFirst();
        }
    }
    private void SendFile(String path, Set<Integer> ids)
    {
        File f = new File(path);
        Socket socket = null ;
        BufferedWriter bufferedWriter = null;
        FileReader fileReader = null;
        BufferedReader bufFileReader = null;
        try {
            String metadata;
            socket = new Socket(Global.ServAddr, Global.ServerFileUploadPort);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),Global.codePage));
            metadata = FormatCharacters.marker + 
                       String.valueOf(f.length()) +
                       FormatCharacters.marker +
                       f.getName() +
                       FormatCharacters.marker;
            bufferedWriter.write(String.valueOf(metadata.length()) + metadata);
            bufferedWriter.flush();

            fileReader = new FileReader(f);
            bufFileReader = new BufferedReader(fileReader);
            long len = f.length();
            int readed;
            char [] packet = new char[Global.PACKET_SIZE];
            while(len > 0)
            {
                if(len < Global.PACKET_SIZE) readed = bufFileReader.read(packet, 0, (int)len);
                else readed = bufFileReader.read(packet, 0, Global.PACKET_SIZE);
                len -= readed;
                bufferedWriter.write(packet, 0, readed);
                bufferedWriter.flush();
            }

        }catch(Exception ee)
        {
            uMessageBox msg = new uMessageBox("Can not send file" + path,  SWT.OK);
            Log.WriteException(ee);
        }
        try {

           if(bufferedWriter != null) bufferedWriter.close();
           if(fileReader != null) fileReader.close();
           if(bufFileReader != null) bufFileReader.close();
           if( socket != null ) socket.close();
        }catch(Exception ee) {}

    }
}
