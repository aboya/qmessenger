/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ReceiveFileDialog;

/**
 *
 * @author aboimov
 */




import SendFileDialog.FileList;
import SendFileDialog.RemoveSelectedButton;
import SendFileDialog.StartPauseButton;
import clientapp.Global;
import clientapp.Log;
import java.util.Vector;
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
public class ReceiveFileDialogControls {
    Shell composite;
    Display display;
    Vector<BaseControl> controlsList;
    FileList fileList;
    StartPauseButton startPauseButton;
    RemoveSelectedButton removeSelectedButton;
    RemoveTask removeTask;

    public ReceiveFileDialogControls(Shell _composite, Display _display) {
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
                        if(removeTask != null && removeTask.isAlive()) return;
                        removeTask = new RemoveTask(Global.getUser().getReceiveFileDialogView(), fileList.getTable().getSelectionIndices(), fileList.getTable());
                        removeTask.start();
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
                                        Global.getUser().getReceiveFileDialogView().Pause();
                                    }
                                    else {
                                        Global.getUser().getReceiveFileDialogView().Resume();
                                        startPauseButton.getButton().setText("Pause");
                                    }
                                }catch(Exception e)
                                {
                                    Log.WriteException(e);
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
