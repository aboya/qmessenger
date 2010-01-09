/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SendFileDialog;

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
        fileList = new FileList(composite);
        controlsList.add(fileList);

        startPauseButton = new StartPauseButton(composite);
        controlsList.add(startPauseButton);

        removeSelectedButton = new RemoveSelectedButton(composite);
        controlsList.add(removeSelectedButton);

        SelectionListener sListner = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                display.syncExec(new Runnable() {
                    public void run() {
                        fileList.getTable().remove(fileList.getTable().getSelectionIndices());
                    }
                });

            }
            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {

            }
        };
        removeSelectedButton.getButton().addSelectionListener(sListner);
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
