/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SendFileDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import qmessenger.BaseControl;

/**
 *
 * @author Администратор
 */
public class StartPauseButton extends BaseControl {
    Button btn;

    public StartPauseButton(Shell _composite) {
        btn = new Button(_composite, SWT.PUSH);
        this.ControlName = "StartPauseButton";
        btn.setText("start");
    }

    @Override
    public void Resize(Rectangle rect) {
        btn.setBounds(rect.width - 75, 10, 60, 40);

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
