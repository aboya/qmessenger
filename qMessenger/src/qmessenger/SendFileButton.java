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


    }
    @Override
    public void Resize(Rectangle rect) {
       btn.setBounds(10, rect.height - 200, 50, 20);
    }

    @Override
    public String getControlName() {
        return super.getControlName();
    }
    public Button getButton()
    {
        return btn;
    }
}
