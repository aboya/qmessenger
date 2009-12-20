/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import qmessenger.BaseControl;

/**
 *
 * @author Администратор
 */
public class RegButton extends BaseControl {
    final Button button;
    Composite shell;

    public RegButton(Composite _shell, String text)
    {
         this.shell = _shell;
         super.ControlName = "RegButton";
         button = new Button(shell, SWT.PUSH);
         button.setText(text);

    }

    @Override
    public void Resize(Rectangle rect) {
        button.setBounds(40, 50, 50, 20);
    }
    public void addListner(Listener listner, int type)
    {
        button.addListener(type, listner);

    }


}
