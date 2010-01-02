/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package qmessenger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * @author Администратор
 */
public class StatusBar extends BaseControl {
     final org.eclipse.swt.widgets.Label lbl;

    public StatusBar(Shell shell) {
        super.ControlName = "statusBar";
        lbl = new org.eclipse.swt.widgets.Label(shell, SWT.BORDER);
        lbl.setText("status bar");
    }
    @Override
    public void Resize(Rectangle rect)
    {
        lbl.setBounds(0, rect.height-58, rect.width-20, 20);
    }
    public void SetText(String txt)
    {
        lbl.setText(txt);
    }

}
