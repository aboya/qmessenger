/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RegistrationForm;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import qmessenger.BaseControl;

/**
 *
 * @author Администратор
 */
public class UserNameField extends BaseControl {
    Text txt;
    public UserNameField(Composite composite) {
          txt = new Text(composite,  SWT.BORDER);
          txt.setDoubleClickEnabled(true);
          GridData data = new GridData(GridData.FILL_BOTH);
          txt.setLayoutData(data);
    }
    @Override
    public void Resize(Rectangle rect)
    {
        txt.setBounds(10, 10, 150, 20);
    }

}
