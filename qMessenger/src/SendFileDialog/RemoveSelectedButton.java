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
public class RemoveSelectedButton extends BaseControl {
    Button btn;
    public RemoveSelectedButton(Shell composite) {
        this.ControlName = "RemoveSelectedButton";
        btn = new Button(composite, SWT.PUSH);
        btn.setText("remove");
    }

    @Override
    public void Resize(Rectangle rect) {
        btn.setBounds(rect.width - 75, 65, 60, 40);
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
