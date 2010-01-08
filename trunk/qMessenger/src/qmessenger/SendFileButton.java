/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

import clientapp.Global;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Администратор
 */
public class SendFileButton extends BaseControl {
    private Button btn;
    private final Shell composite;

    public SendFileButton(Shell _composite) {
        composite = _composite;
        this.ControlName = "SendFileButton";
        btn = new Button(composite, SWT.PUSH);
        btn.setText("Send File");

         SelectionListener sListner = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent arg0) {
                FileDialog fd = new FileDialog(composite, SWT.MULTI);
                fd.setText("Open");
                fd.setFilterPath(Global.lastOpenPath);
                String[] filterExt = { "*.*" };
                fd.setFilterExtensions(filterExt);
                String selected = fd.open();
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent arg0) {

            }
        };

        btn.addSelectionListener(sListner);
    }
    @Override
    public void Resize(Rectangle rect) {
       btn.setBounds(10, rect.width - 200, 50, 20);
    }

    @Override
    public String getControlName() {
        return super.getControlName();
    }
}
