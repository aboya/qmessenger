/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SendFileDialog;

import UserGUIControls.uMessageBox;
import clientapp.Global;
import clientapp.Log;
import java.util.Vector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import qmessenger.BaseControl;

/**
 *
 * @author Администратор
 */
public class SendFileDialogControls {
    Shell composite;
    Display display;
    Vector<BaseControl> controlsList;
    FileList fileList;
    StartPauseButton startPauseButton;
    RemoveSelectedButton removeSelectedButton;

    public SendFileDialogControls(Shell _composite, Display _display) {
        composite = _composite;
        display = _display;
        controlsList = new Vector<BaseControl>();
        display.syncExec( new Runnable() {

            public void run() {
                 fileList = new FileList(composite);
                 controlsList.add(fileList);

                 startPauseButton = new StartPauseButton(composite);
                 startPauseButton.getButton().setText("Pause");
                 controlsList.add(startPauseButton);

                 removeSelectedButton = new RemoveSelectedButton(composite);
                 controlsList.add(removeSelectedButton);

                 SelectionListener sListner = new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {

                        if(fileList.getTable().getSelectionIndices().length == 0) return;
                        fileList.getTable().setRedraw(false);
                        try {
                            // делаем паузу для синхронизации
                           Global.getUser().getSendFileDialogView().Pause();
                        }catch(Exception e) 
                        {
                            Log.WriteException(e);
                        }
                        Global.getUser().getSendFileDialogView().RemoveFilesFromQuene(fileList.getTable().getSelectionIndices());
                        display.syncExec(new Runnable() {
                            public void run() {
                                 fileList.getTable().remove(fileList.getTable().getSelectionIndices());
                            }
                        });
                        fileList.getTable().setRedraw(true);
                        fileList.getTable().redraw();
                        Global.getUser().getSendFileDialogView().Resume();
                    }
                };
                 removeSelectedButton.getButton().addSelectionListener(sListner);
                 sListner = new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent arg0) {
                        display.asyncExec(new Runnable() {
                            public void run()  {
                                try {
                                    if(startPauseButton.getButton().getText().equals("Pause"))
                                    {
                                        startPauseButton.getButton().setText("Start");
                                        Global.getUser().getSendFileDialogView().Pause();
                                    }
                                    else {
                                        Global.getUser().getSendFileDialogView().Resume();
                                        startPauseButton.getButton().setText("Pause");
                                    }
                                }catch(Exception e)
                                {
                                    Log.WriteException(e);
                                    //uMessageBox msg = new uMessageBox(e.getMessage(), SWT.ABORT);
                                    //msg.open();
                                }
                            }
                        });
                    }
                };
                 startPauseButton.getButton().addSelectionListener(sListner);
            }
        });
    }
    public void Resize(Rectangle rect)
    {
        try {
             for(BaseControl bc : controlsList)
                 bc.Resize(rect);
        }catch(Exception ee)
        {
            Log.WriteException(ee);
        }
    }
    public Table getTable()
    {
        return  fileList.getTable();
    }
}
